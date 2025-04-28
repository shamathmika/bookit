package com._2.BookIt.Service;

import com._2.BookIt.Dto.BookingCount;
import com._2.BookIt.Dto.RestaurantDetailsResponse;
import com._2.BookIt.Dto.RestaurantSearchResponse;
import com._2.BookIt.Dto.ReviewCount;
import com._2.BookIt.Model.Booking;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Model.Review;
import com._2.BookIt.Model.Table;
import com._2.BookIt.Repository.BookingRepository;
import com._2.BookIt.Repository.RestaurantRepository;
import com._2.BookIt.Repository.ReviewRepository;
import com._2.BookIt.Repository.TableRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    public List<RestaurantSearchResponse> search(
            String name,
            String location,
            String state,
            String zipCode,
            String datetimeStr,
            int people
    ) {
        Date now = new Date();
        Date inputTime = now;
        if (datetimeStr != null) {
            inputTime = Date.from(Instant.parse(datetimeStr));
        }

        List<Date> timesToCheck = List.of(
            shiftTime(inputTime, -30),
            inputTime,
            shiftTime(inputTime, 30)
        );

        List<Restaurant> restaurants = restaurantRepo.findAll().stream()
            .filter(r -> r.getStatus().toString().equals("ACTIVE"))
            .filter(r -> {
                if (zipCode != null) return r.getAddress().getZipCode().equalsIgnoreCase(zipCode);
                if (state   != null) return r.getAddress().getState().equalsIgnoreCase(state);
                if (location!= null) return r.getAddress().getCity().equalsIgnoreCase(location);
                return true;
            })
            .collect(Collectors.toList());

        if (name != null && !name.isBlank()) {
            restaurants = restaurants.stream()
                .filter(r -> r.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        }
        if (restaurants.isEmpty()) return List.of();

        List<ObjectId> restaurantIds = restaurants.stream()
                .map(Restaurant::getId)
                .toList();

        // only tables big enough
        Map<ObjectId, List<Table>> tablesByRestaurant = tableRepo.findAllByRestaurantIDIn(restaurantIds).stream()
                .filter(t -> t.getCapacity() >= people)
                .collect(Collectors.groupingBy(Table::getRestaurantID));

        // bookings
        List<Booking> bookings = bookingRepo.findByRestaurantIDInAndDateTimeInAndStatusIn(
                restaurantIds, timesToCheck, List.of("confirmed","pending")
        );
        Map<Date, Map<ObjectId, Set<ObjectId>>> booked = new HashMap<>();
        for (Date slot : timesToCheck) booked.put(slot, new HashMap<>());
        for (Booking b : bookings) {
            booked.get(b.getDateTime())
                  .computeIfAbsent(b.getRestaurantID(), k -> new HashSet<>())
                  .add(b.getTableID());
        }

        // counts
        Date startOfDay = Date.from(inputTime.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate().atStartOfDay(ZoneId.systemDefault())
            .toInstant());
        Date endOfDay = Date.from(inputTime.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusSeconds(1)
            .toInstant());

        Map<ObjectId, Long> bookedTodayMap = new HashMap<>();
        for (BookingCount bc : bookingRepo.countConfirmedTodayByRestaurant(restaurantIds, startOfDay, endOfDay)) {
            bookedTodayMap.put(bc.getId(), bc.getCount());
        }
        Map<ObjectId, Long> reviewCountMap = new HashMap<>();
        for (ReviewCount rc : reviewRepo.countReviewsByRestaurant(restaurantIds)) {
            reviewCountMap.put(rc.getId(), rc.getCount());
        }

        // assemble
        List<RestaurantSearchResponse> result = new ArrayList<>();
        for (Restaurant r : restaurants) {
            ObjectId restId = r.getId();
            List<Table> tables = tablesByRestaurant.getOrDefault(restId, List.of());
            List<String> availableAt = new ArrayList<>();
            for (Date slot : timesToCheck) {
                Set<ObjectId> busy = booked.get(slot).getOrDefault(restId, Set.of());
                if (tables.stream().anyMatch(t -> !busy.contains(t.getId()))) {
                    availableAt.add(format(slot));
                }
            }
            if (!availableAt.isEmpty()) {
                result.add(new RestaurantSearchResponse(
                        restId.toHexString(),       // ← pass ID
                        r.getName(),
                        r.getCuisine(),
                        r.getCostRating(),
                        r.getAvgStarRating(),
                        r.getPhotos(),
                        reviewCountMap.getOrDefault(restId, 0L),
                        bookedTodayMap.getOrDefault(restId, 0L),
                        availableAt
                ));
            }
        }

        return result.stream()
                     .sorted(Comparator.comparingDouble(RestaurantSearchResponse::getAvgRating).reversed())
                     .limit(10)
                     .toList();
    }

    public RestaurantDetailsResponse getRestaurantDetails(String id) {
        ObjectId objectId = new ObjectId(id);
        Restaurant r = restaurantRepo.findById(objectId).orElseThrow();
        List<Review> reviews = reviewRepo.findByRestaurantID(objectId);

        Date now = new Date();
        Date startOfDay = Date.from(now.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate().atStartOfDay(ZoneId.systemDefault())
            .toInstant());
        Date endOfDay = Date.from(now.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate().plusDays(1).atStartOfDay(ZoneId.systemDefault()).minusSeconds(1)
            .toInstant());

        long bookedToday = bookingRepo.countConfirmedTodayByRestaurant(List.of(objectId), startOfDay, endOfDay)
                                      .stream().findFirst().map(BookingCount::getCount).orElse(0L);

        long totalReviews = reviewRepo.countReviewsByRestaurant(List.of(objectId))
                                      .stream().findFirst().map(ReviewCount::getCount).orElse(0L);

        double[] coords = r.getAddress().getLocation().getCoordinates();
        String mapsUrl = String.format(
            "https://www.google.com/maps?q=%f,%f&z=15&output=embed",
            coords[1], coords[0]
        );

        return new RestaurantDetailsResponse(
                r.getId().toHexString(),      // ← pass ID
                r.getName(),
                r.getDescription(),
                r.getContact(),
                r.getCuisine(),
                r.getCostRating(),
                r.getAvgStarRating(),
                totalReviews,
                bookedToday,
                r.getAddress().getStreet(),
                r.getAddress().getCity(),
                r.getAddress().getState(),
                r.getAddress().getZipCode(),
                coords,
                mapsUrl,
                r.getPhotos(),
                reviews
        );
    }

    private Date shiftTime(Date date, int minutes) {
        return Date.from(date.toInstant().plus(minutes, ChronoUnit.MINUTES));
    }

    private String format(Date date) {
        return date.toInstant()
                   .atZone(ZoneId.systemDefault())
                   .toLocalTime()
                   .truncatedTo(ChronoUnit.MINUTES)
                   .toString();
    }
}
