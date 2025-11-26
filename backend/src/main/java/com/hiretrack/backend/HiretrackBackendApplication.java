package com.hiretrack.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.hiretrack.backend.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class HiretrackBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(HiretrackBackendApplication.class, args);
	}

}
