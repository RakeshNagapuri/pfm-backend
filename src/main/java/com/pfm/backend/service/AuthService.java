package com.pfm.backend.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pfm.backend.dto.LoginRequestDto;
import com.pfm.backend.dto.RegisterRequestDto;
import com.pfm.backend.model.User;
import com.pfm.backend.repository.UserRepository;
import com.pfm.backend.security.JwtUtil;
import com.pfm.backend.util.ResponseUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public static String EMAIL_ALREADY_EXISTS = "Email already exists";
    public static String USER_REGISTERED_SUCCESSFULLY = "User registered successfully";
    public static String INVALID_EMAIL_PASSWORD = "Invalid email or passrod";
    public static String LOGIN_SUCCESSFUL = "Login Successful";
    
	public ResponseEntity<?> register(RegisterRequestDto aRegisterRequestDto) {
		
		// 1. Check if email already exists
		if(userRepository.findByEmail(aRegisterRequestDto.getEmail()).isPresent()) {
			return ResponseUtil.build(HttpStatus.BAD_REQUEST, EMAIL_ALREADY_EXISTS);
		}
		
		// 2. Create user with hashed password
		User user = User.builder().email(aRegisterRequestDto.getEmail())
					.passwordHash(passwordEncoder.encode(aRegisterRequestDto.getPassword()))
					.fullName(aRegisterRequestDto.getFullname())
					.build();
		
		// 3. Save user
		userRepository.save(user);
		
		// 4. Return Success
		return ResponseUtil.build(HttpStatus.CREATED, USER_REGISTERED_SUCCESSFULLY);
	}

	public ResponseEntity<?> login(LoginRequestDto aLoginRequestDto) {
		
		User user = userRepository.findByEmail(aLoginRequestDto.getEmail()).orElse(null);
		
		if(user==null) {
			return ResponseUtil.build(HttpStatus.UNAUTHORIZED, INVALID_EMAIL_PASSWORD);
		}
		
		if(!passwordEncoder.matches(aLoginRequestDto.getPassword(), user.getPasswordHash())) {
			return ResponseUtil.build(HttpStatus.UNAUTHORIZED, INVALID_EMAIL_PASSWORD);
		}
		
		String token = jwtUtil.generateToken(user.getEmail());
		
		return ResponseUtil.build(HttpStatus.OK, LOGIN_SUCCESSFUL,token);
	}

}
