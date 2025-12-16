package com.pfm.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfm.backend.dto.CategoryRequestDto;
import com.pfm.backend.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService service;
	
	@PostMapping
	public ResponseEntity<?>createCategory(@RequestBody CategoryRequestDto aRequestDto,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return service.createCategory(aRequestDto, email);
	}
	
	@GetMapping
	public ResponseEntity<?>getCategory(Authentication aAuthentication){
		String email = aAuthentication.getName();
		return service.getCategories(email);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?>updateCategory(@PathVariable Long id,@RequestBody CategoryRequestDto aRequestDto,Authentication aAuthentication){
		String email = aAuthentication.getName();
		return service.updateCategory(id,aRequestDto,email);
	}
}
