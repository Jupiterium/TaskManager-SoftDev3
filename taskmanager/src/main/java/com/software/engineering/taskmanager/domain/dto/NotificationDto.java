package com.software.engineering.taskmanager.domain.dto;

import com.software.engineering.taskmanager.domain.entities.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationDto(
        UUID id,
        String title,
        String message,
        NotificationType type,
        UUID taskId,
        String taskTitle,
        boolean isRead,
        LocalDateTime created
) {}