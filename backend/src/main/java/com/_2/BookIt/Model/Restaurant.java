package com._2.BookIt.Model;

// Project packages

import com._2.BookIt.Enum.RestaurantStatus;

// JSON packages
import com.fasterxml.jackson.annotation.JsonFormat;

// Validation packages
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

// Lombok packages
import lombok.*;

// Spring packages
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Java packages
import java.time.LocalTime;
import java.util.List;

/**
 * Represents a restaurant.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor (access = AccessLevel.PRIVATE)
@Builder
@Document (collection = "restaurant")
public class Restaurant {
	@Id
	@Setter (AccessLevel.NONE)
	private String id;
	
	@NotBlank (message = "Name is required")
	private String name;
	
	private String description;
	
	private List<@Valid PhotoUrl> photos; // If photos list is present, the String must not be ""
	
	@NotNull (message = "Address is required")
	@Valid
	private Address address;
	
	@NotBlank (message = "Contact number is required")
	@Pattern (regexp = "^[0-9]{10}$", message = "Phone number must be a 10-digit US number")
	private String contact;
	
	@NotBlank (message = "Cuisine type is required")
	private String cuisine;
	
	@Min (value = 0, message = "Cost rating must be greater than 0")
	@Max (value = 3, message = "Cost rating must be less than 3")
	private Integer costRating;
	
	@DecimalMin (value = "0.0", inclusive = true, message = "Average star rating must be greater than 0.0")
	@DecimalMax (value = "5.0", inclusive = true, message = "Average star rating must be less than 5.0")
	private Double avgStarRating;
	
	@NotBlank (message = "Status is required")
	private RestaurantStatus status;
	
	@NotBlank (message = "Opening time is required")
	@JsonFormat (pattern = "hh:mm a")
	private LocalTime openingTime;
	
	@NotBlank (message = "Closing time is required")
	@JsonFormat (pattern = "hh:mm a")
	private LocalTime closingTime;
	
	/**
	 * Nested class for Photo URL.
	 */
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class PhotoUrl {
		@NotBlank (message = "Photo URL cannot be blank")
		@Pattern (regexp = "https?://.+", message = "Photo must be a valid HTTP/HTTPS URL")
		private String url;
	}
	
	/**
	 * Nested class for Address.
	 */
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Address {
		@NotBlank (message = "Street is required")
		private String street;
		
		@NotBlank (message = "City is required")
		private String city;
		
		@NotBlank (message = "State is required")
		private String state;
		
		@NotBlank (message = "Zip code is required")
		private String zipCode;
		
		@NotNull (message = "Geo location is required")
		@Valid
		private GeoLocation location;
	}
	
	/**
	 * Nest class for GeoJSON format.
	 * This is required to map to the location JSON in MongoDB. The location field in MongoDB helps us query for things
	 * like 2 miles near location.
	 */
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class GeoLocation {
		@Pattern (regexp = "Point", message = "Location type must be 'Point'")
		private String type = "Point";
		
		@Size (min = 2, max = 2, message = "Coordinates must contain [longitude, latitude]")
		private double[] coordinates;
	}
	
	// Constructor
	public Restaurant (String name, String description, List<PhotoUrl> photos, Address address, String contact, String cuisine, Integer costRating, Double avgStarRating, RestaurantStatus status, LocalTime openingTime, LocalTime closingTime) {
		this.name = name;
		this.description = description;
		this.photos = photos;
		this.address = address;
		this.contact = contact;
		this.cuisine = cuisine;
		this.costRating = costRating;
		this.avgStarRating = avgStarRating;
		this.status = status;
		this.openingTime = openingTime;
		this.closingTime = closingTime;
	}
}
