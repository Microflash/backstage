package com.example.method.logging;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Launcher {

	void main(String... args) {
		SpringApplication.run(Launcher.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(GreetingService greetingService) {
		return _ -> {
			greetingService.greet("Joe");
			greetingService.greet("Jane");
		};
	}
}
