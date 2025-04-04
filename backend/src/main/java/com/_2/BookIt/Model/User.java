package com._2.BookIt.Model;

// Project packages

// Validation packages

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// Json packages

// Lombok package
import lombok.*;

// Spring packages
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Java packages
import java.util.List;

/**
 * Represents a user entity in the system.
 */
@Document (collection = "user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@Getter
	@Setter
	@Id
	private String id;
	
	@Getter
	@Setter
	@NotBlank (message = "Name is required")
	private String name;
	
	@Getter
	@Setter
	@Email (message = "Invalid email")
	private String email;
	
	@Getter
	@Setter
	@Pattern (regexp = "^[0-9]{10}$", message = "Phone number must be a 10-digit US number")
	private String phoneNumber;
	
	@Getter
	@Setter
	@NotBlank (message = "Password is required")
	private String password;
	
	@Getter
	@Setter
	@NotNull (message = "Role is required")
	private Role role;
	
	// To be set only when role = MANAGER (can be empty list initially for a manager as well)
	@Getter
	@Setter
	private List<String> restaurantIDs;
	
	// Constructor for customer Signup
	public User (String name, String email, String phoneNumber, String password, Role role) {
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.role = role;
	}
	
}
