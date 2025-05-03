package com._2.BookIt.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler (IOException.class)
	public ResponseEntity<String> handleIOException (IOException ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("An error occurred while uploading images. Please try again.");
	}
	
	@ExceptionHandler (MaxUploadSizeExceededException.class)
	public ResponseEntity<String> handleMaxSizeException (MaxUploadSizeExceededException exc) {
		return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
				.body("File too large! Max allowed is 10MB per file.");
	}
}