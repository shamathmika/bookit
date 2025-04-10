package com._2.BookIt.Util;

/**
 * Utility class to hold URL constants for security configuration.
 */
public class URLConstants {
	public static final String[] PUBLIC_URLS = {
			/* Home URls */
			"/",                        // bookit.com/
			"/favicon.ico",             // browser automatically calls GET /favicon.ico on first load
			"/home",                    // if defined, landing page; essentially same as /
			"/debug/**",
			/* Search Results */
			"/searchResults",           //  search results page
			
			/* Restaurant related URLs */
			"/api/restaurant/**",           // restaurant view pages
			"/api/restaurants/**",          // restaurants
			
			/* Error */
			"/error"                    // /error is mentioned here to accomodate https://github.com/spring-projects/spring-boot/issues/31091
	};
	public static final String[] AUTH_URLS = { "/api/auth/**" }; // For AUTH - signin and signup
	public static final String[] TEST_URLS = { "/api/test/**" }; // For testing
	public static final String[] SWAGGER_URLS = {
			"/swagger-ui/**",           // HTML, CSS, JS and icons
			"/v3/api-docs/**",             // Config and OpenAPI docs
	};
}
