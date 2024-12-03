package com.example.taskflow_system.controller;

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

	@Autowired
	private TaskService taskService;

	@GetMapping("/get")
	public ResponseEntity<ApiResponse<TaskDTO>> getTaskById(@RequestParam(value = "id") @NotNull Long id) {
		ApiResponse<TaskDTO> taskDTO = taskService.getTaskById(id);
		return new ResponseEntity<>(taskDTO, HttpStatus.OK);
	}

	@GetMapping("/getAll")
	public ResponseEntity<ApiResponse<Page<TaskDTO>>> getAllTasks(Pageable pageable) {
		ApiResponse<Page<TaskDTO>> tasksDTO = taskService.getAllTasks(pageable);
		return new ResponseEntity<>(tasksDTO, HttpStatus.OK);
	}

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<TaskDTO>> createTask(@RequestBody TaskDTO taskDTO) {
		ApiResponse<TaskDTO> createdTask = taskService.createTask(taskDTO);
		return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
	}

	@PutMapping("/update")
	public ResponseEntity<ApiResponse<TaskDTO>> updateTask(@RequestParam(value = "id") @NotNull Long id,
			@RequestBody TaskDTO taskDTO) {
		ApiResponse<TaskDTO> updatedTask = taskService.updateTask(id, taskDTO);
		return new ResponseEntity<>(updatedTask, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<ApiResponse<Void>> deleteTask(@RequestParam(value = "id") @NotNull Long id) {
		ApiResponse<Void> response = taskService.deleteTaskById(id);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/get/by-status")
	public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTaskByStatus(
			@RequestParam(value = "status") @NotNull Status status, Pageable pageable) {
		ApiResponse<Page<TaskDTO>> taskDTO = taskService.getTasksByStatus(status, pageable);
		return new ResponseEntity<>(taskDTO, HttpStatus.OK);
	}

	@GetMapping("/get/by-user")
	public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTasksByUser(@RequestParam(value = "id") @NotNull Long id,
			Pageable pageable) {
		ApiResponse<Page<TaskDTO>> taskDTO = taskService.getTasksByUser(id, pageable);
		return new ResponseEntity<>(taskDTO, HttpStatus.OK);
	}

	public ResponseEntity<ApiResponse<Page<TaskDTO>>> getTasksByWorkflow(@RequestParam(value = "id") @NotNull Long id,
			Pageable pageable) {
		ApiResponse<Page<TaskDTO>> taskDTO = taskService.getTasksByWorkflow(id, pageable);
		return new ResponseEntity<>(taskDTO, HttpStatus.OK);
	}

	@PatchMapping("/update/status")
	public ResponseEntity<ApiResponse<Void>> updateStatus(@RequestParam(value = "status") @NotNull Status status,
			@RequestParam(value = "id") @NotNull Long id) {
		ApiResponse<Void> response = taskService.changeStatus(id, status);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
