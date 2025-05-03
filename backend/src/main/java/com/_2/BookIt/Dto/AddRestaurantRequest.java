package com._2.BookIt.Dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class AddRestaurantRequest {
	private String name;
	private String description;
	private List<MultipartFile> photos;
	private AddressDTO address;
	private String phoneNumber;
	private String cuisine;
	private int costRating;
	private String openingTime;
	private String closingTime;
	
	@Data
	public static class AddressDTO {
		private String street;
		private String city;
		private String state;
		private String zipCode;
	}
}
