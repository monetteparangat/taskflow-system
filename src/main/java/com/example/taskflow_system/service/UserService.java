package com.example.taskflow_system.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.taskflow_system.dto.UserDTO;
import com.example.taskflow_system.response.ApiResponse;

public interface UserService {

	ApiResponse<Page<UserDTO>> getAllUsers(Pageable pageable);

	ApiResponse<UserDTO> getUserById(Long id);

	ApiResponse<UserDTO> createUser(UserDTO userDTO);
}
