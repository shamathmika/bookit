package com._2.BookIt.Util;

import io.jsonwebtoken.security.Keys;

import java.util.Base64;
import javax.crypto.SecretKey;

/**
 * Used to generate strong base64 encoded 512-bit secret key. Only used once to add this in .env file
 */
public class JwtSecretKeyGenerator {
	public static void main (String[] args) {
		SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
		String base64Key = Base64.getEncoder().encodeToString(key.getEncoded());
		System.out.println("Generated Secure Key: " + base64Key);
	}
}