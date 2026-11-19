package com.example.taskmanagementtoolbackend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateListRequest(
        @NotBlank(message = "List title is required") String title
) {}
