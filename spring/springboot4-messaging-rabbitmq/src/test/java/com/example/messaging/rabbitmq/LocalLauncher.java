package com.example.messaging.rabbitmq;

import org.springframework.boot.SpringApplication;

class LocalLauncher {

	static void main(String... args) {
		SpringApplication
				.from(Launcher::main)
				.with(LocalConfiguration.class)
				.run(args);
	}
}
