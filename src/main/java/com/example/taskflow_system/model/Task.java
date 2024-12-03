package com.example.taskflow_system.model;

import java.time.LocalDateTime;
import java.util.List;

import com.example.taskflow_system.model.enums.Priority;
import com.example.taskflow_system.model.enums.Status;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Size(max = 255)
	private String title;

	@Size(max = 255)
	private String description;

	@Enumerated(EnumType.STRING)
	private Status status;

	@Enumerated(EnumType.STRING)
	private Priority priority;

	@PastOrPresent
	private LocalDateTime startDate;

	private LocalDateTime endDate;

	private LocalDateTime deadline;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "workflow_id")
	private Workflow workflow;
	
	@OneToMany(mappedBy = "task")
	private List<Comment> comments;

}
