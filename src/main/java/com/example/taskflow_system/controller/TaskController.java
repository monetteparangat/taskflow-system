package com.example.taskflow_system.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskflow_system.dto.TaskDTO;
import com.example.taskflow_system.response.ApiResponse;
import com.example.taskflow_system.service.TaskService;

import com.example.taskflow_system.model.enums.Status;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/task")
@Validated
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
	@Autowired
	private TaskService taskService;

	@GetMapping("/getAll")
	public ResponseEntity<ApiResponse<Page<TaskDTO>>> getAllTasks(Pageable pageable) {
		logger.info("Start getAllTasks at controller: Fetching all tasks");
		ApiResponse<Page<TaskDTO>> tasksDTO = taskService.getAllTasks(pageable);
		logger.info("End getAllTasks at controller: Successfully fetched all tasks");
		return new ResponseEntity<>(tasksDTO, HttpStatus.OK);
	}

	@GetMapping("/get")
	public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@RequestParam(value = "id") @NotNull Long id) {
		logger.info("Start getTaskByID at controller: Fetching task with ID: {}", id);
		ApiResponse<TaskDTO> taskDTO = taskService.getTaskById(id);
		logger.info("End getTaskByID at controller: Successfully fetched task with ID: {}", id);
		return new ResponseEntity<>(taskDTO, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<TaskDTO>> createTask(@RequestBody TaskDTO taskDTO) {
		logger.info("Start createTask at controller");
		ApiResponse<TaskDTO> createdTask = taskService.createTask(taskDTO);
		logger.info("End createTask at controller");
		return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<ApiResponse<TaskDTO>> updateTask(@RequestParam(value = "id") @NotNull Long id,
			@RequestBody TaskDTO taskDTO) {
		logger.info("Start updateTask at controller: Updating task with ID: {}", id);
		ApiResponse<TaskDTO> updatedTask = taskService.updateTask(id, taskDTO);
		logger.info("End updateTask at controller: Updated task with ID: {}", id);
		return new ResponseEntity<>(updatedTask, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse<Void>> deleteTask(@RequestParam(value = "id") @NotNull Long id) {
		logger.info("Start deleteTask at controller: Deleting task with ID: {}", id);
		ApiResponse<Void> response = taskService.deleteTaskById(id);
		logger.info("End deleteTask at controller: Deleted task with ID: {}", id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/by-status")
	public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTaskByStatus(
			@RequestParam(value = "status") @NotNull Status status, Pageable pageable) {
		logger.info("Start getTaskByStatus at controller: Fetch task with status: {}", status);
		ApiResponse<Page<TaskDTO>> taskDTO = taskService.getTasksByStatus(status, pageable);
		logger.info("End getTaskByStatus at controller: Fetch task with status: {}", status);
		return new ResponseEntity<>(taskDTO, HttpStatus.OK);
	}

	@GetMapping("/get/by-user")
	public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTasksByUser(@RequestParam(value = "id") @NotNull Long id,
			Pageable pageable) {
		logger.info("Start getTasksByUser at controller: Fetching task with user ID: {}", id);
		ApiResponse<Page<TaskDTO>> taskDTO = taskService.getTasksByUser(id, pageable);
		logger.info("End getTasksByUser at controller: Fetching task with user ID: {}", id);
		return new ResponseEntity<>(taskDTO, HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTasksByWorkflow(@RequestParam(value = "id") @NotNull Long id,
			Pageable pageable) {
		logger.info("Start getTasksByWorkflow at controller: Fetching task with workflow ID: {}", id);
		ApiResponse<Page<TaskDTO>> taskDTO = taskService.getTasksByWorkflow(id, pageable);
		logger.info("End getTasksByWorkflow at controller: Fetching task with workflow ID: {}", id);
		return new ResponseEntity<>(taskDTO, HttpStatus.OK);
	}

	@PatchMapping("/update/status")
	public ResponseEntity<ApiResponse<Void>> updateStatus(@RequestParam(value = "status") @NotNull Status status,
			@RequestParam(value = "id") @NotNull Long id) {
		logger.info("Start updateStatus at controller: Update task status with ID: {}", id);
		ApiResponse<Void> response = taskService.changeStatus(id, status);
		logger.info("End updateStatus at controller: Update task status with ID: {}", id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
