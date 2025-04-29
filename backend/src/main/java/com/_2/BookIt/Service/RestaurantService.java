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
import org.springframework.stereotype.Service;

import org.bson.types.ObjectId;

import java.time.LocalDate;

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

    private String formatTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault())
                .toLocalTime()
                .truncatedTo(ChronoUnit.MINUTES)
                .toString();
    }

    private Date toDate(LocalDateTime ldt) {
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
}
