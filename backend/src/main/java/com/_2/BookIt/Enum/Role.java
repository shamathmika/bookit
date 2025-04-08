package com._2.BookIt.Enum;

/**
 * Role of each user
 * - CUSTOMER: Can search, book (and cancel) tables, check reviews and description at restaurants and access location of restaurant
 * - MANAGER: Can do everything a customer can and access manager portal to manage tables in restaurants they run
 * - ADMIN: Can do everything a customer can and access the admin portal to approve/decline restaurants and view statistics
 */
// ROLE_ prefix is required for JWT to map it as a role in hasRole()
public enum Role {
	ROLE_CUSTOMER,
	ROLE_MANAGER,
	ROLE_ADMIN;
}