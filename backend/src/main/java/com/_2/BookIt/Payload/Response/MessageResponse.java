package com._2.BookIt.Payload.Response;

// Lombok packages

import lombok.Getter;
import lombok.Setter;

/**
 * Defines a message response to be sent in case of error or other scenarios
 */
public class MessageResponse {
	@Getter
	@Setter
	private String message;
	
	public MessageResponse (String message) {
		this.message = message;
	}
}
