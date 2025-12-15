package com.pfm.backend.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/*
 * 
 * 1.Runs once per request
 * 2.Extracts JWT
 * 3.Sets authenticated user in context
 * 
 */

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		if(authHeader!=null && authHeader.startsWith("Bearer")) {
			String token = authHeader.substring(7);
			if(jwtUtil.isTokenValid(token)) {
				String email = jwtUtil.extractSubject(token);
				UsernamePasswordAuthenticationToken authenticationToken = 
						new UsernamePasswordAuthenticationToken(email, null,Collections.EMPTY_LIST);
				authenticationToken.setDetails(
	                        new WebAuthenticationDetailsSource().buildDetails(request)
	                );
				SecurityContextHolder.getContext()
                .setAuthentication(authenticationToken);
			}
		}
		 filterChain.doFilter(request, response);
	}

}
