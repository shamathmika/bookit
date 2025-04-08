package com._2.BookIt.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/")
public class Home {
	@GetMapping ("/")
	public String home () {
		return "Welcome to the Spring Boot app!";
	}
}
