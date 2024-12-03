package com.example.taskflow_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.taskflow_system.response.ApiResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse<?>> handleConstraintViolation(ConstraintViolationException e) {
		String errorMessage = e.getConstraintViolations()
				.stream()
				.map(violation -> String.format("Parameter '%s' %s", 
						violation.getPropertyPath(), 
						violation.getMessage()))
				.findFirst()
				.orElse("Invalid Request");
		
		return ResponseEntity.badRequest().body(new ApiResponse<>(errorMessage, false));
	}
	
}
