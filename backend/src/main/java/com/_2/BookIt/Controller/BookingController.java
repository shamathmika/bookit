package com._2.BookIt.Controller;

import com._2.BookIt.Enum.ConfirmationType;
import com._2.BookIt.Model.Booking;
import com._2.BookIt.Service.BookingService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping ("/api/bookings")
public class BookingController {
	
	@Autowired
	private BookingService bookingService;
	
	// ✅ Get bookings by user
	@GetMapping ("/user/{userId}")
	@PreAuthorize ("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
	public ResponseEntity<List<Booking>> getByUser (@PathVariable String userId) {
		return ResponseEntity.ok(bookingService.getBookingsByUser(new ObjectId(userId)));
	}
	
	// ✅ Get bookings by restaurant
	@GetMapping ("/restaurant/{restaurantId}")
	@PreAuthorize ("hasRole('ROLE_MANAGER')")
	public ResponseEntity<List<Booking>> getByRestaurant (@PathVariable String restaurantId) {
		return ResponseEntity.ok(bookingService.getBookingsByRestaurant(new ObjectId(restaurantId)));
	}
	
	// ✅ Update booking (only allowed by the same customer)
	@PutMapping ("/user/{userId}/booking/{bookingId}")
	@PreAuthorize ("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
	public ResponseEntity<Booking> updateBooking (
			@PathVariable String userId,
			@PathVariable String bookingId,
			@Valid @RequestBody Booking updatedBooking) {
		
		return ResponseEntity.ok(
				bookingService.updateBookingByUser(
						new ObjectId(userId),
						new ObjectId(bookingId),
						updatedBooking
				)
		);
	}
	
	// Delete booking - customer deletes booking if they dont take any action in 5 mins.
	// Manager deletes a booking if someone called and cancelled or some other reason.
	@DeleteMapping ("/{bookingId}")
	@PreAuthorize ("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
	public ResponseEntity<Void> deleteBooking (@PathVariable String bookingId) {
		bookingService.deleteBooking(new ObjectId(bookingId));
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping ("/create")
	@PreAuthorize ("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
	public ResponseEntity<Booking> createPendingBooking (
			@RequestParam String restaurantId,
			@RequestParam String userId,
			@RequestParam @DateTimeFormat (iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTime,
			@RequestParam int people
	) {
		return ResponseEntity.ok(
				bookingService.createPendingBooking(new ObjectId(restaurantId), new ObjectId(userId), dateTime, people)
		);
	}
	
	@PutMapping ("/{bookingId}/confirm")
	@PreAuthorize ("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
	public ResponseEntity<Booking> confirmBooking (
			@PathVariable String bookingId,
			@RequestParam ConfirmationType type
	) {
		return ResponseEntity.ok(
				bookingService.confirmBooking(new ObjectId(bookingId), type)
		);
	}
}
