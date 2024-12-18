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
public class UserServiceImpl implements UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ModelMapper mapper;

	public ApiResponse<Page<UserDTO>> getAllUsers(Pageable pageable) {
		logger.info("Start getAllUsers at Service");
		try {
			Page<User> users = userRepository.findAll(pageable);
			Page<UserDTO> usersDTO = users.map(user -> mapper.map(user, UserDTO.class));
			logger.info("End getAllUsers at Service: Successfully retrieved users");
			return new ApiResponse<>(Constant.USER_RETRIEVED_SUCCESS, true, usersDTO);
		} catch (Exception e) {
			logger.warn("End getAllUsers at Service: Failed to retrieved users", e);
			return new ApiResponse<>(Constant.USER_RETRIEVED_FAILED, false, null);
		}
	}

	public ApiResponse<UserDTO> getUserById(Long id) {
		logger.info("Star getUserById at Service: Fetching task with ID: {}", id);
		User user = userRepository.findById(id).orElse(null);

		if (user == null) {
			logger.warn("End getUserById at Service: Failed to retrieved user");
			return new ApiResponse<>(String.format(Constant.USER_NOT_FOUND, id), false, null);
		}

		UserDTO userDTO = mapper.map(user, UserDTO.class);
		logger.info("End getUserById at Service: Successfully retrieved user with ID: {}", id);
		return new ApiResponse<>(String.format(Constant.USER_EXISTS, id), true, userDTO);
	}

	public ApiResponse<UserDTO> createUser(UserDTO userDTO) {
		logger.info("Start createUser at Service");
		User createUser = mapper.map(userDTO, User.class);

		Long userId = createUser.getId();
		String email = createUser.getEmail();
		Boolean isEmailExists = userRepository.existsByEmail(email);

		if ((userId != null && userRepository.existsById(userId)) || isEmailExists) {
			logger.warn("User already with ID {} or Email {} already exists", userId, email);
			return new ApiResponse<>(String.format(Constant.USER_ALREADY_EXISTS, userId), false, null);
		}

		createUser = userRepository.save(createUser);
		UserDTO createdUserDTO = mapper.map(createUser, UserDTO.class);
		return new ApiResponse<>(Constant.USER_SAVED_SUCCESS, true, createdUserDTO);
	}

}