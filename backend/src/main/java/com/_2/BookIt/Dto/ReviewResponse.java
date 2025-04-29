package com._2.BookIt.Dto;

import com._2.BookIt.Model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class ReviewResponse {
    private String id;
    private String restaurantID;
    private String customerID;
    private String customerName;
    private Object bookingID;
    private int rating;
    private String comments;
    private List<String> photos;
    private Date date;

    public ReviewResponse(Review review, String customerName) {
        this.id = review.getId().toHexString();
        this.restaurantID = review.getRestaurantID().toHexString();
        this.customerID = review.getCustomerID().toHexString();
        this.customerName = customerName;
        this.bookingID = review.getBookingID().toHexString(); // or use Object if you're returning full booking data
        this.rating = review.getRating();
        this.comments = review.getComments();
        this.photos = review.getPhotos();
        this.date = review.getDate();
    }

}
