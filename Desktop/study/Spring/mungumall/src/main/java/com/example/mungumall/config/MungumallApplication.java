package com.example.mungumall.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class MungumallApplication {

	public static void main(String[] args) {
		SpringApplication.run(MungumallApplication.class, args);
	}

}
