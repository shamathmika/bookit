package com._2.BookIt.Service;

import com._2.BookIt.Model.Booking;
import com._2.BookIt.Repository.BookingRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUser(ObjectId userId) {
        return bookingRepository.findByUserID(userId);
    }

    public List<Booking> getBookingsByRestaurant(ObjectId restaurantId) {
        return bookingRepository.findByRestaurantID(restaurantId);
    }

    public Booking getBookingById(ObjectId id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public void deleteBooking(ObjectId id) {
        bookingRepository.deleteById(id);
    }
}


