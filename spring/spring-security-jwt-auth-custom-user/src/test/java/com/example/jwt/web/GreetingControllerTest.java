package com.example.jwt.web;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	@DisplayName("Should greet user on public endpoint")
	void shouldGreetUserOnPublicEndpoint() throws Exception {
		this.mvc.perform(get(GreetingController.PUBLIC_ENDPOINT))
				.andExpect(content().string("Hello, World!"));
	}

	@Test
	@DisplayName("Should greet user when authenticated")
	void shouldGreetUserWhenAuthenticated() throws Exception {
		String username = "victoria";
		String password = "secret";
		String userRegistrationRequest = /* language=json */ """
				{
				  "username": "%s",
				  "password": "%s"
				}
				""".formatted(username, password);

		this.mvc.perform(post(UserController.USER_ENDPOINT)
						.contentType(MediaType.APPLICATION_JSON)
						.content(userRegistrationRequest))
				.andExpect(status().isOk())
				.andReturn();

		MvcResult result = this.mvc.perform(post(TokenController.TOKEN_ENDPOINT)
						.with(httpBasic(username, password)))
				.andExpect(status().isOk())
				.andReturn();

		String token = result.getResponse().getContentAsString();

		this.mvc.perform(get(GreetingController.PRIVATE_ENDPOINT)
						.header("Authorization", "Bearer " + token))
				.andExpect(content().string("Hello, victoria!"));
	}

	@Test
	@DisplayName("Should deny access when unauthenticated")
	void shouldDenyAccessWhenUnauthenticated() throws Exception {
		this.mvc.perform(get(TokenController.TOKEN_ENDPOINT))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@DisplayName("Should deny access for invalid token")
	void shouldDenyAccessForInvalidToken() throws Exception {
		String token = "fake.token.attempt";

		this.mvc.perform(get(GreetingController.PRIVATE_ENDPOINT)
						.header("Authorization", "Bearer " + token))
				.andExpect(status().isUnauthorized());
	}
}
