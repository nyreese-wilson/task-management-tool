package com.example.taskmanagementtoolbackend.dto;

import java.util.List;

public record BoardDto(
        Long id,
        String title,
        Long workspaceId,
        List<TaskListDto> lists
) {}
