package com.example.method.logging;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import com.example.method.logging.aspect.LogEntryExitAspect;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class LogEntryExitAspectTest {

	private GreetingService greetingService;
	private AspectAppender aspectAppender;

	@BeforeEach
	void setUp() {
		final var entryExitAspect = new LogEntryExitAspect();
		final var proxyFactory = new AspectJProxyFactory(new GreetingService());
		proxyFactory.addAspect(entryExitAspect);

		final AopProxy aopProxy = new DefaultAopProxyFactory().createAopProxy(proxyFactory);
		greetingService = (GreetingService) aopProxy.getProxy();

		aspectAppender = new AspectAppender();
		aspectAppender.setContext(new LoggerContext());
		aspectAppender.start();

		final Logger logger = (Logger) LoggerFactory.getLogger(GreetingService.class);
		logger.addAppender(aspectAppender);
	}

	@AfterEach
	void cleanUp() {
		aspectAppender.stop();
	}

	@Test
	@DisplayName("Should fire advice with logs")
	void shouldFireAdviceWithLogs() {
		greetingService.greet("Veronica");

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
	@DisplayName("Should not fire advice without annotation")
	void shouldNotFireAdviceWithoutAnnotation() {
		greetingService.resolveName("Veronica");

		assertThat(aspectAppender.events).isEmpty();
	}

	@ParameterizedTest
	@MethodSource
	@DisplayName("Should return greeting")
	void shouldReturnGreeting(String name, String expectedGreeting) {
		assertThat(greetingService.greet(name)).isEqualTo(expectedGreeting);
	}

	private static Stream<Arguments> shouldReturnGreeting() {
		return Stream.of(
				Arguments.of("Veronica", "Hello, Veronica!"),
				Arguments.of("", "Hello, world!")
		);
	}
}
