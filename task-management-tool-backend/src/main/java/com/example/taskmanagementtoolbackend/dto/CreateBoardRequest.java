package com.example.taskmanagementtoolbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateBoardRequest(
        @NotBlank(message = "Board title is required") String title,
        @NotNull(message = "Workspace ID is required") Long workspaceId
) {}

