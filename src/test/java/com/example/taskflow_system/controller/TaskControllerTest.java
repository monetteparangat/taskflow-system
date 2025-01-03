package com.example.taskflow_system.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.taskflow_system.dto.TaskDTO;
import com.example.taskflow_system.response.ApiResponse;
import com.example.taskflow_system.service.TaskService;

import java.util.List;

class TaskControllerTest {

	private MockMvc mockMvc;

	@Mock
	private TaskService taskService;

	@InjectMocks
	private TaskController taskController;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(taskController)
				.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
	}

	/*
	 * @Test void testGetAllTasks() throws Exception { // Arrange Pageable pageable
	 * = PageRequest.of(0, 10); TaskDTO task1 = new TaskDTO(); task1.setId(1L);
	 * task1.setTitle("Task 1");
	 * 
	 * TaskDTO task2 = new TaskDTO(); task2.setId(2L); task2.setTitle("Task 2");
	 * 
	 * List<TaskDTO> tasks = List.of(task1, task2); Page<TaskDTO> taskPage = new
	 * PageImpl<>(tasks, pageable, tasks.size()); ApiResponse<Page<TaskDTO>>
	 * response = new ApiResponse<>("Tasks retrieved successfully", true, taskPage);
	 * 
	 * when(taskService.getAllTasks(pageable)).thenReturn(response);
	 * 
	 * // Act & Assert
	 * mockMvc.perform(get("/api/task/getAll?page=0&size=10").contentType(MediaType.
	 * APPLICATION_JSON))
	 * .andExpect(status().isOk()).andExpect(jsonPath("$.success").value(true))
	 * .andExpect(jsonPath("$.message").value("Tasks retrieved successfully"))
	 * .andExpect(jsonPath("$.data.content[0].id").value(1))
	 * .andExpect(jsonPath("$.data.content[0].title").value("Task 1"))
	 * .andExpect(jsonPath("$.data.content[1].id").value(2))
	 * .andExpect(jsonPath("$.data.content[1].title").value("Task 2"));
	 * 
	 * }
	 */
}
