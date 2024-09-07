package com.LetUsCodeTogether.ats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.io.IOException;


//@SpringBootApplication
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class AtsApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(AtsApplication.class, args);
	}
}
