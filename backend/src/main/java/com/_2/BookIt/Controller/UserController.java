package com._2.BookIt.Controller;
import com._2.BookIt.Dto.AvailableRestaurantResponse;
import com._2.BookIt.Model.Booking;
import com._2.BookIt.Model.Review;
import com._2.BookIt.Model.User;
import com._2.BookIt.Service.BookingService;
import com._2.BookIt.Service.RestaurantService;

// Spring packages
import com._2.BookIt.Service.ReviewService;
import com._2.BookIt.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;
    @Autowired private BookingService bookingService;
    @Autowired private ReviewService reviewService;

    @GetMapping("/{id}")
 //   @PreAuthorize("@userAccess.canAccessUser(#id, authentication)")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/{id}")
//    @PreAuthorize("@userAccess.canAccessUser(#id, authentication)")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User updated) {
        return ResponseEntity.ok(userService.updateUser(id, updated));
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize("@userAccess.canAccessUser(#id, authentication)")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/reservations")
//    @PreAuthorize("@userAccess.canAccessUser(#id, authentication)")
    public ResponseEntity<List<Booking>> getReservations(@PathVariable String id) {
        return ResponseEntity.ok(bookingService.getBookingsByUserId(id));
    }

    @GetMapping("/{id}/reviews")
 //   @PreAuthorize("@userAccess.canAccessUser(#id, authentication)")
    public ResponseEntity<List<Review>> getReviews(@PathVariable String id) {
        return ResponseEntity.ok(reviewService.getReviewsByUserId(id));
    }

    @DeleteMapping("/booking/{bookingId}")
//    @PreAuthorize("@bookingService.isOwnerOfBooking(#bookingId, authentication.name)")
    public ResponseEntity<Void> cancelBooking(@PathVariable String bookingId) {
        bookingService.cancelBookingIfFuture(bookingId);
        return ResponseEntity.noContent().build();
    }
}