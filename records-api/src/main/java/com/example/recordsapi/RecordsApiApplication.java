package com.example.recordsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEurekaClient
@RestController
public class RecordsApiApplication {

	@RequestMapping("/")
	public String home() {
		return "some records";
	}

	public static void main(String[] args) {
		SpringApplication.run(RecordsApiApplication.class, args);
	}
}
