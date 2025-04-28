package com._2.BookIt.Controller;

//Project packages

import com._2.BookIt.Dto.AvailableRestaurantResponse;
import com._2.BookIt.Dto.CategoriesResponse;
import com._2.BookIt.Service.RestaurantService;

// Spring packages
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * Controller class for Restaurants.
 * TODO: Update the functions as required
 */
@RestController
@RequestMapping ("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {
	
	@Autowired
	private final RestaurantService RestaurantService;
	
	// ---------------------------------------------- CUSTOMER + PUBLIC ------------------------------------------------
	@GetMapping ("/")
	public String searchRestaurants () {
		return "Customer - restaurant";
	}
	
	// --------------------------------------------------- MANAGER -----------------------------------------------------
	@GetMapping ("/manager")
	@PreAuthorize ("hasRole('ROLE_MANAGER')")
	public String addRestaurant () {
		return "Manager - restaurant";
	}
	
	//Available Tables now
	@GetMapping ("/available-tables")
	public ResponseEntity<List<AvailableRestaurantResponse>> getAvailableTables (
			@RequestParam (defaultValue = "San Jose") String location
	) {
		List<AvailableRestaurantResponse> response = RestaurantService.getAvailableTables(location);
		return ResponseEntity.ok(response);
	}
	
	// Categories - Top Rated, Top Booked Today, Near You
	@GetMapping ("/categories")
	public ResponseEntity<CategoriesResponse> getTopRatedRestaurants (
			@RequestParam (defaultValue = "San Jose") String location
	) {
		CategoriesResponse response = RestaurantService.getCategories(location);
		return ResponseEntity.ok(response);
	}
}
