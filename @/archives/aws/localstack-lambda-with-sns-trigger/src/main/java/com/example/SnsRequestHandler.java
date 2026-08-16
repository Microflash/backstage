package com.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;

import java.util.List;

public class SnsRequestHandler implements RequestHandler<SNSEvent, List<String>> {

	@Override
	public List<String> handleRequest(SNSEvent event, Context context) {
		final List<String> messages = event.getRecords().stream()
				.map(SNSEvent.SNSRecord::getSNS)
				.map(SNSEvent.SNS::getMessage)
				.toList();
		messages.forEach(System.out::println);
		return messages;
	}
}
