package com._2.BookIt.Controller;

// Project packages

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
@RequestMapping ("/api/tables")
public class TableController {
	// ---------------------------------------------- CUSTOMER + PUBLIC ------------------------------------------------
	@GetMapping ("/")
	@PreAuthorize ("hasRole('ROLE_CUSTOMER')")
	public String bookTable () {
		return "Customer - tables";
	}
	
	// --------------------------------------------------- MANAGER -----------------------------------------------------
	@GetMapping ("/manager")
	@PreAuthorize ("hasRole('ROLE_MANAGER')")
	public String getTablesByRestaurant () {
		return "Manager - tables";
	}
}
