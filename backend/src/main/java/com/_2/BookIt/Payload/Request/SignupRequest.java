package com._2.BookIt.Payload.Request;

//Project packages

import com._2.BookIt.Model.Role;

// Validation packages
import jakarta.validation.constraints.NotBlank;

// Lombok packages
import lombok.Getter;
import lombok.Setter;

/**
 * Defines the payload for a Sign Up request. It should contain {name, email, phone number(optional), password and role}
 */
public class SignupRequest {
	@Getter
	@Setter
	@NotBlank
	private String name;
	
	@Getter
	@Setter
	@NotBlank
	private String email;
	
	@Getter
	@Setter
	private String phoneNumber;
	
	@Getter
	@Setter
	@NotBlank
	private String password;
	
	@Getter
	@Setter
	@NotBlank
	private Role role; // Client needs to send this based on which URL the user is trying to sign-up from
}
