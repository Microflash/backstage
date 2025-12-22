package com.example.method.logging.annotation;

import org.slf4j.event.Level;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.temporal.ChronoUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogEntryExit {

	Level value() default Level.INFO;

	ChronoUnit unit() default ChronoUnit.SECONDS;

	boolean showArgs() default false;

	boolean showResult() default false;

	boolean showExecutionTime() default true;
}
