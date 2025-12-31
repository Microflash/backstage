package com.example.messaging.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Launcher {

	static void main(String... args) {
		SpringApplication.run(Launcher.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(RabbitTemplate rabbitTemplate) {
		return _ -> {
			IO.println("Sending message...");
			rabbitTemplate.convertAndSend(
					RabbitConfiguration.MESSAGE_TOPIC_EXCHANGE,
					RabbitConfiguration.ROUTING_KEY,
					new Book("Platform Decay", "Martha Wells")
			);
		};
	}
}
