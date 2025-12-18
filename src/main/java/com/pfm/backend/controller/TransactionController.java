package com.pfm.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfm.backend.dto.TransactionRequestDto;
import com.pfm.backend.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transactions")
public class TransactionController {

	private final TransactionService transactionService;
	
	@PostMapping
	public ResponseEntity<?> createTransaction(@RequestBody TransactionRequestDto aRequestDto,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return transactionService.createTransaction(aRequestDto, email);
	}
	
	@GetMapping
	public ResponseEntity<?>getTransactions(Authentication aAuthentication){
		String email = aAuthentication.getName();
		return transactionService.getTransactions(email);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?>updateTransactions(@PathVariable Long id,@RequestBody TransactionRequestDto aRequestDto,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return transactionService.updateTransaction(id,aRequestDto,email);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?>deleteTransaction(@PathVariable Long id,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return transactionService.deleteTransaction(id,email);
	}
}
