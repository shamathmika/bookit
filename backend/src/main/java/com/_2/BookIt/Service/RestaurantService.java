package com._2.BookIt.Service;

import com._2.BookIt.Dto.AvailableRestaurantResponse;
import com._2.BookIt.Dto.BookingCount;
import com._2.BookIt.Dto.ReviewCount;
import com._2.BookIt.Model.Booking;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Model.Table;
import com._2.BookIt.Repository.BookingRepository;
import com._2.BookIt.Repository.RestaurantRepository;
import com._2.BookIt.Repository.ReviewRepository;
import com._2.BookIt.Repository.TableRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
    @Autowired
    private RestaurantRepository restaurantRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    @Autowired
    private TableRepository tableRepo;

    @Autowired
    private BookingRepository bookingRepo;

    public List<AvailableRestaurantResponse> getAvailableTables(String location) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        LocalDateTime nowPlus30 = now.plusMinutes(30);

        List<Restaurant> restaurants = restaurantRepo.findByAddress_CityAndStatus(location, "ACTIVE");
        List<ObjectId> restaurantIds = restaurants.stream()
                .map(Restaurant::getId)
                .collect(Collectors.toList());

        List<Table> allTables = tableRepo.findAllByRestaurantIDIn(restaurantIds);
        Map<ObjectId, List<Table>> tablesByRestaurant = allTables.stream()
                .collect(Collectors.groupingBy(Table::getRestaurantID));

        List<Booking> bookings = bookingRepo.findByRestaurantIDInAndDateTimeInAndStatusIn(
                restaurantIds,
                List.of(toDate(now), toDate(nowPlus30)),
                List.of("confirmed", "pending")
        );

        Map<ObjectId, Set<ObjectId>> bookedNow = new HashMap<>();
        Map<ObjectId, Set<ObjectId>> booked30 = new HashMap<>();

        for (Booking b : bookings) {
            ObjectId rid = b.getRestaurantID();
            if (b.getDateTime().equals(now)) {
                bookedNow.computeIfAbsent(rid, k -> new HashSet<>()).add(b.getTableID());
            } else if (b.getDateTime().equals(nowPlus30)) {
                booked30.computeIfAbsent(rid, k -> new HashSet<>()).add(b.getTableID());
            }
        }

        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        Map<ObjectId, Long> bookedTodayMap = new HashMap<>();
        for (BookingCount bc : bookingRepo.countConfirmedTodayByRestaurant(restaurantIds, toDate(startOfDay), toDate(endOfDay))) {
            bookedTodayMap.put(bc.getId(), bc.getCount());
        }

        Map<ObjectId, Long> reviewCountMap = new HashMap<>();
        for (ReviewCount rc : reviewRepo.countReviewsByRestaurant(restaurantIds)) {
            reviewCountMap.put(rc.getId(), rc.getCount());
        }

        List<AvailableRestaurantResponse> result = new ArrayList<>();
        for (Restaurant r : restaurants) {
            ObjectId restId = r.getId();
            List<Table> tables = tablesByRestaurant.getOrDefault(restId, List.of());
            Set<ObjectId> bn = bookedNow.getOrDefault(restId, Set.of());
            Set<ObjectId> b30 = booked30.getOrDefault(restId, Set.of());

            boolean hasNow = tables.stream().anyMatch(t -> !bn.contains(t.getId()));
            boolean has30 = tables.stream().anyMatch(t -> !b30.contains(t.getId()));

            if (hasNow || has30) {
                List<String> times = new ArrayList<>();
                if (hasNow) times.add("NOW");
                if (has30) times.add("NOW + 30 MIN");

                result.add(new AvailableRestaurantResponse(
                        restId.toHexString(),          // ← pass ID as String
                        r.getName(),
                        r.getCuisine(),
                        r.getCostRating(),
                        r.getAvgStarRating(),
                        reviewCountMap.getOrDefault(restId, 0L),
                        bookedTodayMap.getOrDefault(restId, 0L),
                        times
                ));
            }
        }

        return result.stream()
                     .sorted(Comparator.comparingDouble(AvailableRestaurantResponse::getAvgRating).reversed())
                     .limit(10)
                     .toList();
    }

    private Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
