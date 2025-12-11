package com.pfm.backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.pfm.backend.dto.LoginRequestDto;
import com.pfm.backend.dto.RegisterRequestDto;

@Service
public class AuthService {

	public ResponseEntity<?> register(RegisterRequestDto aRegisterRequestDto) {
		return ResponseEntity.ok("Register endpoint hit");
	}

	public ResponseEntity<?> login(LoginRequestDto aLoginRequestDto) {
		return ResponseEntity.ok("Login endpoint hit");
	}

}
