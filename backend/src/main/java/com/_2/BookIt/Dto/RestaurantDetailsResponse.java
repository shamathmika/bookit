package com._2.BookIt.Dto;

import com._2.BookIt.Model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDetailsResponse {
    private String restaurantId;      // ← new
    private String name;
    private String description;
    private String contact;
    private String cuisine;
    private int costRating;
    private double avgRating;
    private long totalReviews;
    private long bookedToday;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private double[] coordinates;     // [lng, lat]
    private String googleMapsEmbedUrl;
    private List<String> photos;
    private List<Review> reviews;
}
