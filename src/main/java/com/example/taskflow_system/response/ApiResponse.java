package com.example.taskflow_system.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

	private String message;
	private boolean success;
	private T data;

	public ApiResponse(String message, boolean success) {
		this.message = message;
		this.success = success;
	}

}
