package com.example.taskflow_system.exception;

public class WorkflowNotFoundException extends RuntimeException {

	public WorkflowNotFoundException(String message) {
		super(message);
	}
}
