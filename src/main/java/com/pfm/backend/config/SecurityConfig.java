package com.pfm.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.pfm.backend.security.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/*
 * 1. /api/auth/** -> public
 * 2. All others -> JWT required
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)  {

        System.out.println(">>> Custom SecurityConfig LOADED <<<");
        
        http.csrf(csrf->csrf.disable())
        .authorizeHttpRequests(auth->auth
        		.requestMatchers("/api/auth/**").permitAll()
        		.anyRequest().authenticated()
        		)
        .addFilterBefore(jwtAuthenticationFilter, 
        		UsernamePasswordAuthenticationFilter.class
        		)
        .httpBasic(httpBasic->httpBasic.disable())
        .formLogin(form->form.disable());
        

        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
}
