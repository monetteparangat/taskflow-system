package com.example.taskflow_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskflow_system.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
