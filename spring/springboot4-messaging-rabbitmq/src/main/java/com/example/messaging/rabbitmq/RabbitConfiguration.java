package com.example.messaging.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.SmartMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfiguration {

	public static final String QUEUE_NAME = "message-queue";
	public static final String MESSAGE_TOPIC_EXCHANGE = "message-topic-exchange";
	public static final String ROUTING_KEY = "message.#";

	@Bean
	Queue queue() {
		return new Queue(QUEUE_NAME, false);
	}

	@Bean
	TopicExchange exchange() {
		return new TopicExchange(MESSAGE_TOPIC_EXCHANGE);
	}

	@Bean
	Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
	}

	@Bean
	SmartMessageConverter messageConverter() {
		return new JacksonJsonMessageConverter();
	}
}
