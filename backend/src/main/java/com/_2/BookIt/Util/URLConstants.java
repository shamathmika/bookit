package com._2.BookIt.Util;

/**
 * Utility class to hold URL constants for security configuration.
 */
public class URLConstants {
	public static final String[] PUBLIC_URLS = {
			"/",                        // bookit.com/
			"/home",                    // if defined, landing page; essentially same as /
			"/searchResults",           //  search results page
			"/restaurant/**",           // restaurant view pages
			"/error"                    // /error is mentioned here to accomodate https://github.com/spring-projects/spring-boot/issues/31091
	};
	public static final String[] AUTH_URLS = { "/api/auth/**" }; // For AUTH - signin and signup
	public static final String[] TEST_URLS = { "/api/test/**" }; // For testing
}
