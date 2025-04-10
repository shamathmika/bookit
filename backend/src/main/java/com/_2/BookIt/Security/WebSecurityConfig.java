package com._2.BookIt.Security;

// Project packages

import com._2.BookIt.Security.Jwt.AuthEntryPointJwt;
import com._2.BookIt.Security.Jwt.AuthTokenFilter;
import com._2.BookIt.Security.Services.UserDetailsServiceImpl;

// Spring packages
import com._2.BookIt.Util.URLConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration class to set up Spring Security.
 */
@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {
	@Autowired
	UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	/**
	 * Creates a bean for the authentication JWT token filter.
	 *
	 * @return AuthTokenFilter instance
	 */
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter () {
		return new AuthTokenFilter();
	}
	
	/**
	 * Creates a bean for the DAO authentication provider.
	 *
	 * @return DaoAuthenticationProvider instance
	 */
	@Bean
	public DaoAuthenticationProvider authenticationProvider () {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}
	
	/**
	 * Creates a bean for the authentication manager.
	 *
	 * @param authConfig Authentication configuration
	 * @return AuthenticationManager instance
	 * @throws Exception if there is an error getting the authentication manager
	 */
	@Bean
	public AuthenticationManager authenticationManager (AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	
	/**
	 * Creates a bean for the password encoder.
	 *
	 * @return PasswordEncoder instance
	 */
	@Bean
	public PasswordEncoder passwordEncoder () {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * Configures the security filter chain for HTTP requests.
	 *
	 * @param http HttpSecurity configuration
	 * @return SecurityFilterChain instance
	 * @throws Exception if there is an error configuring the security filter chain
	 */

	@Bean
	public SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
				.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(URLConstants.PUBLIC_URLS).permitAll() // Allow public pages
						.requestMatchers(URLConstants.AUTH_URLS).permitAll() // Allow API authentication routes (/signin and /signup)
						.requestMatchers(URLConstants.TEST_URLS).permitAll() // Allow test-related API routes
						.requestMatchers(URLConstants.SWAGGER_URLS).permitAll() // Allow Swagger API routes. TODO: Allow this only for admin roles
						.requestMatchers(URLConstants.PUBLIC_URLS).permitAll()

						.anyRequest().authenticated()); // Require authentication for all
		
		http.authenticationProvider(authenticationProvider());
		
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
}
