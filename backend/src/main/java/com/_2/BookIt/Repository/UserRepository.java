package com._2.BookIt.Repository;

// Project packages

import com._2.BookIt.Model.User;

// Spring packages
import org.springframework.data.mongodb.repository.MongoRepository; // Inheriting MongoRepository methods like save(User user), findById(String id), deleteById(String id) to perform CRUD operations
import org.springframework.stereotype.Repository;

// Java packages
import java.util.Optional; // Allows the return type to be User or null (to avoid NullPointerException)

/**
 * Repository interface for User entities.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {
	Optional<User> findByEmail (String email);
	
	Boolean existsByEmail (String email);
	
	Optional<User> findByPhoneNumber (String phoneNumber);
	
	Boolean existsByPhoneNumber (String phoneNumber);

	Optional<User> findById(String id);
}
