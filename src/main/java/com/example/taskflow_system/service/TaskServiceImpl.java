package com.example.taskflow_system.service;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Service
public class TaskServiceImpl implements TaskService {

	private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);
	@Autowired
	private TaskRepository taskRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private WorkflowRepository workflowRepository;
	@Autowired
	private ModelMapper mapper;

	@Override
	public ApiResponse<Page<TaskDTO>> getAllTasks(Pageable pageable) {
		logger.info("Start getAllTasks at Service");
		Page<Task> tasks = taskRepository.findAll(pageable);
		Page<TaskDTO> tasksDTO = tasks.map(task -> mapper.map(task, TaskDTO.class));
		logger.info("End getAllTasks at Service");
		return new ApiResponse<Page<TaskDTO>>(Constant.TASK_RETRIEVED_SUCCESS, true, tasksDTO);
	}

	@Override
	public ApiResponse<TaskDTO> getTaskById(Long id) {
		logger.info("Start getTaskById at Service: Fetching task with ID: {}", id);
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException(String.format(Constant.TASK_NOT_FOUND, id)));
		TaskDTO taskDTO = mapper.map(task, TaskDTO.class);
		logger.info("End getTaskById at Service");
		return new ApiResponse<>(Constant.TASK_RETRIEVED_SUCCESS, true, taskDTO);
	}

	@Override
	public ApiResponse<TaskDTO> createTask(TaskDTO taskDTO) {
		logger.info("Start createTask at Service");
		Task task = mapper.map(taskDTO, Task.class);

		Long taskId = task.getId();
		if (taskId != null) {
			return new ApiResponse<>(String.format(Constant.TASK_SAVED_FAILED, taskId), false);
		}

		Task saveTask = taskRepository.save(task);

		TaskDTO savedTaskDTO = mapper.map(saveTask, TaskDTO.class);
		logger.info("End createTask at Service");
		return new ApiResponse<>(Constant.TASK_SAVED_SUCCESS, true, savedTaskDTO);
	}

	@Override
	public ApiResponse<TaskDTO> updateTask(Long id, TaskDTO taskDTO) {
		logger.info("Start updateTask at Service");
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
			setStartDateIfInProgress(existingTask);
			setEndDateIfCompleted(existingTask);
		}

		Task updatedTask = taskRepository.save(existingTask);
		updatedTaskDTO = mapper.map(updatedTask, TaskDTO.class);

		if (updatedTask.getId() == null) {
			return new ApiResponse<>(String.format(Constant.TASK_UPDATED_FAILED, id), false, updatedTaskDTO);
		}

		logger.info("End updateTask at Service");
		return new ApiResponse<>(String.format(Constant.TASK_UPDATED_SUCCESS, id), true, updatedTaskDTO);
	}

	@Override
	public ApiResponse<Void> deleteTaskById(Long id) {
		logger.info("Start deleteTaskById at Service");
		Boolean isExists = taskRepository.existsById(id);
		if (!isExists) {
			return new ApiResponse<>(String.format(Constant.TASK_NOT_FOUND, id), false);
		}
		taskRepository.deleteById(id);
		logger.info("End deleteTaskById at Service");
		return new ApiResponse<>(String.format(Constant.TASK_DELETED_SUCCESS, id), true);

	}

	public ApiResponse<Void> changeStatus(Long id, Status status) {
		logger.info("Start changeStatus at Service");
		Task task = taskRepository.findById(id)
				.orElseThrow(() -> new TaskNotFoundException(String.format(Constant.TASK_NOT_FOUND, id)));
		task.setStatus(status);
		setStartDateIfInProgress(task);
		setEndDateIfCompleted(task);
		taskRepository.save(task);
		logger.info("End changeStatus at Service");
		return new ApiResponse<>(String.format(Constant.TASK_STATUS_UPDATE_SUCCESS, status), true);
	}

	public ApiResponse<Page<TaskDTO>> getTasksByUser(Long userId, Pageable pageable) {
		logger.info("Start getTasksByUser at Service");
		Page<Task> tasksPage = taskRepository.findByUserId(userId, pageable);

		if (tasksPage.isEmpty()) {
			return new ApiResponse<>(String.format(Constant.USERS_TASKS_NOT_FOUND, userId), false, Page.empty());
		}
		Page<TaskDTO> tasksPageDTO = tasksPage.map(task -> mapper.map(task, TaskDTO.class));
		logger.info("End getTasksByUser at Service");
		return new ApiResponse<>(String.format(Constant.USERS_TASKS_RETRIEVED, userId), true, tasksPageDTO);
	}

	public ApiResponse<Page<TaskDTO>> getTasksByWorkflow(Long workflowId, Pageable pageable) {
		logger.info("Start getTasksByWorkflow at Service");
		Page<Task> tasksPage = taskRepository.findByWorkflowId(workflowId, pageable);

		if (tasksPage.isEmpty()) {
			return new ApiResponse<>(String.format(Constant.WORKFLOWS_TASKS_NOT_FOUND, workflowId), false,
					Page.empty());
		}

		Page<TaskDTO> tasksPageDTO = tasksPage.map(task -> mapper.map(task, TaskDTO.class));
		logger.info("End getTasksByWorkflow at Service");
		return new ApiResponse<>(String.format(Constant.WORKFLOWS_TASKS_RETRIEVED, workflowId), true, tasksPageDTO);
	}

	public ApiResponse<Page<TaskDTO>> getTasksByStatus(Status status, Pageable pageable) {
		logger.info("Start getTasksByStatus at Service");
		Page<Task> tasksPage = taskRepository.findByStatus(status, pageable);

		if (tasksPage.isEmpty()) {
			return new ApiResponse<>(String.format(Constant.STATUS_TASKS_NOT_FOUND, status), false);
		}

		Page<TaskDTO> tasksPageDTO = tasksPage.map(task -> mapper.map(task, TaskDTO.class));
		logger.info("End getTasksByStatus at Service");
		return new ApiResponse<>(String.format(Constant.STATUS_TASKS_RETRIEVED, status), true, tasksPageDTO);
	}

	public void setStartDateIfInProgress(Task task) {
		logger.info("Start setStartDateIfInProgress at Service");
		if (task.getStatus().equals(Status.IN_PROGRESS) && task.getStartDate() == null) {
			task.setStartDate(LocalDateTime.now());
			taskRepository.save(task);
		}
		logger.info("End setStartDateIfInProgress at Service");
	}

	public void setEndDateIfCompleted(Task task) {
		logger.info("Start setEndDateIfCompleted at Service");
		if (task.getStatus().equals(Status.COMPLETED) && task.getEndDate() == null) {
			task.setEndDate(LocalDateTime.now());
			taskRepository.save(task);
		}
		logger.info("End setEndDateIfCompleted at Service");
	}

}
