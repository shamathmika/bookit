package com._2.BookIt.Service;

import com._2.BookIt.Dto.AvailableRestaurantResponse;
import com._2.BookIt.Dto.BookingCount;
import com._2.BookIt.Dto.ReviewCount;
import com._2.BookIt.Model.Booking;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Model.Table;
import com._2.BookIt.Repository.*;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<<<<<<< Updated upstream
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
=======
import org.springframework.web.server.ResponseStatusException;

import java.time.*;
>>>>>>> Stashed changes
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RestaurantService {
	private static final ZoneId ZONE_SJ = ZoneId.of("America/Los_Angeles");
	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("h:mm a", Locale.US);

	@Autowired
	private RestaurantRepository restaurantRepo;

	@Autowired
	private ReviewRepository reviewRepo;

	@Autowired
	private TableRepository tableRepo;

	@Autowired
	private BookingRepository bookingRepo;

	public List<AvailableRestaurantResponse> getAvailableTables(String location) {
		ZonedDateTime now = ZonedDateTime.now(ZONE_SJ).truncatedTo(ChronoUnit.MINUTES);
		int minute = now.getMinute();
		int minutesToNextSlot = (minute % 30 == 0) ? 0 : 30 - (minute % 30);
		ZonedDateTime slot0 = now.plusMinutes(minutesToNextSlot);
		ZonedDateTime slot1 = slot0.plusMinutes(30);
		List<String> slotStrings = List.of(
				slot0.toLocalTime().truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("HH:mm")),
				slot1.toLocalTime().truncatedTo(ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("HH:mm"))
		);

		List<Restaurant> restaurants = restaurantRepo.findByAddress_CityAndStatus(location, "ACTIVE");
		List<ObjectId> restaurantIds = restaurants.stream().map(Restaurant::getId).toList();

		List<Table> allTables = tableRepo.findAllByRestaurantIDIn(restaurantIds);
		Map<ObjectId, List<Table>> tablesByRestaurant = allTables.stream()
				.collect(Collectors.groupingBy(Table::getRestaurantID));

		Map<ObjectId, Long> bookedTodayMap = new HashMap<>();
		Map<ObjectId, Long> reviewCountMap = new HashMap<>();
		ZonedDateTime startOfDay = now.toLocalDate().atStartOfDay(ZONE_SJ);
		ZonedDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

		for (BookingCount bc : bookingRepo.countConfirmedTodayByRestaurant(
				restaurantIds,
				Date.from(startOfDay.toInstant()),
				Date.from(endOfDay.toInstant())
		)) {
			bookedTodayMap.put(bc.getId(), bc.getCount());
		}
		for (ReviewCount rc : reviewRepo.countReviewsByRestaurant(restaurantIds)) {
			reviewCountMap.put(rc.getId(), rc.getCount());
		}

		List<AvailableRestaurantResponse> result = new ArrayList<>();
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

			for (String slotStr : slotStrings) {
				LocalTime slotTime = LocalTime.parse(slotStr, DateTimeFormatter.ofPattern("HH:mm"));
				if (slotTime.isBefore(opening) || !slotTime.isBefore(closing)) continue;

				long bookedCount = bookingCountBySlot.getOrDefault(slotStr, 0L);
				if (bookedCount < totalTables) {
					availableAt.add(slotStr);
				}
			}

			if (!availableAt.isEmpty()) {
				result.add(new AvailableRestaurantResponse(
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
				.sorted(Comparator.comparingDouble(AvailableRestaurantResponse::getAvgRating).reversed())
				.limit(10)
				.toList();
	}





<<<<<<< Updated upstream
    private Date toDate(LocalDateTime ldt) {
        return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
    }
}
=======
	private String formatTime(LocalDateTime time) {
		return time.atZone(ZONE_SJ)
				.toLocalTime()
				.truncatedTo(ChronoUnit.MINUTES)
				.toString();
	}

	private Date toDate(LocalDateTime ldt) {
		return Date.from(ldt.atZone(ZONE_SJ).toInstant());
	}

	public CategoriesResponse getCategories (String location) {
		List<Restaurant> topRatedRestaurants = findTopRatedRestaurants(location);
		List<Restaurant> topBookedTodayRestaurants = findTopBookedTodayRestaurants(location);
		List<Restaurant> nearYouRestaurants = findRestaurantsNear(location);

		return new CategoriesResponse(
				topRatedRestaurants,
				topBookedTodayRestaurants,
				nearYouRestaurants
		);
	}

	public List<Restaurant> findTopRatedRestaurants (String location) {
		return restaurantRepo.findByAddress_CityIgnoreCaseAndStatusOrderByAvgStarRatingDesc(location, RestaurantStatus.ACTIVE)
				.stream()
				.limit(5)
				.toList();
	}

	public List<Restaurant> findTopBookedTodayRestaurants (String location) {
		LocalDateTime startOfDay = LocalDate.now(ZONE_SJ).atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1).minusSeconds(1);

		List<ObjectId> restaurantIds = restaurantRepo.findByAddress_CityIgnoreCaseAndStatus(location, RestaurantStatus.ACTIVE)
				.stream()
				.map(Restaurant::getId)
				.toList();

		List<BookingCount> bookingCounts = bookingRepo.countConfirmedTodayByRestaurant(
				restaurantIds,
				toDate(startOfDay),
				toDate(endOfDay)
		);

		List<ObjectId> topRestaurantIds = bookingCounts.stream()
				.sorted(Comparator.comparingLong(BookingCount::getCount).reversed())
				.limit(5)
				.map(BookingCount::getId)
				.toList();

		return restaurantRepo.findAllById(topRestaurantIds);
	}

	public List<Restaurant> findRestaurantsNear (String city) {
		List<Restaurant> allRestaurants = restaurantRepo.findByAddress_CityIgnoreCaseAndStatus(city, RestaurantStatus.ACTIVE);

		if (allRestaurants.isEmpty()) {
			return List.of();
		}

		Restaurant base = allRestaurants.get(0);
		double longitude = base.getAddress().getLocation().getCoordinates()[0];
		double latitude = base.getAddress().getLocation().getCoordinates()[1];

		return restaurantRepo.findNearbyApprovedActiveRestaurants(longitude, latitude, 3218.69)
				.stream()
				.limit(5)
				.toList();
	}


	public List<String> getAvailableTimeSlots(ObjectId restaurantId, LocalDate date, int people) {
		Restaurant restaurant = restaurantRepo.findById(restaurantId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));

		DateTimeFormatter inputFmt = DateTimeFormatter.ofPattern("h:mm a");
		DateTimeFormatter outputFmt = DateTimeFormatter.ofPattern("HH:mm");

		LocalTime open = LocalTime.parse(restaurant.getOpeningTime().trim(), inputFmt);
		LocalTime close = LocalTime.parse(restaurant.getClosingTime().trim(), inputFmt);

		LocalTime now = LocalTime.now(ZONE_SJ);
		boolean isToday = date.equals(LocalDate.now(ZONE_SJ));

		// ⬅️ Only show future time slots
		LocalTime roundedNow = roundUpToNext30Min(now);
		LocalTime start = isToday ? (roundedNow.isAfter(open) ? roundedNow : open) : open;

		List<String> allSlots = new ArrayList<>();
		for (LocalTime slot = start; !slot.isAfter(close.minusMinutes(30)); slot = slot.plusMinutes(30)) {
			allSlots.add(slot.format(outputFmt));
		}

		List<Table> tables = tableRepo.findByRestaurantID(restaurantId);
		int totalTables = tables.size();

		ZonedDateTime startOfDay = date.atStartOfDay(ZONE_SJ);
		ZonedDateTime endOfDay = startOfDay.plusDays(1);

		List<Booking> bookings = bookingRepo.findByRestaurantIDAndDateTimeBetween(
				restaurantId,
				Date.from(startOfDay.toInstant()),
				Date.from(endOfDay.toInstant())
		);

		Map<String, Long> bookingsByTime = bookings.stream()
				.map(booking -> booking.getDateTime().toInstant().atZone(ZONE_SJ).toLocalTime()
						.truncatedTo(ChronoUnit.MINUTES).format(outputFmt))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

		return allSlots.stream()
				.filter(slotStr -> bookingsByTime.getOrDefault(slotStr, 0L) < totalTables)
				.collect(Collectors.toList());
	}

	private LocalTime roundUpToNext30Min(LocalTime time) {
		int minute = time.getMinute();
		return time.withMinute(minute < 30 ? 30 : 0)
				.plusHours(minute >= 30 ? 1 : 0)
				.withSecond(0)
				.withNano(0);
	}




	// ... (rest of the class remains unchanged)
	}

>>>>>>> Stashed changes
