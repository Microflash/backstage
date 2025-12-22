package com.example.method.logging;

import com.example.method.logging.annotation.LogEntryExit;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;

@Service
public class GreetingService {

	@LogEntryExit(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
	public String greet(String name) {
		return "Hello, " + resolveName(name) + "!";
	}

	public String resolveName(String name) {
		return !name.isBlank() ? name : "world";
	}
}
