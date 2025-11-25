package com.software.engineering.taskmanager.mappers;

import com.software.engineering.taskmanager.domain.dto.NotificationDto;
import com.software.engineering.taskmanager.domain.entities.Notification;

public interface NotificationMapper {
    NotificationDto toDto(Notification notification);
}