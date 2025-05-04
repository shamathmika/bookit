package com._2.BookIt.Repository;

// Project packages

import com._2.BookIt.Model.Table;
import com._2.BookIt.Enum.TableStatus;

// BSON packages
import org.bson.types.ObjectId;

// Spring packages
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// Java packages
import java.util.List;
import java.util.Optional;

/**
 * Repository for Tables.
 */
@Repository
public interface TableRepository extends MongoRepository<Table, String> {
	List<Table> findByRestaurantID (ObjectId restaurantID);
	
	List<Table> findByRestaurantIDAndStatus (ObjectId restaurantID, TableStatus status);
	
	Optional<Table> findByRestaurantIDAndTableNumber (ObjectId restaurantID, Integer tableNumber);
	
	boolean existsByRestaurantIDAndTableNumber (ObjectId restaurantID, Integer tableNumber);
	
	long countByRestaurantID (ObjectId restaurantID);
	
	
	List<Table> findAllByRestaurantIDIn (List<ObjectId> restaurantIds);
	
	List<Table> findByRestaurantIDAndCapacityGreaterThanEqual (ObjectId restaurantID, int capacity);
	
	void deleteByRestaurantID (ObjectId objectId);
}
