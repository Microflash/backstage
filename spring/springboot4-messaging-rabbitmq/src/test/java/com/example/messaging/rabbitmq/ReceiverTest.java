package com.example.messaging.rabbitmq;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Import(LocalConfiguration.class)
class ReceiverTest {

	private @Autowired RabbitTemplate rabbitTemplate;
	private @MockitoSpyBean Receiver receiver;

	@Test
	@DisplayName("Should receive book message on message-queue")
	void shouldReceiveBookMessageOnMessageQueue() {
		var book = new Book("The Faith of Beasts", "James S A Corey");

		rabbitTemplate.convertAndSend(
				RabbitConfiguration.MESSAGE_TOPIC_EXCHANGE,
				RabbitConfiguration.ROUTING_KEY,
				book
		);

		verify(receiver, timeout(1000)).receive(book);
	}
}
