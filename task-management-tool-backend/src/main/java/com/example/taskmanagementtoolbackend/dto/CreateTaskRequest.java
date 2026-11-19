package com.example.taskmanagementtoolbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(
        @NotBlank(message = "Task title is required") String title,
        String description
) {}

