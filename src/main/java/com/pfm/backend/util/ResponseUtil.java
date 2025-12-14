package com.pfm.backend.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pfm.backend.dto.common.ApiResponse;

public class ResponseUtil {

	private ResponseUtil() {}
	
	public static ResponseEntity<ApiResponse> build(HttpStatus status,String message){
		return build(status, message, null);
	}
	
	public static ResponseEntity<ApiResponse> build(
            HttpStatus status,
            String message,
            Object data
    ) {
        ApiResponse response = new ApiResponse(
                status.value(),
                message,
                data
        );

        return new ResponseEntity<>(response, status);
    }
}
