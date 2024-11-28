package com.example.taskflow_system.dto;

import java.time.LocalDateTime;

import com.example.taskflow_system.model.enums.Priority;
import com.example.taskflow_system.model.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {

	private Long id;
	private String title;
	private String description;
	private Status status;
	private Priority priority;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDateTime deadline;
	private Long userId;
	private Long workflowId;
}
