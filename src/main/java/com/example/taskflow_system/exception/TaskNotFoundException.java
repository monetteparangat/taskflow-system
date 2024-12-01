package com.example.taskflow_system.exception;

public class TaskNotFoundException extends RuntimeException {

	public TaskNotFoundException(String message) {
		super(message);
	}
}
