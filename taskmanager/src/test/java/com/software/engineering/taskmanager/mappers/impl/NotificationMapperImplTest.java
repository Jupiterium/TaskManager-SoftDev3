package com.software.engineering.taskmanager.mappers.impl;

import com.software.engineering.taskmanager.domain.dto.NotificationDto;
import com.software.engineering.taskmanager.domain.entities.Notification;
import com.software.engineering.taskmanager.domain.entities.NotificationType;
import com.software.engineering.taskmanager.domain.entities.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationMapperImplTest {

    private NotificationMapperImpl notificationMapper;

    @BeforeEach
    void setUp() {
        notificationMapper = new NotificationMapperImpl();
    }

    @Test
    void toDto_WithTask_MapsAllFields() {
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle("Test Task");

        Notification notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setTitle("Test Notification");
        notification.setMessage("Test Message");
        notification.setType(NotificationType.TASK_DUE_SOON);
        notification.setTask(task);
        notification.setRead(true);
        notification.setCreated(LocalDateTime.now());

        NotificationDto dto = notificationMapper.toDto(notification);

        assertEquals(notification.getId(), dto.id());
        assertEquals(notification.getTitle(), dto.title());
        assertEquals(notification.getMessage(), dto.message());
        assertEquals(notification.getType(), dto.type());
        assertEquals(task.getId(), dto.taskId());
        assertEquals(task.getTitle(), dto.taskTitle());
        assertEquals(notification.isRead(), dto.isRead());
        assertEquals(notification.getCreated(), dto.created());
    }

    @Test
    void toDto_WithoutTask_MapsFieldsWithNullTask() {
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setTitle("Test Notification");
        notification.setMessage("Test Message");
        notification.setType(NotificationType.CUSTOM_REMINDER);
        notification.setTask(null);
        notification.setRead(false);
        notification.setCreated(LocalDateTime.now());

        NotificationDto dto = notificationMapper.toDto(notification);

        assertEquals(notification.getId(), dto.id());
        assertEquals(notification.getTitle(), dto.title());
        assertEquals(notification.getMessage(), dto.message());
        assertEquals(notification.getType(), dto.type());
        assertNull(dto.taskId());
        assertNull(dto.taskTitle());
        assertEquals(notification.isRead(), dto.isRead());
        assertEquals(notification.getCreated(), dto.created());
    }
}