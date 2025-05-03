package com._2.BookIt.Dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateRestaurantRequest {
	private String restaurantId;
	private String name;
	private String description;
	private String phoneNumber;
	private String cuisine;
	private int costRating;
	private String openingTime;
	private String closingTime;
	private AddRestaurantRequest.AddressDTO address;
	
	private List<String> retainedImageUrls;
}
