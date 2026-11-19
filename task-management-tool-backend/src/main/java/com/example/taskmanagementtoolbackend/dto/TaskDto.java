package com.example.taskmanagementtoolbackend.dto;

import com.example.taskmanagementtoolbackend.model.TaskStatus;

public record TaskDto(
        Long id,
        String title,
        String description,
        TaskStatus status,
        Integer position,
        Long listId
) {}
