package com._2.BookIt.Model;

// Project packages

import com._2.BookIt.Enum.TableStatus;

// Validation packages
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// Lombok packages
import lombok.*;

// BSON packages
import org.bson.types.ObjectId;

// Spring packages
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a table in a restaurant.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor (access = AccessLevel.PRIVATE)
@Builder
@Document (collection = "tableInfo")
public class Table {
	@Id
	@Setter (AccessLevel.NONE)
	private String id;
	
	@NotNull (message = "Restaurant ID is required")
	private ObjectId restaurantID;
	
	@NotNull (message = "Table number is required")
	private Integer tableNumber;
	
	@NotNull (message = "Capacity is required")
	@Min (value = 1, message = "Table capacity must be at least 1")
	private Integer capacity;
	
	private TableStatus status = TableStatus.AVAILABLE;
	
	// Constructor
	public Table (ObjectId restaurantID, Integer tableNumber, Integer capacity) {
		this.restaurantID = restaurantID;
		this.tableNumber = tableNumber;
		this.capacity = capacity;
	}
}
