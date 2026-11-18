package com.example.taskmanagementtoolbackend.repository;

import com.example.taskmanagementtoolbackend.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}

