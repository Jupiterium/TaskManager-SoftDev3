package com.software.engineering.taskmanager.domain.dto;

import com.software.engineering.taskmanager.domain.entities.TaskPriority;
import com.software.engineering.taskmanager.domain.entities.TaskStatus;

import java.time.LocalDateTime;
import java.util.UUID;

// We use record here as it allows for an immutable class
// This class is basically a data carrier
public record TaskDto(
        // Each instance variable is immutable
        UUID id,
        String title,
        String description,
        LocalDateTime dueDate,
        TaskPriority priority,
        TaskStatus status
)
{ }
