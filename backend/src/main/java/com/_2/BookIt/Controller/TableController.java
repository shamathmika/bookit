package com._2.BookIt.Controller;

// Project packages

// Spring packages

import com._2.BookIt.Dto.AddTablesRequest;
import com._2.BookIt.Model.Table;
import com._2.BookIt.Service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for Restaurants.
 * TODO: Update the functions as required
 */
@RestController
@RequestMapping ("/api/manager/tables")
@PreAuthorize ("hasRole('ROLE_MANAGER')")
public class TableController {
	
	@Autowired
	private TableService tableService;
	
	@GetMapping ("/{restaurantId}")
	public ResponseEntity<List<Table>> getTablesByRestaurant (@PathVariable String restaurantId) {
		return ResponseEntity.ok(tableService.getTablesByRestaurantId(restaurantId));
	}
	
	@PostMapping ("/{restaurantId}/add-tables")
	public ResponseEntity<?> addTables (
			@PathVariable String restaurantId,
			@RequestBody AddTablesRequest request) {
		tableService.addTables(restaurantId, request);
		return ResponseEntity.ok("Tables added successfully");
	}
	
	@DeleteMapping ("/{restaurantId}/delete-many")
	public ResponseEntity<Void> deleteTables (
			@PathVariable String restaurantId,
			@RequestBody List<String> tableIds) {
		tableService.deleteMultipleTablesByRestaurantId(restaurantId, tableIds);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping ("/{restaurantId}/delete-all")
	public ResponseEntity<Void> deleteAllTablesForRestaurant (@PathVariable String restaurantId) {
		tableService.deleteTablesByRestaurantId(restaurantId);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping ("/{tableId}/update")
	public ResponseEntity<Table> updateTableCapacity (
			@PathVariable String tableId,
			@RequestParam int seats) {
		return ResponseEntity.ok(tableService.updateTableCapacity(tableId, seats));
	}
}
