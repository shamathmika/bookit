package com._2.BookIt.Controller;

import com._2.BookIt.Model.Booking;
import com._2.BookIt.Service.BookingService;
import jakarta.validation.Valid;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    // ✅ Create booking
    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
    public ResponseEntity<Booking> createBooking(@Valid @RequestBody Booking booking) {
        return ResponseEntity.ok(bookingService.createBooking(booking));
    }

    // ✅ Get bookings by user
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
    public ResponseEntity<List<Booking>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(bookingService.getBookingsByUser(new ObjectId(userId)));
    }

    // ✅ Get bookings by restaurant
    @GetMapping("/restaurant/{restaurantId}")
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    public ResponseEntity<List<Booking>> getByRestaurant(@PathVariable String restaurantId) {
        return ResponseEntity.ok(bookingService.getBookingsByRestaurant(new ObjectId(restaurantId)));
    }

    // ✅ Update booking (only allowed by the same customer)
    @PutMapping("/user/{userId}/booking/{bookingId}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
    public ResponseEntity<Booking> updateBooking(
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

    // ✅ Delete booking
    @DeleteMapping("/{bookingId}")
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MANAGER')")
    public ResponseEntity<Void> deleteBooking(@PathVariable String bookingId) {
        bookingService.deleteBooking(new ObjectId(bookingId));
        return ResponseEntity.noContent().build();
    }
}
