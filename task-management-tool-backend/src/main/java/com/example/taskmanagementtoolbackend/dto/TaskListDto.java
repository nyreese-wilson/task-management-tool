package com.example.taskmanagementtoolbackend.dto;

import java.util.List;

public record TaskListDto(
        Long id,
        String title,
        Integer position,
        List<TaskDto> tasks
) {}
