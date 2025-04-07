package com._2.BookIt.Controller;


import com._2.BookIt.Model.Role;
import com._2.BookIt.Model.User;
import com._2.BookIt.Payload.Request.SigninRequest;
import com._2.BookIt.Payload.Request.SignupRequest;
import com._2.BookIt.Payload.Response.JwtResponse;
import com._2.BookIt.Payload.Response.MessageResponse;
import com._2.BookIt.Repository.UserRepository;
import com._2.BookIt.Security.Jwt.JwtUtils;
import com._2.BookIt.Security.Services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class for sign in and sign up.
 */
@CrossOrigin (origins = {
		"http://localhost:8080",
		"http://localhost:3000",
		"https://bookit.com",
		"https://restaurant.bookit.com",
		"https://admin.bookit.com"
}, maxAge = 3600)
@RestController
@RequestMapping ("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtUtils jwtUtils;
	
	/**
	 * Authenticate user and return a JWT token if successful.
	 *
	 * @param signinRequest The sign in request containing email and password.
	 * @return A ResponseEntity containing the JWT response or an error message.
	 */
	@PostMapping ("/signin")
	public ResponseEntity<?> authenticateUser (@Valid @RequestBody SigninRequest signinRequest) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		Role role = userDetails.getAuthorities().stream()
				.findFirst() // There is only one role
				.map(item -> Role.valueOf(item.getAuthority())) // Convert String to Enum
				.orElse(null); // Should not reach this case since the role is a required field in the MongoDB schema
		
		return ResponseEntity.ok(new JwtResponse(jwt,
				userDetails.getId(),
				userDetails.getName(),
				userDetails.getEmail(),
				userDetails.getPhoneNumber(),
				role));
	}
	
	/**
	 * Register a new user account.
	 *
	 * @param signupRequest The signup request containing user details.
	 * @return A ResponseEntity indicating success or error message.
	 */
	@PostMapping ("/signup")
	public ResponseEntity<?> registerUser (@Valid @RequestBody SignupRequest signupRequest) {
		// Check if email already in database
		if (userRepository.existsByEmail(signupRequest.getEmail())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Email is already in use!"));
		}
		// Need this null check here, or signup fails for cases where phone number is missing since it matches with documents with no phone number
		if (signupRequest.getPhoneNumber() != null && userRepository.existsByPhoneNumber(signupRequest.getPhoneNumber())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Phone number is already in use!"));
		}
		
		// Create a new user
		User user = new User(signupRequest.getName(),
				signupRequest.getEmail(),
				signupRequest.getPhoneNumber(),
				passwordEncoder.encode(signupRequest.getPassword()),
				signupRequest.getRole());
		
		Role role = signupRequest.getRole(); // This can't be null since this is a required payload item in Signup HTTP Request
		user.setRole(role);
		
		// Save to user collection
		userRepository.save(user);
		
		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
	}
}
