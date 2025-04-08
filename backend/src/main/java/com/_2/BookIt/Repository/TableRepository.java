package com._2.BookIt.Repository;

// Project packages

import com._2.BookIt.Model.Table;
import com._2.BookIt.Enum.TableStatus;

// BSON packages
import org.bson.types.ObjectId;

// Spring packages
import org.springframework.data.mongodb.repository.MongoRepository;

// Java packages
import java.util.List;
import java.util.Optional;

/**
 * Repository for Tables.
 */
public interface TableRepository extends MongoRepository<Table, String> {
	List<Table> findByRestaurantId (ObjectId restaurantID);
	
	List<Table> findByRestaurantIdAndStatus (ObjectId restaurantID, TableStatus status);
	
	boolean existsByRestaurantIdAndTableNumber (ObjectId restaurantID, Integer tableNumber);
	
	Optional<Table> findByRestaurantIdAndTableNumber (ObjectId restaurantID, Integer tableNumber);
	
	long countByRestaurantId (ObjectId restaurantID);
}
