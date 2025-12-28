package com.pfm.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pfm.backend.dto.BudgetRequestDto;
import com.pfm.backend.dto.BudgetUpdateRequestDto;
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
	public ResponseEntity<?>getBudget(@RequestParam(required=false,name="month")String aMonth,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return budgetService.getBudgets(email, aMonth);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?>updateBudget(@PathVariable Long id,@RequestBody BudgetUpdateRequestDto aDto,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return budgetService.updateBudget(id,aDto,email);
	}
	
	@GetMapping("/summary")
	public ResponseEntity<?>getBudgetSummary(@RequestParam("month") String aMonth,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return budgetService.getBudgetSummary(email, aMonth);
	}
	
	@GetMapping("/summary/categories")
	public ResponseEntity<?>getCategoryBudgetSummary(@RequestParam("month") String aMonth,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return budgetService.getCategoryBudgetSummary(email, aMonth);
	}
	
	@GetMapping("/summary/over-budget")
	public ResponseEntity<?> getOverBudgetSummary(@RequestParam("month") String aMonth,Authentication aAuthentication) {
	    return budgetService.getOverBudgetSummary(aAuthentication.getName(),aMonth);
	}
}
