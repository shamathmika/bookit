package com._2.BookIt.Service;

import com._2.BookIt.Enum.ConfirmationType;
import com._2.BookIt.Model.Booking;
import com._2.BookIt.Model.Table;
import com._2.BookIt.Repository.BookingRepository;
import com._2.BookIt.Repository.TableRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookingService {
	
	@Autowired
	private BookingRepository bookingRepository;
	
	@Autowired
	private TableRepository tableRepository;
	
	public Booking createPendingBooking (ObjectId restaurantId, ObjectId userId, LocalDateTime dateTime, int people) {
		List<Table> tables = tableRepository.findByRestaurantIDAndCapacityGreaterThanEqual(restaurantId, people);
		if (tables.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No suitable tables available");
		}
		
		Date from = Date.from(dateTime.minusMinutes(29).atZone(ZoneId.systemDefault()).toInstant());
		Date to = Date.from(dateTime.plusMinutes(29).atZone(ZoneId.systemDefault()).toInstant());
		
		List<Booking> existing = bookingRepository.findByTableIDInAndDateTimeBetweenAndStatusIn(
				tables.stream().map(t -> new ObjectId(t.getId())).toList(),
				from, to,
				List.of("confirmed", "pending")
		);
		
		Set<ObjectId> bookedTables = existing.stream()
				.map(Booking::getTableID)
				.collect(Collectors.toSet());
		
		for (Table table : tables) {
			if (!bookedTables.contains(new ObjectId(table.getId()))) {
				Booking booking = new Booking();
				booking.setRestaurantID(restaurantId);
				booking.setTableID(new ObjectId(table.getId()));
				booking.setUserID(userId);
				booking.setDateTime(Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant()));
				booking.setTotalCustomers(people);
				booking.setStatus("pending");
				return bookingRepository.save(booking);
			}
		}
		
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No available table found for selected time");
	}
	
	public Booking confirmBooking (ObjectId bookingId, ConfirmationType type) {
		Booking booking = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
		
		if (!"pending".equalsIgnoreCase(booking.getStatus())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending bookings can be confirmed");
		}
		
		booking.setStatus("confirmed");
		booking = bookingRepository.save(booking);
		
		sendConfirmation(booking, type);
		
		return booking;
	}
	
	private void sendConfirmation (Booking booking, ConfirmationType type) {
		switch (type) {
			case EMAIL -> sendEmailConfirmation(booking);
			case SMS -> sendSmsConfirmation(booking);
		}
	}
	
	private void sendEmailConfirmation (Booking booking) {
		// TODO: Integrate real email API
		System.out.println("Sending Email to user: " + booking.getUserID());
	}
	
	private void sendSmsConfirmation (Booking booking) {
		// TODO: Integrate real SMS API
		System.out.println("Sending SMS to user: " + booking.getUserID());
	}
	
	// ✅ Get all bookings (admin/debug)
	public List<Booking> getAllBookings () {
		return bookingRepository.findAll();
	}
	
	// ✅ Get bookings by customer
	public List<Booking> getBookingsByUser (ObjectId userId) {
		return bookingRepository.findByUserID(userId);
	}
	
	// ✅ Get bookings by restaurant (manager)
	public List<Booking> getBookingsByRestaurant (ObjectId restaurantId) {
		return bookingRepository.findByRestaurantID(restaurantId);
	}
	
	// ✅ Get booking by ID (internal use)
	public Booking getBookingById (ObjectId id) {
		return bookingRepository.findById(id).orElse(null);
	}
	
	// ✅ Update booking by user with ownership check
	public Booking updateBookingByUser (ObjectId userId, ObjectId bookingId, Booking updatedBooking) {
		Booking existing = bookingRepository.findById(bookingId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
		
		if (!existing.getUserID().equals(userId)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to update this booking");
		}
		
		// Only update modifiable fields
		existing.setDateTime(updatedBooking.getDateTime());
		existing.setTotalCustomers(updatedBooking.getTotalCustomers());
		existing.setStatus(updatedBooking.getStatus());
		
		return bookingRepository.save(existing);
	}
	
	// ✅ Delete booking
	public void deleteBooking (ObjectId id) {
		bookingRepository.deleteById(id);
	}
	
	public long getTotalBookingCount () {
		return bookingRepository.count();
	}
	
	public List<Booking> getBookingsByUserId (String id) {
		return bookingRepository.findByUserID(new ObjectId(id));
	}
	
	
	public void cancelBookingIfFuture (String bookingId) {
		Booking booking = bookingRepository.findById(new ObjectId(bookingId))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found"));
		
		if (booking.getDateTime().after(new Date())) {
			booking.setStatus("cancelled"); // ✅ Corrected
			bookingRepository.save(booking);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot cancel past bookings");
		}
	}
	
	
	public boolean isOwnerOfBooking (String bookingId, String userId) {
		return bookingRepository.findById(new ObjectId(bookingId))
				.map(b -> b.getUserID().equals(userId))
				.orElse(false);
	}
	
}
