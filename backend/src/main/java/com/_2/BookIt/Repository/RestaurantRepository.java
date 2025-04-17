package com._2.BookIt.Repository;

// Project packages

import com._2.BookIt.Enum.RestaurantStatus;
import com._2.BookIt.Model.Restaurant;

// Spring packages
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

// Java packages
import java.util.List;

/**
 * Repository interface for Restaurants.
 */
public interface RestaurantRepository extends MongoRepository<Restaurant, String> {
	List<Restaurant> findByNameContainingIgnoreCase (String name);
	
	List<Restaurant> findByStatus (RestaurantStatus status);
	
	List<Restaurant> findByAddress_CityIgnoreCase (String city);
	
	List<Restaurant> findByAddress_CityIgnoreCaseAndStatus (String city, RestaurantStatus status);
	
	List<Restaurant> findByAddress_LocationNear (Point location, Distance distance);

	long countByStatus(String status);

	List<Restaurant> findByAddress_CityAndStatus(String city, String status);
}
