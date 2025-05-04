package com._2.BookIt.Repository;

import com._2.BookIt.Dto.BookingCount;
import com._2.BookIt.Model.Booking;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BookingRepository extends MongoRepository<Booking, ObjectId> {
	List<Booking> findByUserID (ObjectId userID);
	
	List<Booking> findByRestaurantID (ObjectId restaurantID);
	
	List<Booking> findByRestaurantIDInAndDateTimeInAndStatusIn (
			List<ObjectId> restaurantIds, List<Date> times, List<String> status
	);
	
	
	@Aggregation (pipeline = {
			"{ '$match': { 'status': 'confirmed', 'dateTime': { '$gte': ?1, '$lte': ?2 }, 'restaurantID': { '$in': ?0 } } }",
			"{ '$group': { '_id': '$restaurantID', 'count': { '$sum': 1 } } }"
	})
	List<BookingCount> countConfirmedTodayByRestaurant (List<ObjectId> restaurantIds, Date start, Date end);
	
	@Aggregation (pipeline = {
			"{ '$match': { 'dateTime': { '$gte': { $dateSubtract: { startDate: '$$NOW', unit: 'month', amount: 1 } } } } }",
			"{ '$count': 'count' }"
	})
	Long countBookingsLastMonth ();

	@Query("{ 'restaurantID': ?0, 'dateTime': { $gte: ?1, $lt: ?2 } }")
	List<Booking> findBookingsByRestaurantIdAndDate(ObjectId restaurantID, Date start, Date end);

	List<Booking> findByRestaurantIDAndDateTimeBetween(ObjectId restaurantID, Date start, Date end);


	@Query ("{ 'tableID': ?0, 'dateTime': ?1, 'status': { $in: ?2 } }")
	List<Booking> findByTableIDAndDateTimeAndStatusIn (ObjectId tableId, java.util.Date dateTime, List<String> statuses);
	
	List<Booking> findByTableIDInAndDateTimeBetweenAndStatusIn (List<ObjectId> tableIds, Date from, Date to, List<String> statuses);
}
