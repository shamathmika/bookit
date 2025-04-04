package com._2.BookIt.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api/test")
public class TestLoginSignUp {
	@GetMapping ("/")
	public String test () {
		return "Public access";
	}
	
	@GetMapping ("/bookTable")
	@PreAuthorize ("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	public String bookTable () {
		return "Booking Table";
	}
	
	@GetMapping ("/manager")
	@PreAuthorize ("hasRole('ROLE_MANAGER')")
	public String managerPortal () {
		return "Manager Portal";
	}
	
	@GetMapping ("/admin")
	@PreAuthorize ("hasRole('ROLE_ADMIN')")
	public String adminPortal () {
		return "Admin Portal";
	}
	
	
}

