package com._2.BookIt.Service;

import com._2.BookIt.Model.Booking;
import com._2.BookIt.Repository.BookingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // ✅ Create booking
    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    // ✅ Get all bookings (admin/debug)
    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    // ✅ Get bookings by customer
    public List<Booking> getBookingsByUser(ObjectId userId) {
        return bookingRepository.findByUserID(userId);
    }

    // ✅ Get bookings by restaurant (manager)
    public List<Booking> getBookingsByRestaurant(ObjectId restaurantId) {
        return bookingRepository.findByRestaurantID(restaurantId);
    }

    // ✅ Get booking by ID (internal use)
    public Booking getBookingById(ObjectId id) {
        return bookingRepository.findById(id).orElse(null);
    }

    // ✅ Update booking by user with ownership check
    public Booking updateBookingByUser(ObjectId userId, ObjectId bookingId, Booking updatedBooking) {
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
    public void deleteBooking(ObjectId id) {
        bookingRepository.deleteById(id);
    }

    public long getTotalBookingCount() {
        return bookingRepository.count();
    }

}
