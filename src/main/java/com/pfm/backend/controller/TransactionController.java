package com.pfm.backend.controller;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public ResponseEntity<?>getTransactions(Authentication aAuthentication,
			@RequestParam(required=false) LocalDate aFromDate,
			@RequestParam(required=false)LocalDate aToDate,
			@RequestParam(required=false)String aType,
			@RequestParam(defaultValue = "0")int aPage,
			@RequestParam(defaultValue = "10")int aSize,
			@RequestParam(defaultValue = "transactionDate")String aSortBy,
			@RequestParam(defaultValue = "desc")String aSortDir){
		String email = aAuthentication.getName();
		return transactionService.getTransactions(email, aFromDate, aToDate, aType, aPage, aSize, aSortBy, aSortDir);
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
