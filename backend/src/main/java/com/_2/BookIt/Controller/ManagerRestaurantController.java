package com._2.BookIt.Controller;

// Project packages

import com._2.BookIt.Dto.AddRestaurantRequest;
import com._2.BookIt.Model.Restaurant;
import com._2.BookIt.Service.ManagerRestaurantService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// Java packages
import java.io.IOException;
import java.util.List;

/**
 * Controller class for Restaurant managers
 */
@RestController
@RequestMapping ("/api/manager/restaurants")
@PreAuthorize ("hasRole('ROLE_MANAGER')")
public class ManagerRestaurantController {
	
	@Autowired
	private ManagerRestaurantService managerRestaurantService;
	
	@PostMapping (value = "add-restaurant", consumes = { "multipart/form-data" })
	public ResponseEntity<Restaurant> addRestaurant (
			@RequestPart ("request") @Valid AddRestaurantRequest request,
			@RequestPart ("images") List<MultipartFile> images
	) throws IOException {
		Restaurant savedRestaurant = managerRestaurantService.addRestaurant(request, images);
		return ResponseEntity.ok(savedRestaurant);
	}
	
	@GetMapping ("/restaurants-by-manager/{managerId}")
	public ResponseEntity<List<Restaurant>> getRestaurantsByManager (@PathVariable String managerId) {
		List<Restaurant> restaurants = managerRestaurantService.getRestaurantsByManager(managerId);
		return ResponseEntity.ok(restaurants);
	}
}