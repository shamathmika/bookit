package com._2.BookIt.Service;

import com._2.BookIt.Dto.*;
import com._2.BookIt.Model.*;
import com._2.BookIt.Repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
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

    @Autowired
    private UserRepository userRepository;

    private static final ZoneId ZONE_SJ = ZoneId.of("America/Los_Angeles");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a", Locale.US);

    public List<RestaurantSearchResponse> search(
            String name,
            String location,
            String state,
            String zipCode,
            String datetimeStr,
            int people
    ) {
        ZonedDateTime now = ZonedDateTime.now(ZONE_SJ).truncatedTo(ChronoUnit.MINUTES);
        ZonedDateTime inputTime = now;
        boolean isToday = true;
        if (datetimeStr != null) {
            inputTime = Instant.parse(datetimeStr).atZone(ZONE_SJ);
            isToday = inputTime.toLocalDate().isEqual(now.toLocalDate());
        }

        List<ZonedDateTime> slotsToCheck;
        if (isToday && inputTime.isBefore(now.plusMinutes(30))) {
            int minute = now.getMinute();
            int minutesToNextSlot = (minute % 30 == 0) ? 0 : 30 - (minute % 30);
            ZonedDateTime slot0 = now.plusMinutes(minutesToNextSlot).truncatedTo(ChronoUnit.MINUTES);
            ZonedDateTime slot1 = slot0.plusMinutes(30);
            slotsToCheck = List.of(slot0, slot1);
        } else {
            long minute = inputTime.getMinute();
            long minuteRoundedDown = (minute / 30) * 30;
            ZonedDateTime slotMid = inputTime.withMinute((int) minuteRoundedDown).withSecond(0).withNano(0);
            ZonedDateTime slotBefore = slotMid.minusMinutes(30);
            ZonedDateTime slotAfter = slotMid.plusMinutes(30);
            slotsToCheck = List.of(slotBefore, slotMid, slotAfter);
        }

        List<Restaurant> restaurants = restaurantRepo.findAll().stream()
                .filter(r -> r.getStatus().toString().equals("ACTIVE"))
                .filter(r -> {
                    if (zipCode != null) return r.getAddress().getZipCode().equalsIgnoreCase(zipCode);
                    if (state != null) return r.getAddress().getState().equalsIgnoreCase(state);
                    if (location != null) return r.getAddress().getCity().equalsIgnoreCase(location);
                    return true;
                })
                .collect(Collectors.toList());

        if (name != null && !name.isBlank()) {
            restaurants = restaurants.stream()
                    .filter(r -> r.getName().toLowerCase().contains(name.toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (restaurants.isEmpty()) return List.of();

        List<ObjectId> restaurantIds = restaurants.stream().map(Restaurant::getId).toList();

        Map<ObjectId, List<Table>> tablesByRestaurant = tableRepo.findAllByRestaurantIDIn(restaurantIds).stream()
                .filter(t -> t.getCapacity() >= people)
                .collect(Collectors.groupingBy(Table::getRestaurantID));

        ZonedDateTime startOfDay = inputTime.toLocalDate().atStartOfDay(ZONE_SJ);
        ZonedDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        Map<ObjectId, Long> bookedTodayMap = new HashMap<>();
        for (BookingCount bc : bookingRepo.countConfirmedTodayByRestaurant(
                restaurantIds,
                Date.from(startOfDay.toInstant()),
                Date.from(endOfDay.toInstant())
        )) {
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
            if (tables.isEmpty()) continue;

            List<String> availableAt = new ArrayList<>();
            LocalTime opening = LocalTime.parse(r.getOpeningTime().toUpperCase(), TIME_FORMATTER);
            LocalTime closing = LocalTime.parse(r.getClosingTime().toUpperCase(), TIME_FORMATTER);
            int totalTables = tables.size();

            List<ObjectId> tableIds = tables.stream().map(t -> new ObjectId(t.getId())).toList();
            List<Booking> bookingsToday = bookingRepo.findByTableIDInAndDateTimeBetweenAndStatusIn(
                    tableIds,
                    Date.from(startOfDay.toInstant()),
                    Date.from(endOfDay.toInstant()),
                    List.of("confirmed", "pending")
            );

            Map<String, Long> bookingCountBySlot = bookingsToday.stream()
                    .map(booking -> booking.getDateTime().toInstant().atZone(ZONE_SJ).toLocalTime()
                            .truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("HH:mm")))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            for (ZonedDateTime slot : slotsToCheck) {
                LocalTime slotTime = slot.toLocalTime();
                if (slotTime.isBefore(opening) || !slotTime.isBefore(closing)) continue;

                String slotStr = slotTime.format(DateTimeFormatter.ofPattern("HH:mm"));
                long bookedCount = bookingCountBySlot.getOrDefault(slotStr, 0L);
                if (bookedCount < totalTables) {
                    availableAt.add(slotStr);
                }
            }

            if (!availableAt.isEmpty()) {
                result.add(new RestaurantSearchResponse(
                        restId.toHexString(),
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
        if (id == null || id.length() != 24) {
            throw new IllegalArgumentException("Invalid restaurant ID: " + id);
        }

        ObjectId objectId = new ObjectId(id);
        Restaurant r = restaurantRepo.findById(objectId).orElseThrow();
        List<Review> reviews = reviewRepo.findByRestaurantID(objectId);

        List<ReviewResponse> enrichedReviews = reviews.stream().map(review -> {
            String customerName = userRepository.findById(review.getCustomerID().toHexString())
                    .map(User::getName)
                    .orElse("Unknown");
            return new ReviewResponse(review, customerName);
        }).toList();

        ZonedDateTime now = ZonedDateTime.now(ZONE_SJ);
        ZonedDateTime startOfDay = now.toLocalDate().atStartOfDay(ZONE_SJ);
        ZonedDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

        long bookedToday = bookingRepo.countConfirmedTodayByRestaurant(List.of(objectId),
                        Date.from(startOfDay.toInstant()), Date.from(endOfDay.toInstant()))
                .stream().findFirst().map(BookingCount::getCount).orElse(0L);

        long totalReviews = reviewRepo.countReviewsByRestaurant(List.of(objectId))
                .stream().findFirst().map(ReviewCount::getCount).orElse(0L);

        String formattedAddress = String.format("%s, %s, %s %s",
                r.getAddress().getStreet(),
                r.getAddress().getCity(),
                r.getAddress().getState(),
                r.getAddress().getZipCode()
        ).replace(" ", "+");

        String mapsUrl = "https://www.google.com/maps?q=" + formattedAddress + "&z=15&output=embed";

        return new RestaurantDetailsResponse(
                r.getId().toHexString(),
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
                mapsUrl,
                r.getPhotos(),
                enrichedReviews
        );
    }
}
