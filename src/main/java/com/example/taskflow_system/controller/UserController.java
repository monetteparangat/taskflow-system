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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow_system.dto.UserDTO;
import com.example.taskflow_system.response.ApiResponse;
import com.example.taskflow_system.service.UserService;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private UserService userService;

	@GetMapping("/getAll")
	public ResponseEntity<ApiResponse<Page<UserDTO>>> getAllUsers(Pageable pageable) {
		logger.info("Start getAllUsers at Controller");
		ApiResponse<Page<UserDTO>> users = null;
		try {
			users = userService.getAllUsers(pageable);
			logger.info("getAllUsers at Controller: Successfully retrieved users");
			return new ResponseEntity<>(users, HttpStatus.OK);

		} catch (Exception e) {
			logger.warn("getAllUsers at Controller: Failed to retrieved users");
			return new ResponseEntity<>(users, HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<ApiResponse<UserDTO>> getUserById(@PathVariable @NotNull Long id) {
		logger.info("Start getUserById at Controller: Fetching user with ID: {}", id);

		ApiResponse<UserDTO> user = userService.getUserById(id);
		if (user == null) {
			logger.info("End getUserById at Controller: Failed to fetched user with ID: {}", id);
			return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
		}

		logger.info("End getUserById at Controller: Fetched user with ID: {}", id);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}

	@PostMapping("/post")
	public ResponseEntity<ApiResponse<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
		ApiResponse<UserDTO> user = userService.createUser(userDTO);

		if (!user.isSuccess()) {
			return new ResponseEntity<>(user, HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>(user, HttpStatus.CREATED);
	}
	
	
	

}
