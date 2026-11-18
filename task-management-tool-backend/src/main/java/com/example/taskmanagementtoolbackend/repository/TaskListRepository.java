package com.example.taskmanagementtoolbackend.repository;

import com.example.taskmanagementtoolbackend.model.TaskList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskListRepository extends JpaRepository<TaskList, Long> {
}

