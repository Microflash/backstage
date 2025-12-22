package com.example.aop;

import com.example.aop.annotation.LogEntryExit;

import java.time.temporal.ChronoUnit;

public class GreetingHandler {

	@LogEntryExit(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
	public String greet(String name) {
		return "Hello, " + resolveName(name) + "!";
	}

	@LogEntryExit(showArgs = true, showResult = true, unit = ChronoUnit.MILLIS)
	private String resolveName(String name) {
		return !name.isBlank() ? name : "world";
	}

	public void resolveNothing(String randomString) {
		IO.println("All good, nothing to resolve!");
	}
}
