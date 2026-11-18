package com.example.taskmanagementtoolbackend.repository;

import com.example.taskmanagementtoolbackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

