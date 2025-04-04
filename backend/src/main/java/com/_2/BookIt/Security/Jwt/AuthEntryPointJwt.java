package com._2.BookIt.Security.Jwt;

// Servlet packages

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Logger packages
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Spring packages
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

// Java packages
import java.io.IOException;

/**
 * Custom implementation of AuthenticationEntryPoint to handle unauthorized access.
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
	
	/**
	 * Handle unauthorized access attempts.
	 *
	 * @param request       The HTTP request.
	 * @param response      The HTTP response.
	 * @param authException The exception that was thrown during authentication.
	 * @throws IOException      If an input or output exception occurs.
	 * @throws ServletException If a servlet-related exception occurs.
	 */
	@Override
	public void commence (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
			throws IOException, ServletException {
		logger.error("Unauthorized error: {}", authException.getMessage());
		
		// Return 401 status code - SC_UNAUTHORIZED
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
	}
}
