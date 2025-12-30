package com.example.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OpenAPIProperties.class)
public class OpenAPIConfiguration {

	@Bean
	public OpenAPI openAPI(OpenAPIProperties openAPIProperties) {
		return openAPIProperties;
	}
}
