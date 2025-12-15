package com.pfm.backend.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
@Component
public class JwtUtil {
	private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000;
	private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	
	public String generateToken(String subject) {
		return Jwts.builder()
				.setSubject(subject)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_MS))
				.signWith(key)
				.compact();
	}
	
	public String extractSubject(String token) {
	    return extractAllClaims(token).getSubject();
	}
	private Claims extractAllClaims(String token) {
	    return Jwts.parserBuilder()
	            .setSigningKey(key)
	            .build()
	            .parseClaimsJws(token)
	            .getBody();
	}
	public boolean isTokenValid(String token) {
	    try {
	        extractAllClaims(token);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
}
