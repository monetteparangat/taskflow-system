package com.example.taskflow_system.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskflow_system.model.Task;
import com.example.taskflow_system.model.enums.Status;

public interface TaskRepository extends JpaRepository<Task, Long>{

	Page<Task> findByUserId(Long userId, Pageable pageable);
	Page<Task> findByWorkflowId(Long workflowId, Pageable pageable);
	Page<Task> findByStatus(Status status, Pageable pageable);
}
