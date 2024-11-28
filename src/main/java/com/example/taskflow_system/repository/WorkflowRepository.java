package com.example.taskflow_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskflow_system.model.Workflow;

public interface WorkflowRepository extends JpaRepository<Workflow, Long>{

}
