package com.example.messaging.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class Receiver {

	@RabbitListener(queues = RabbitConfiguration.QUEUE_NAME)
	public void receive(final Book book) {
		IO.println("Received: " + book);
	}
}
