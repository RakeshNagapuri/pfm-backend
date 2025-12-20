package com.pfm.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pfm.backend.dto.BudgetRequestDto;
import com.pfm.backend.service.BudgetService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {
	private final BudgetService budgetService;
	
	@PostMapping
	public ResponseEntity<?>createBudget(@RequestBody BudgetRequestDto aRequestDto,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return budgetService.createBudget(aRequestDto, email);
	}
	
	@GetMapping
	public ResponseEntity<?>getBudget(@RequestParam(required=false)String aMonth,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return budgetService.getBudgets(email, aMonth);
	}
}
