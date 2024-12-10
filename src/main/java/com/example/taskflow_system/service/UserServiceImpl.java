package com.example.taskflow_system.service;


import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.taskflow_system.dto.UserDTO;
import com.example.taskflow_system.model.User;
import com.example.taskflow_system.repository.UserRepository;
import com.example.taskflow_system.response.ApiResponse;
import com.example.taskflow_system.response.Constant;

@Service
public class UserServiceImpl implements UserService{
	
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper mapper;


	public ApiResponse<Page<UserDTO>> getAllUsers(Pageable pageable){
		logger.info("Start getAllUsers at Service");
		try {
			Page<User> users = userRepository.findAll(pageable);
			Page<UserDTO> usersDTO = users.map(user -> mapper.map(user, UserDTO.class));
			logger.info("End getAllUsers at Service: Successfully retrieved users");
			return new ApiResponse<>(Constant.USER_RETRIEVED_SUCCESS, true, usersDTO);
		}catch(Exception e) {
			logger.warn("End getAllUsers at Service: Failed to retrieved users");
			return new ApiResponse<>(Constant.USER_RETRIEVED_FAILED, false, null);
		}
	}
	
	
}