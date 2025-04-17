package com._2.BookIt.Service;
import com._2.BookIt.Dto.BookingCount;
import com._2.BookIt.Dto.RestaurantSearchResponse;
import com._2.BookIt.Dto.ReviewCount;
import com._2.BookIt.Model.Booking;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Model.Table;
import com._2.BookIt.Repository.BookingRepository;
import com._2.BookIt.Repository.RestaurantRepository;

// Spring packages
import com._2.BookIt.Repository.ReviewRepository;
import com._2.BookIt.Repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.bson.types.ObjectId;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantSearchService {

    @Autowired
    private RestaurantRepository restaurantRepo;

    @Autowired
    private TableRepository tableRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private ReviewRepository reviewRepo;

    public List<RestaurantSearchResponse> search(String name, String location, String state, String zipCode, String datetimeStr, int people) {
        Date now = new Date();
        Date inputTime = now;

        if (datetimeStr != null) {
            inputTime = Date.from(Instant.parse(datetimeStr));
        }

        List<Date> timesToCheck = new ArrayList<>();
        timesToCheck.add(shiftTime(inputTime, -30));
        timesToCheck.add(inputTime);
        timesToCheck.add(shiftTime(inputTime, 30));

        List<Restaurant> restaurants = restaurantRepo.findAll().stream()
                .filter(r -> r.getStatus().toString().equals("ACTIVE"))
                .filter(r -> {
                    if (zipCode != null) {
                        return r.getAddress().getZipCode().equalsIgnoreCase(zipCode);
                    } else if (state != null) {
                        return r.getAddress().getState().equalsIgnoreCase(state);
                    } else if (location != null) {
                        return r.getAddress().getCity().equalsIgnoreCase(location);
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());

        if (name != null && !name.isBlank()) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (restaurants.isEmpty()) return List.of();

        List<ObjectId> restaurantIds = restaurants.stream().map(Restaurant::getId).toList();
        List<Table> allTables = tableRepo.findAllByRestaurantIDIn(restaurantIds);
        Map<ObjectId, List<Table>> tablesByRestaurant = allTables.stream()
                .filter(t -> t.getCapacity() >= people)
                .collect(Collectors.groupingBy(Table::getRestaurantID));

        List<Booking> bookings = bookingRepo.findByRestaurantIDInAndDateTimeInAndStatusIn(
                restaurantIds,
                timesToCheck,
                List.of("confirmed", "pending")
        );

        Map<Date, Map<ObjectId, Set<ObjectId>>> bookedTables = new HashMap<>();
        for (Date time : timesToCheck) bookedTables.put(time, new HashMap<>());

        for (Booking b : bookings) {
            Date bTime = b.getDateTime();
            bookedTables.get(bTime).computeIfAbsent(b.getRestaurantID(), k -> new HashSet<>()).add(b.getTableID());
        }

        Date startOfDay = Date.from(inputTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date endOfDay = Date.from(inputTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1).atStartOfDay().minusSeconds(1).atZone(ZoneId.systemDefault()).toInstant());

        Map<ObjectId, Long> bookedTodayMap = new HashMap<>();
        for (BookingCount bc : bookingRepo.countConfirmedTodayByRestaurant(restaurantIds, startOfDay, endOfDay)) {
            bookedTodayMap.put(bc.getId(), bc.getCount());
        }

        Map<ObjectId, Long> reviewCountMap = new HashMap<>();
        for (ReviewCount rc : reviewRepo.countReviewsByRestaurant(restaurantIds)) {
            reviewCountMap.put(rc.getId(), rc.getCount());
        }

        List<RestaurantSearchResponse> result = new ArrayList<>();
        for (Restaurant r : restaurants) {
            ObjectId restId = r.getId();
            List<Table> tables = tablesByRestaurant.getOrDefault(restId, List.of());
            List<String> availableAt = new ArrayList<>();

            for (Date slot : timesToCheck) {
                Set<ObjectId> bookedSet = bookedTables.getOrDefault(slot, Map.of()).getOrDefault(restId, Set.of());
                boolean available = tables.stream().anyMatch(t -> !bookedSet.contains(t.getId()));
                if (available) availableAt.add(format(slot));
            }

            if (!availableAt.isEmpty()) {
                result.add(new RestaurantSearchResponse(
                        r.getName(),
                        r.getCuisine(),
                        r.getCostRating(),
                        r.getAvgStarRating(),
                        reviewCountMap.getOrDefault(restId, 0L),
                        bookedTodayMap.getOrDefault(restId, 0L),
                        availableAt
                ));
            }
        }

        return result.stream().sorted(Comparator.comparingDouble(RestaurantSearchResponse::getAvgRating).reversed()).limit(10).toList();
    }

    private Date shiftTime(Date date, int minutes) {
        return Date.from(date.toInstant().plus(minutes, ChronoUnit.MINUTES));
    }

    private String format(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime().truncatedTo(ChronoUnit.MINUTES).toString();
    }
}
