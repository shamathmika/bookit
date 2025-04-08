package com._2.BookIt.Security.Jwt;

// Project packages

import com._2.BookIt.Security.Services.UserDetailsServiceImpl;

// Servlet packages
import com._2.BookIt.Util.URLConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Logger packages
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Spring packages
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

// Java packages
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * Filter to validate the JWT token and set user authentication in the security context.
 */
public class AuthTokenFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
	
	/**
	 * Filter method to process JWT token and set authentication.
	 *
	 * @param request     HTTP Request
	 * @param response    HTTP response
	 * @param filterChain Filter chain for further processing
	 * @throws ServletException For servlet related exceptions
	 * @throws IOException      For I/O related exceptions
	 */
	@Override
	protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// Skip authorization checks for public URLs
		System.out.println("Request URI: " + request.getRequestURI());
		AntPathMatcher pathMatcher = new AntPathMatcher();
		String uri = request.getRequestURI();
		
		Stream<String> allowedPaths = Stream.concat(
				Arrays.stream(URLConstants.PUBLIC_URLS),
				Arrays.stream(URLConstants.AUTH_URLS)
		);
		
		boolean isAllowed = allowedPaths.anyMatch(pattern ->
				pathMatcher.match(pattern, uri) || uri.equals(pattern)
		);
		
		if (isAllowed) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
			String jwt = parseJwt(request);
			if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
				String email = jwtUtils.getEmailFromJwtToken(jwt);
				
				UserDetails userDetails = userDetailsService.loadUserByUsername(email);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception e) {
			logger.error("Cannot set user authentication: {}", e.getMessage());
		}
		
		filterChain.doFilter(request, response);
	}
	
	/**
	 * Parse the JWT token from the authorization header in the HTTP request.
	 *
	 * @param request HTTP request
	 * @return JWT token if found, else null
	 */
	private String parseJwt (HttpServletRequest request) {
		String headerAuth = request.getHeader("Authorization");
		
		if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
			return headerAuth.substring(7);
		}
		return null;
	}
}
