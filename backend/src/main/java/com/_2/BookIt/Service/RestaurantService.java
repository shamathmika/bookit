package com._2.BookIt.Service;

import com._2.BookIt.Dto.AvailableRestaurantResponse;
import com._2.BookIt.Dto.BookingCount;
import com._2.BookIt.Dto.CategoriesResponse;
import com._2.BookIt.Dto.ReviewCount;
import com._2.BookIt.Enum.RestaurantStatus;
import com._2.BookIt.Model.Booking;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Model.Table;
import com._2.BookIt.Repository.BookingRepository;
import com._2.BookIt.Repository.RestaurantRepository;
import com._2.BookIt.Repository.ReviewRepository;
import com._2.BookIt.Repository.TableRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.bson.types.ObjectId;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
	
	public List<AvailableRestaurantResponse> getAvailableTables (String location) {
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
			if (b.getDateTime().toInstant().equals(now.atZone(ZoneId.systemDefault()).toInstant())) {
				bookedNow.computeIfAbsent(rid, k -> new HashSet<>()).add(b.getTableID());
			} else if (b.getDateTime().toInstant().equals(nowPlus30.atZone(ZoneId.systemDefault()).toInstant())) {
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
				if (hasNow) times.add(formatTime(now));
				if (has30) times.add(formatTime(nowPlus30));
				
				result.add(new AvailableRestaurantResponse(
						restId.toHexString(),
						r.getName(),
						r.getCuisine(),
						r.getCostRating(),
						r.getAvgStarRating(),
						r.getPhotos(),
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
	
	private String formatTime (LocalDateTime time) {
		return time.atZone(ZoneId.systemDefault())
				.toLocalTime()
				.truncatedTo(ChronoUnit.MINUTES)
				.toString();
	}
	
	private Date toDate (LocalDateTime ldt) {
		return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
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
		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
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
		
		Restaurant base = allRestaurants.get(0); // Use one restaurant as base location
		
		double longitude = base.getAddress().getLocation().getCoordinates()[0];
		double latitude = base.getAddress().getLocation().getCoordinates()[1];
		
		return restaurantRepo.findNearbyApprovedActiveRestaurants(longitude, latitude, 3218.69) // 2 miles
				.stream()
				.limit(5)
				.toList();
	}
	
	public List<String> getAvailableTimeSlots (ObjectId restaurantId, LocalDate date, int people) {
		Restaurant restaurant = restaurantRepo.findById(restaurantId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
		
		boolean isToday = date.equals(LocalDate.now());
		
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("h:mm a", Locale.US);

// Normalize stored time strings
		String normalizedOpen = restaurant.getOpeningTime().replaceAll("\\s+", " ").trim();
		String normalizedClose = restaurant.getClosingTime().replaceAll("\\s+", " ").trim();
		
		LocalTime open = LocalTime.parse(normalizedOpen, fmt);
		LocalTime close = LocalTime.parse(normalizedClose, fmt);
		
		LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.MINUTES);
		LocalDateTime nowDateTime = LocalDateTime.of(date, now);
		LocalDateTime openDateTime = LocalDateTime.of(date, open);
		LocalDateTime closeDateTime = LocalDateTime.of(date, close);
		
		// Check if the current time is outside the booking window
		if (date.equals(LocalDate.now()) && (nowDateTime.isBefore(openDateTime) || nowDateTime.isAfter(closeDateTime.minusMinutes(30)))) {
			return List.of(); // Too early or too late to book
		}
		
		LocalTime start = date.equals(LocalDate.now()) ? now.plusMinutes(1) : open;
		
		List<LocalTime> slots = new ArrayList<>();
		for (LocalTime time = start; !time.isAfter(close.minusMinutes(30)); time = time.plusMinutes(30)) {
			slots.add(time);
		}
		
		// Find tables by capacity
		List<Table> tables = tableRepo.findByRestaurantIDAndCapacityGreaterThanEqual(restaurantId, people);
		if (tables.isEmpty()) return List.of();
		
		List<ObjectId> tableIds = tables.stream().map(t -> new ObjectId(t.getId())).toList();
		
		LocalDateTime startDateTime;
		LocalDateTime endDateTime;
		
		if (isToday) {
			startDateTime = LocalDateTime.of(date, now.plusMinutes(1));
		} else {
			startDateTime = open.atDate(date);
		}
		endDateTime = close.atDate(date);
		
		Date startDate = Date.from(startDateTime.atZone(ZoneId.systemDefault()).toInstant());
		Date endDate = Date.from(endDateTime.atZone(ZoneId.systemDefault()).toInstant());
		
		List<Booking> bookings = bookingRepo.findByTableIDInAndDateTimeBetweenAndStatusIn(tableIds, startDate, endDate, List.of("confirmed", "pending"));
		
		// Map of slot -> tableIds already booked at that time
		Map<LocalTime, Set<ObjectId>> bookedMap = new HashMap<>();
		for (Booking b : bookings) {
			LocalTime slotTime = b.getDateTime().toInstant()
					.atZone(ZoneId.systemDefault())
					.toLocalTime()
					.truncatedTo(ChronoUnit.MINUTES);
			bookedMap.computeIfAbsent(slotTime, k -> new HashSet<>()).add(b.getTableID());
		}
		
		// Check which slots have any free table
		List<String> availableTimes = new ArrayList<>();
		for (LocalTime slot : slots) {
			Set<ObjectId> booked = bookedMap.getOrDefault(slot, Set.of());
			boolean anyAvailable = tables.stream().anyMatch(t -> !booked.contains(t.getId()));
			if (anyAvailable) {
				availableTimes.add(slot.toString());
			}
		}
		
		return availableTimes;
	}
}
