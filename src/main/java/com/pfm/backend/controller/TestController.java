package com.pfm.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
	@GetMapping("/api/test/protected")
	public String protectedAPI() {
		return "You accessed a protected API";
	}
}
