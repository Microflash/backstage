package com.example.method.logging.aspect;

import com.example.method.logging.annotation.LogEntryExit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

@Aspect
@Component
public class LogEntryExitAspect {

	@Around("@annotation(com.example.method.logging.annotation.LogEntryExit)")
	public Object log(ProceedingJoinPoint point) throws Throwable {
		var codeSignature = (CodeSignature) point.getSignature();
		var methodSignature = (MethodSignature) point.getSignature();
		Method method = methodSignature.getMethod();

		var annotation = method.getAnnotation(LogEntryExit.class);
		ChronoUnit unit = annotation.unit();
		boolean showArgs = annotation.showArgs();
		boolean showResult = annotation.showResult();
		boolean showExecutionTime = annotation.showExecutionTime();
		String methodName = method.getName();
		Object[] methodArgs = point.getArgs();
		String[] methodParams = codeSignature.getParameterNames();
		Level level = annotation.value();
		Logger logger = LoggerFactory.getLogger(method.getDeclaringClass());

		logger.atLevel(level).log(entry(methodName, showArgs, methodParams, methodArgs));

		var start = Instant.now();
		var response = point.proceed();
		var end = Instant.now();
		var duration = String.format("%s %s", unit.between(start, end), unit.name().toLowerCase());

		logger.atLevel(level).log(exit(methodName, duration, response, showResult, showExecutionTime));

		return response;
	}

	static String entry(String methodName, boolean showArgs, String[] params, Object[] args) {
		var message = new StringJoiner(" ").add("Started").add(methodName).add("method");

		if (showArgs && Objects.nonNull(params) && Objects.nonNull(args) && params.length == args.length) {

			Map<String, Object> values = new HashMap<>(params.length);

			for (int i = 0; i < params.length; i++) {
				values.put(params[i], args[i]);
			}

			message.add("with args:").add(values.toString());
		}

		return message.toString();
	}

	static String exit(String methodName, String duration, Object result, boolean showResult, boolean showExecutionTime) {
		var message = new StringJoiner(" ").add("Finished").add(methodName).add("method");

		if (showExecutionTime) {
			message.add("in").add(duration);
		}

		if (showResult) {
			message.add("with return:").add(result.toString());
		}

		return message.toString();
	}
}
