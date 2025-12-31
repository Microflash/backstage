package com.example.messaging.rabbitmq;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.rabbitmq.RabbitMQContainer;

@TestConfiguration(proxyBeanMethods = false)
public class LocalConfiguration {

	@Bean
	@ServiceConnection
	public RabbitMQContainer rabbitContainer() {
		return new RabbitMQContainer("rabbitmq:4-management-alpine");
	}
}
