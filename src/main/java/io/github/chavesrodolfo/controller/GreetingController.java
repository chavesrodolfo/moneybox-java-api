package io.github.chavesrodolfo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.chavesrodolfo.model.representations.MessageResponse;

@RestController
@RequestMapping("/api/v1")
public class GreetingController {

	private static final String template = "Hello, %s!";

	@GetMapping("/hello")
	@PreAuthorize("isAuthenticated()")
	public MessageResponse greetingHello(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new MessageResponse("Success", String.format(template, name));
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER')")
	public MessageResponse greetingUser(@RequestParam(value = "name", defaultValue = "User") String name) {
		return new MessageResponse("Success", String.format(template, name));
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public MessageResponse greetingAdmin(@RequestParam(value = "name", defaultValue = "Admin") String name) {
		return new MessageResponse("Success", String.format(template, name));
	}
}