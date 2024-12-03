package com.example.taskflow_system.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	
	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<ApiResponse<?>> handleTaskNotFoundException(TaskNotFoundException e){
		ApiResponse<String> response = new ApiResponse<String>(e.getMessage(), false);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(WorkflowNotFoundException.class)
	public ResponseEntity<ApiResponse<?>> handleWorkflowNotFoundException(WorkflowNotFoundException e){
		ApiResponse<String> response = new ApiResponse<>(e.getMessage(), false);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponse<?>> handleUserNotFoundException(UserNotFoundException e){
		ApiResponse<String> response = new ApiResponse<String>(e.getMessage(), false);
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<?>> handleValidateException(MethodArgumentNotValidException e){
		StringBuilder errorMessage = new StringBuilder();
		e.getBindingResult().getFieldErrors().forEach(error -> 
		errorMessage
		.append(error.getField())
		.append(": ")
		.append(error.getDefaultMessage())
		.append("; "));
		return null;
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleGlobalException(Exception e){
		ApiResponse<String> response = new ApiResponse<>(e.getMessage(), false);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
}
