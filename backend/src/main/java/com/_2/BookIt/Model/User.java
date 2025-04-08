package com._2.BookIt.Model;

// Project packages

// Validation packages

import com._2.BookIt.Enum.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

// Json packages

// Lombok package
import lombok.*;

// Spring packages
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

// Java packages
import java.util.List;

/**
 * Represents a user entity in the system.
 */
@Document (collection = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor (access = AccessLevel.PRIVATE)
@Builder
public class User {
	@Id
	@Setter (AccessLevel.NONE)
	private String id;
	
	@NotBlank (message = "Name is required")
	private String name;
	
	@NotBlank (message = "Email is required")
	@Email (message = "Invalid email")
	private String email;
	
	@Pattern (regexp = "^[0-9]{10}$", message = "Phone number must be a 10-digit US number")
	private String phoneNumber;
	
	@NotBlank (message = "Password is required")
	private String password;
	
	@NotNull (message = "Role is required")
	private Role role;
	
	// To be set only when role = MANAGER (can be empty list initially for a manager as well)
	private List<ObjectId> restaurantIDs;
	
	// Constructor for customer Signup
	public User (String name, String email, String phoneNumber, String password, Role role) {
		this.name = name;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.password = password;
		this.role = role;
	}
	
}
