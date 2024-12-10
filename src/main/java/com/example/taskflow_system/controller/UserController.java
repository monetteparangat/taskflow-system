package com.example.taskflow_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow_system.dto.UserDTO;
import com.example.taskflow_system.response.ApiResponse;
import com.example.taskflow_system.service.UserService;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	@GetMapping("/getAll")
	public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllTasks(Pageable pageable) {
		logger.info("Start getAllTasks at Controller");
		ApiResponse<Page<UserDTO>> tasks = null;
		try {
			tasks = userService.getAllUsers(pageable);
			logger.info("getAlltasks at Controller: Successfully retrieved users");
			return new ResponseEntity<>(tasks, HttpStatus.OK);

		} catch (Exception e) {
			logger.warn("getAllTasks at Controller: Failed to retrieved users");
			return new ResponseEntity<>(tasks, HttpStatus.NOT_FOUND);
		}
	}

}
