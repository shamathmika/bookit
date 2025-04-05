package com._2.BookIt.Payload.Request;

// Validation packages

import jakarta.validation.constraints.NotBlank;

// Lombok packages
import lombok.Getter;
import lombok.Setter;

/**
 * Defines the payload for a Sign In request. It should contain {email and password}
 */
public class SigninRequest {
	@Getter
	@Setter
	@NotBlank
	private String email;
	
	@Getter
	@Setter
	@NotBlank
	private String password;
	
}
