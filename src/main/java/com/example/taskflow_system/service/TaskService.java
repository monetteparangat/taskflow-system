package com.example.taskflow_system.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.taskflow_system.dto.TaskDTO;
import com.example.taskflow_system.model.Task;
import com.example.taskflow_system.model.enums.Status;
import com.example.taskflow_system.response.ApiResponse;

public interface TaskService {

	ApiResponse<Page<TaskDTO>> getAllTasks(Pageable pageable);

	ApiResponse<TaskDTO> getTaskById(Long id);

	ApiResponse<TaskDTO> createTask(TaskDTO task);

	ApiResponse<TaskDTO> updateTask(Long id, TaskDTO taskDTO);

	ApiResponse<Void> deleteTaskById(Long id);

	ApiResponse<Void> changeStatus(Long id, Status status);

	ApiResponse<Page<TaskDTO>> getTasksByUser(Long userId, Pageable pageable);

	ApiResponse<Page<TaskDTO>> getTasksByWorkflow(Long workflowId, Pageable pageable);

	ApiResponse<Page<TaskDTO>> getTasksByStatus(Status status, Pageable pageable);

	void setStartDateIfInProgress(Task task);

	void setEndDateIfCompleted(Task task);
}
