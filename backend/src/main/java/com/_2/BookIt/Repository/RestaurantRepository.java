package com._2.BookIt.Repository;

// Project packages

import com._2.BookIt.Enum.ApprovalStatus;
import com._2.BookIt.Enum.RestaurantStatus;
import com._2.BookIt.Model.Restaurant;

// Spring packages
import org.bson.types.ObjectId;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

// Java packages
import java.util.List;

/**
 * Repository interface for Restaurants.
 */
public interface RestaurantRepository extends MongoRepository<Restaurant, ObjectId> {
	List<Restaurant> findByNameContainingIgnoreCase (String name);
	
	List<Restaurant> findByStatus (RestaurantStatus status);
	
	List<Restaurant> findByAddress_CityIgnoreCase (String city);
	
	List<Restaurant> findByAddress_CityIgnoreCaseAndStatus (String city, RestaurantStatus status);
	
	List<Restaurant> findByAddress_LocationNear (Point location, Distance distance);
	
	long countByStatus (String status);
	
	List<Restaurant> findByAddress_CityAndStatus (String city, String status);
	
	List<Restaurant> findByApprovalStatus (ApprovalStatus status);
	
	long countByApprovalStatus (ApprovalStatus status);
	
	List<Restaurant> findByAddress_CityIgnoreCaseAndStatusOrderByAvgStarRatingDesc (String city, RestaurantStatus status);
	
	@Query ("{'address.location': {$near: {$geometry: { type: 'Point', coordinates: [?0, ?1] },$maxDistance: ?2 } },'status': 'ACTIVE','approvalStatus': 'APPROVED' }")
	List<Restaurant> findNearbyApprovedActiveRestaurants (double longitude, double latitude, double maxDistanceInMeters);
}
