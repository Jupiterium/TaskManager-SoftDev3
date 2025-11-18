package com.software.engineering.taskmanager.mappers.impl;

import com.software.engineering.taskmanager.domain.dto.NotificationDto;
import com.software.engineering.taskmanager.domain.entities.Notification;
import com.software.engineering.taskmanager.mappers.NotificationMapper;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapperImpl implements NotificationMapper {

    @Override
    public NotificationDto toDto(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getTask() != null ? notification.getTask().getId() : null,
                notification.getTask() != null ? notification.getTask().getTitle() : null,
                notification.isRead(),
                notification.getCreated()
        );
    }
}