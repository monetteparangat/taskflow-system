package com.example.taskflow_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskflow_system.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long>{

}
