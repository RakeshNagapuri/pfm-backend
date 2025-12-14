package com.pfm.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pfm.backend.dto.LoginRequestDto;
import com.pfm.backend.dto.RegisterRequestDto;
import com.pfm.backend.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequestDto aRegisterRequestDto){
		return authService.register(aRegisterRequestDto);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?>login(@RequestBody LoginRequestDto aLoginRequestDto){
		return authService.login(aLoginRequestDto);
	}
	@GetMapping("/test")
	public String test() {
	    return "AuthController Loaded";
	}

}
