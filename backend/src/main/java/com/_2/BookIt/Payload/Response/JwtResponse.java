package com._2.BookIt.Payload.Response;

// Project packages

import com._2.BookIt.Model.Role;

// Lombok packages
import lombok.Getter;
import lombok.Setter;

/**
 * Defines the payload for JWT Response upon sign in which will have {token, type, id, name, email, phone and role}
 */
public class JwtResponse {
	
	@Getter
	@Setter
	private String token;
	
	@Getter
	@Setter
	private String type = "Bearer";
	
	@Getter
	@Setter
	private String id;
	
	@Getter
	@Setter
	private String name;
	
	@Getter
	@Setter
	private String email;
	
	@Getter
	@Setter
	private String phoneNumber;
	
	@Getter
	private Role role;
	
	public JwtResponse (String accessToken, String id, String name, String email, String phoneNumber, Role role) {
		this.token = accessToken;
		this.id = id;
		this.name = name;
		this.email = email;
		this.role = role;
	}
}
