package com.example.taskmanagementtoolbackend.dto;

import com.example.taskmanagementtoolbackend.model.TaskStatus;

public record UpdateTaskRequest(
        String title,
        String description,
        TaskStatus status,
        Long targetListId,
        Integer position
) {}

