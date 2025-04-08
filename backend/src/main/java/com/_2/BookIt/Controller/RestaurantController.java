package com._2.BookIt.Controller;

//Project packages

import com._2.BookIt.Service.RestaurantService;

// Spring packages
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller class for Restaurants.
 * TODO: Update the functions as required
 */
@RestController
@RequestMapping ("/api/restaurants")
public class RestaurantController {
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
	
	// ---------------------------------------------------- ADMIN ------------------------------------------------------
	@GetMapping ("/admin")
	@PreAuthorize ("hasRole('ROLE_ADMIN')")
	public String approveRestaurant () {
		return "Admin - restaurant";
	}
	
}
