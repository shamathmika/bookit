package com._2.BookIt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource ("classpath:application.properties")
public class BookItApplication {
	
	public static void main (String[] args) {
		SpringApplication.run(BookItApplication.class, args);
	}
	
}
