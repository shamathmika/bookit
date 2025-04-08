package com._2.BookIt.Enum;

/**
 * Status of table in real-time
 * - AVAILABLE: Can be booked
 * - OCCUPIED - Already booked
 * - PENDING - temp hold (during booking confirmation flow)
 */
public enum TableStatus {
	AVAILABLE,
	OCCUPIED,
	PENDING
}
