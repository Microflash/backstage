package com.example.aop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class GreetingHandlerTest {

	private GreetingHandler greetingHandler;

	@BeforeEach
	void setUp() {
		greetingHandler = new GreetingHandler();
	}

	@ParameterizedTest
	@MethodSource
	@DisplayName("Should return greeting")
	void shouldReturnGreeting(String name, String expectedGreeting) {
		assertThat(greetingHandler.greet(name)).isEqualTo(expectedGreeting);
	}

	private static Stream<Arguments> shouldReturnGreeting() {
		return Stream.of(
				Arguments.of("Veronica", "Hello, Veronica!"),
				Arguments.of("", "Hello, world!")
		);
	}
}
