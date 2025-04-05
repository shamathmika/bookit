package com._2.BookIt.Security.Jwt;

// Project packages

import com._2.BookIt.Security.Services.UserDetailsImpl;

// JWT packages
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

// Logger packages
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Spring packages
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

// Java packages
import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Utility class for managing JSON Web Tokens (JWT).
 */
@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	
	@Value ("${jwt.secret-key}")
	private String jwtSecretKey;
	
	@Value ("${jwt.expiration}")
	private int jwtExpirationMs;
	
	/**
	 * Generate a JWT token based on the provided authentication.
	 *
	 * @param authentication The authentication object containing user details.
	 * @return The generated JWT token as a string.
	 */
	public String generateJwtToken (Authentication authentication) {
		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		
		return Jwts.builder()
				.subject((userPrincipal.getUsername()))     // Returns the email
				.issuedAt(new Date())
				.expiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(key(), SignatureAlgorithm.HS512)
				.compact();
	}
	
	/**
	 * Create a signing key from the JWT secret.
	 *
	 * @return The signing key as a Key object.
	 */
	private SecretKey key () {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
	}
	
	/**
	 * Extract the email from the given JWT token.
	 *
	 * @param token The JWT token.
	 * @return The email extracted from the token.
	 */
	public String getEmailFromJwtToken (String token) {
		return Jwts.parser()
				.verifyWith(key())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				.getSubject();
	}
	
	/**
	 * Validate the given JWT token.
	 *
	 * @param authToken The JWT token to validate.
	 * @return True if the token is valid, false otherwise.
	 */
	public boolean validateJwtToken (String authToken) {
		try {
			Jwts.parser()
					.verifyWith(key())
					.build()
					.parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}
		
		return false;
	}
}
