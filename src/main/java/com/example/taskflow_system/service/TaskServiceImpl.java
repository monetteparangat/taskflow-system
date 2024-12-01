package com.example.taskflow_system.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.taskflow_system.dto.TaskDTO;
import com.example.taskflow_system.exception.TaskNotFoundException;
import com.example.taskflow_system.exception.UserNotFoundException;
import com.example.taskflow_system.exception.WorkflowNotFoundException;
import com.example.taskflow_system.model.Task;
import com.example.taskflow_system.model.User;
import com.example.taskflow_system.model.Workflow;
import com.example.taskflow_system.model.enums.Status;
import com.example.taskflow_system.repository.TaskRepository;
import com.example.taskflow_system.repository.UserRepository;
import com.example.taskflow_system.repository.WorkflowRepository;
import com.example.taskflow_system.response.ApiResponse;
import com.example.taskflow_system.response.Constant;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {

	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkflowRepository workflowRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public List<TaskDTO> getAllTasks() {
		List<Task> tasks = taskRepository.findAll();
		List<TaskDTO> tasksDTO = tasks.stream().map(task -> mapper.map(task, TaskDTO.class))
				.collect(Collectors.toList());
		return tasksDTO;
	}

	@Override
	public TaskDTO getTaskById(Long id) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException(String.format(Constant.TASK_NOT_FOUND, id)));
		TaskDTO taskDTO = mapper.map(task, TaskDTO.class);
		return taskDTO;
	}

	@Override
	public ApiResponse<TaskDTO> createTask(TaskDTO taskDTO) {
		Task task = mapper.map(taskDTO, Task.class);

		Long taskId = task.getId();
		if (taskId != null) {
			return new ApiResponse<>(String.format(Constant.TASK_SAVED_FAILED, taskId), false);
		}

		Task saveTask = taskRepository.save(task);

		TaskDTO savedTaskDTO = mapper.map(saveTask, TaskDTO.class);
		return new ApiResponse<>(Constant.TASK_SAVED_SUCCESS, true, savedTaskDTO);
	}

	@Override
	public ApiResponse<TaskDTO> updateTask(Long id, TaskDTO taskDTO) {
		TaskDTO updatedTaskDTO = null;
		Task existingTask = taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException(String.format(Constant.TASK_NOT_FOUND, id)));

		Long userId = taskDTO.getUserId();
		Long workflowId = taskDTO.getWorkflowId();

		Workflow workflow = workflowRepository.findById(workflowId)
				.orElseThrow(() -> new WorkflowNotFoundException(String.format(Constant.WORKFLOW_NOT_FOUND, id)));
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundException(String.format(Constant.USER_NOT_FOUND, id)));

		existingTask.setTitle(taskDTO.getTitle());
		existingTask.setDescription(taskDTO.getDescription());
		existingTask.setStatus(taskDTO.getStatus());
		existingTask.setPriority(taskDTO.getPriority());
		existingTask.setStartDate(taskDTO.getStartDate());
		existingTask.setEndDate(taskDTO.getEndDate());
		existingTask.setDeadline(taskDTO.getDeadline());
		existingTask.setUser(user);
		existingTask.setWorkflow(workflow);

		if (existingTask.getStatus() != taskDTO.getStatus()) {
			setStartDateIfInProgress(id);
			setEndDateIfCompleted(id);
		}

		Task updatedTask = taskRepository.save(existingTask);
		updatedTaskDTO = mapper.map(updatedTask, TaskDTO.class);

		if (updatedTask.getId() == null) {
			return new ApiResponse<>(String.format(Constant.TASK_UPDATED_FAILED, id), false, updatedTaskDTO);
		}

		return new ApiResponse<>(String.format(Constant.TASK_UPDATED_SUCCESS, id), true, updatedTaskDTO);
	}

	@Override
	public ApiResponse<TaskDTO> deleteTaskById(Long id) {
		Boolean isExists = taskRepository.existsById(id);
		if (!isExists) {
			return new ApiResponse<>(String.format(Constant.TASK_NOT_FOUND, id), false);
		}
		taskRepository.deleteById(id);
		return new ApiResponse<>(String.format(Constant.TASK_DELETED_SUCCESS, id), true);

	}

	public void changeStatus(Long id, Status status) {
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException(String.format(Constant.TASK_NOT_FOUND, id)));
		task.setStatus(status);
		taskRepository.save(task);
	}

	public ApiResponse<Page<TaskDTO>> getTasksByUser(Long userId, Pageable pageable) {
		Page<Task> tasksPage = taskRepository.findByUserId(userId, pageable);

		if (tasksPage.isEmpty()) {
			return new ApiResponse<>(String.format(Constant.USERS_TASKS_NOT_FOUND, userId), false, Page.empty());
		}
		Page<TaskDTO> tasksPageDTO = tasksPage.map(task -> mapper.map(task, TaskDTO.class));
		return new ApiResponse<>(String.format(Constant.USERS_TASKS_RETRIEVED, userId), true, tasksPageDTO);
	}

	public ApiResponse<Page<TaskDTO>> getTasksByWorkflow(Long workflowId, Pageable pageable) {
		Page<Task> tasksPage = taskRepository.findByWorkflowId(workflowId, pageable);

		if (tasksPage.isEmpty()) {
			return new ApiResponse<>(String.format(Constant.WORKFLOWS_TASKS_NOT_FOUND, workflowId), false,
					Page.empty());
		}

		Page<TaskDTO> tasksPageDTO = tasksPage.map(task -> mapper.map(task, TaskDTO.class));
		return new ApiResponse<>(String.format(Constant.WORKFLOWS_TASKS_RETRIEVED, workflowId), true, tasksPageDTO);
	}

	public ApiResponse<Page<TaskDTO>> getTasksByStatus(Status status, Pageable pageable) {
		Page<Task> tasksPage = taskRepository.findByStatus(status, pageable);

		if (tasksPage.isEmpty()) {
			return new ApiResponse<>(String.format(Constant.STATUS_TASKS_NOT_FOUND, status), false);
		}

		Page<TaskDTO> tasksPageDTO = tasksPage.map(task -> mapper.map(task, TaskDTO.class));
		return new ApiResponse<>(String.format(Constant.STATUS_TASKS_RETRIEVED, status), true, tasksPageDTO);
	}

	public void setStartDateIfInProgress(Long taskId) {
		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new TaskNotFoundException(Constant.TASK_NOT_FOUND));

		if (task.getStatus().equals(Status.IN_PROGRESS) && task.getStartDate() == null) {
			task.setStartDate(LocalDateTime.now());
			taskRepository.save(task);
		}
	}

	public void setEndDateIfCompleted(Long taskId) {
		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new TaskNotFoundException(Constant.TASK_NOT_FOUND));

		if (task.getStatus().equals(Status.COMPLETED) && task.getEndDate() == null) {
			task.setEndDate(LocalDateTime.now());
			taskRepository.save(task);
		}
	}

}
