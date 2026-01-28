package com.pfm.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pfm.backend.service.AnalyticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsController {
	private final AnalyticsService service;
	
	@GetMapping("/monthly-summary")
	public ResponseEntity<?>getMonthlySummary(@RequestParam String month,Authentication aAuthentication){
		return service.getMonthlySummary(aAuthentication.getName(), month);
	}
}
