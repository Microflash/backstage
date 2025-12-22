package com.example.aop.aspect;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.example.aop.GreetingHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

class LogEntryExitAspectTest {

	private GreetingHandler greetingHandler;
	private AspectAppender aspectAppender;

	@BeforeEach
	void setUp() {
		greetingHandler = new GreetingHandler();

		aspectAppender = new AspectAppender();
		aspectAppender.setContext(new LoggerContext());
		aspectAppender.start();

		final Logger logger = (Logger) LoggerFactory.getLogger(GreetingHandler.class);
		logger.addAppender(aspectAppender);
	}

	@AfterEach
	void cleanUp() {
		aspectAppender.stop();
	}

	@Test
	@DisplayName("Should fire advice with logs on public method")
	void shouldFireAdviceWithLogsOnPublicMethod() {
		greetingHandler.greet("Veronica");

		assertThat(aspectAppender.events).isNotEmpty()
				.anySatisfy(event -> assertThat(event.getMessage())
						.isEqualTo("Started greet method with args: {name=Veronica}")
				)
				.anySatisfy(event -> assertThat(event.getMessage())
						.startsWith("Finished greet method in ")
						.endsWith("millis with return: Hello, Veronica!")
				);
	}

	@Test
	@DisplayName("Should fire advice with logs on private method")
	void shouldFireAdviceWithLogsOnPrivateMethod() {
		greetingHandler.greet("Veronica");

		assertThat(aspectAppender.events).isNotEmpty()
				.anySatisfy(event -> assertThat(event.getMessage())
						.isEqualTo("Started resolveName method with args: {name=Veronica}")
				)
				.anySatisfy(event -> assertThat(event.getMessage())
						.startsWith("Finished resolveName method in ")
						.endsWith("millis with return: Veronica")
				);
	}

	@Test
	@DisplayName("Should not fire advice without annotation")
	void shouldNotFireAdviceWithoutAnnotation() {
		greetingHandler.resolveNothing("Veronica");

		assertThat(aspectAppender.events).isEmpty();
	}
}
