package com.example.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Launcher {

	private static final Logger logger = LoggerFactory.getLogger(Launcher.class);
	private static final GreetingHandler handler = new GreetingHandler();

	void main() {
		logger.info("{}", handler.greet("Veronica"));
	}
}
