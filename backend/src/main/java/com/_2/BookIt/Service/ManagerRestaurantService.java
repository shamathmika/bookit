package com._2.BookIt.Service;

// Project packages

import com._2.BookIt.Dto.UpdateRestaurantRequest;
import com._2.BookIt.Enum.ApprovalStatus;
import com._2.BookIt.Enum.RestaurantStatus;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Repository.RestaurantRepository;
import com._2.BookIt.Dto.AddRestaurantRequest;
import com._2.BookIt.Repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

// Java packages
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service class for restaurant managers
 */
@Service
public class ManagerRestaurantService {
	
	private final RestaurantRepository restaurantRepository;
	private final UserRepository userRepository;
	private final S3Service s3Service;
	
	// TODO: Get the right location based on address - or remove this from db
	// Default location for the restaurant if no location is provided in the request - San Jose.
	Restaurant.GeoLocation location = new Restaurant.GeoLocation(
			"Point",
			new double[]{ -121.8863, 37.3382 }
	);
	
	public ManagerRestaurantService (RestaurantRepository restaurantRepository, UserRepository userRepository, S3Service s3Service) {
		this.restaurantRepository = restaurantRepository;
		this.userRepository = userRepository;
		this.s3Service = s3Service;
	}
	
	public Restaurant addRestaurant (AddRestaurantRequest request, List<MultipartFile> images) throws IOException {
		
		List<String> uploadedUrls = s3Service.uploadImages("restaurant", images);
		
		
		Restaurant.Address address = new Restaurant.Address(
				request.getAddress().getStreet(),
				request.getAddress().getCity(),
				request.getAddress().getState(),
				request.getAddress().getZipCode(),
				location
		);
		
		Restaurant restaurant = Restaurant.builder()
				.name(request.getName())
				.description(request.getDescription())
				.photos(uploadedUrls)
				.address(address)
				.contact(request.getPhoneNumber())
				.cuisine(request.getCuisine())
				.costRating(request.getCostRating())
				.avgStarRating(0.0)
				.status(RestaurantStatus.ACTIVE)
				.openingTime(request.getOpeningTime())
				.closingTime(request.getClosingTime())
				.approvalStatus(ApprovalStatus.PENDING)
				.build();
		
		restaurant = restaurantRepository.save(restaurant);
		
		ObjectId restaurantId = restaurant.getId();
		userRepository.findById(request.getManagerId()).ifPresent(user -> {
			List<ObjectId> ids = user.getRestaurantIDs();
			if (ids != null) {
				ids.add(restaurantId);
			} else {
				user.setRestaurantIDs(List.of(restaurantId));
			}
			userRepository.save(user);
		});
		
		return restaurant;
	}
	
	public List<Restaurant> getRestaurantsByManager (String managerId) {
		return userRepository.findById(managerId)
				.map(user -> restaurantRepository.findAllById(user.getRestaurantIDs()))
				.orElse(Collections.emptyList());
	}
	
	public void deleteRestaurantById (String restaurantId) {
		Restaurant restaurant = restaurantRepository.findById(new ObjectId(restaurantId))
				.orElseThrow(() -> new RuntimeException("Restaurant not found"));
		restaurant.setStatus(RestaurantStatus.INACTIVE);
		restaurantRepository.save(restaurant);
	}
	
	public void deleteRestaurantsByManagerId (String managerId) {
		List<Restaurant> restaurants = getRestaurantsByManager(managerId);
		for (Restaurant restaurant : restaurants) {
			restaurant.setStatus(RestaurantStatus.INACTIVE);
			restaurantRepository.save(restaurant);
		}
	}
	
	public Restaurant updateRestaurant (UpdateRestaurantRequest request, List<MultipartFile> newImages) {
		Restaurant restaurant = restaurantRepository.findById(new ObjectId(request.getRestaurantId()))
				.orElseThrow(() -> new RuntimeException("Restaurant not found"));
		
		restaurant.setName(request.getName());
		restaurant.setDescription(request.getDescription());
		restaurant.setContact(request.getPhoneNumber());
		restaurant.setCuisine(request.getCuisine());
		restaurant.setCostRating(request.getCostRating());
		restaurant.setOpeningTime(request.getOpeningTime());
		restaurant.setClosingTime(request.getClosingTime());
		restaurant.setAddress(new Restaurant.Address(
				request.getAddress().getStreet(),
				request.getAddress().getCity(),
				request.getAddress().getState(),
				request.getAddress().getZipCode(),
				location
		));
		
		List<String> currentImages = restaurant.getPhotos() != null ? restaurant.getPhotos() : new ArrayList<>();
		List<String> retained = request.getRetainedImageUrls() != null ? request.getRetainedImageUrls() : new ArrayList<>();
		
		for (String imageUrl : currentImages) {
			if (!retained.contains(imageUrl)) {
				s3Service.deleteImage(imageUrl);
			}
		}
		
		List<String> finalImageUrls = new ArrayList<>(retained);
		if (newImages != null) {
			List<String> newUrls = s3Service.uploadImages("restaurant", newImages);
			finalImageUrls.addAll(newUrls);
		}
		
		restaurant.setPhotos(finalImageUrls);
		
		return restaurantRepository.save(restaurant);
	}
}