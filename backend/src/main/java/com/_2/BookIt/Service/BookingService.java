package com._2.BookIt.Service;

import com._2.BookIt.Enum.ConfirmationType;
import com._2.BookIt.Model.Booking;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Model.Table;
import com._2.BookIt.Model.User;
import com._2.BookIt.Repository.BookingRepository;
import com._2.BookIt.Repository.RestaurantRepository;
import com._2.BookIt.Repository.TableRepository;
import com._2.BookIt.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private RestaurantRepository restaurantRepository;
	
	@Autowired
	private UserRepository userRepository;
	
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
		User user = userRepository.findById(booking.getUserID().toHexString())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
		
		Restaurant restaurant = restaurantRepository.findById(booking.getRestaurantID())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
		
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mm A");
		
		String formattedDate = booking.getDateTime().toInstant()
				.atZone(ZoneId.systemDefault()).toLocalDate().format(dateFormatter);
		
		String formattedTime = booking.getDateTime().toInstant()
				.atZone(ZoneId.systemDefault()).toLocalTime().format(timeFormatter);
		
		String htmlBody = """
				<!DOCTYPE html>
				<html lang="en">
				<head>
				  <meta charset="UTF-8">
				  <title>Booking Confirmation</title>
				  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600&display=swap" rel="stylesheet">
				  <style>
				    body {
				      font-family: 'Montserrat', sans-serif;
				      background-color: #f8f8f8;
				      margin: 0;
				      padding: 0;
				    }
				    .container {
				      max-width: 600px;
				      margin: 30px auto;
				      background: #fff;
				      padding: 30px;
				      border-radius: 8px;
				      box-shadow: 0 2px 8px rgba(0,0,0,0.1);
				      color: #333;
				    }
				    .brand {
				      font-size: 24px;
				      color: #A31D1D;
				      text-align: center;
				      font-weight: normal;
				    }
				    .title {
				      font-size: 22px;
				      color: #D84040;
				      font-weight: 600;
				      text-align: center;
				      margin: 20px 0;
				    }
				    .restaurant {
				      font-size: 18px;
				      font-weight: bold;
				      text-align: center;
				      margin-bottom: 10px;
				    }
				    .info-line {
				      text-align: center;
				      margin: 10px 0;
				      font-size: 15px;
				    }
				    .info-line span {
				      margin: 0 8px;
				      font-weight: 500;
				      color: #000;
				    }
				    .address {
				      text-align: center;
				      color: #A31D1D;
				      font-size: 14px;
				      margin-top: 5px;
				    }
				    .details {
				      margin-top: 20px;
				      line-height: 1.6;
				      font-size: 15px;
				      text-align: center;
				    }
				    .footer {
				      text-align: center;
				      font-size: 13px;
				      color: #999;
				      margin-top: 30px;
				    }
				  </style>
				</head>
				<body>
				  <div class="container">
				    <div class="brand">BookIt</div>
				    <div class="title">Booking Confirmed!</div>
				
				    <div class="restaurant">🍽️ %s</div>
				
				    <div class="info-line">
				      <span>👤 %d people</span>|
				      <span>📅 %s</span>|
				      <span>⏰ %s</span>
				    </div>
				    <div class="address">📍 %s</div>
				
				    <div class="details">
				      Thank you for booking with us. Here are your booking details:
				    </div>
				
				    <div class="details">
				      <strong>Name:</strong> %s<br>
				      <strong>Confirmation Number:</strong> %s
				    </div>
				
				    <div class="footer">© 2025 BookIt, Inc.<br>All rights reserved.</div>
				  </div>
				</body>
				</html>
				""".formatted(
				restaurant.getName(),
				booking.getTotalCustomers(),
				formattedDate,
				formattedTime,
				restaurant.getAddress().getFullAddress(),
				user.getName(),
				booking.getId().toHexString()
		);
		
		
		try {
			emailService.sendBookingConfirmation(
					user.getEmail(),
					"🎉 Your Table is Booked at " + restaurant.getName(),
					htmlBody,
					true
			);
		} catch (IOException e) {
			System.err.println("Failed to send confirmation email: " + e.getMessage());
		}
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
