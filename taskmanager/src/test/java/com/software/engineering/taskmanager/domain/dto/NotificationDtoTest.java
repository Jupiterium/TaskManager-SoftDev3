package com.software.engineering.taskmanager.domain.dto;

import com.software.engineering.taskmanager.domain.entities.NotificationType;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationDtoTest {

    @Test
    void notificationDto_CreatesRecordWithAllFields() {
        UUID id = UUID.randomUUID();
        UUID taskId = UUID.randomUUID();
        String title = "Test Title";
        String message = "Test Message";
        NotificationType type = NotificationType.TASK_DUE_SOON;
        String taskTitle = "Task Title";
        boolean isRead = true;
        LocalDateTime created = LocalDateTime.now();

        NotificationDto dto = new NotificationDto(id, title, message, type, taskId, null, taskTitle, isRead, created);

        assertEquals(id, dto.id());
        assertEquals(title, dto.title());
        assertEquals(message, dto.message());
        assertEquals(type, dto.type());
        assertEquals(taskId, dto.taskId());
        assertNull(dto.taskListId());
        assertEquals(taskTitle, dto.taskTitle());
        assertEquals(isRead, dto.isRead());
        assertEquals(created, dto.created());
    }

    @Test
    void notificationDto_WithNullValues_HandlesNulls() {
        NotificationDto dto = new NotificationDto(null, "Title", "Message", NotificationType.CUSTOM_REMINDER, null, null, null, false, LocalDateTime.now());

        assertNull(dto.id());
        assertNull(dto.taskId());
        assertNull(dto.taskListId());
        assertNull(dto.taskTitle());
        assertEquals("Title", dto.title());
        assertEquals("Message", dto.message());
        assertEquals(NotificationType.CUSTOM_REMINDER, dto.type());
        assertFalse(dto.isRead());
        assertNotNull(dto.created());
    }

    @Test
    void notificationDto_Equality_WorksCorrectly() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now();
        
        NotificationDto dto1 = new NotificationDto(id, "Title", "Message", NotificationType.TASK_DUE_SOON, null, null, null, false, created);
        NotificationDto dto2 = new NotificationDto(id, "Title", "Message", NotificationType.TASK_DUE_SOON, null, null, null, false, created);
        NotificationDto dto3 = new NotificationDto(UUID.randomUUID(), "Different", "Message", NotificationType.TASK_DUE_SOON, null, null, null, false, created);

        assertEquals(dto1, dto2);
        assertNotEquals(dto1, dto3);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void notificationDto_ToString_ContainsAllFields() {
        NotificationDto dto = new NotificationDto(UUID.randomUUID(), "Title", "Message", NotificationType.TASK_ASSIGNED, UUID.randomUUID(), null, "Task", true, LocalDateTime.now());
        
        String toString = dto.toString();
        assertTrue(toString.contains("NotificationDto"));
        assertTrue(toString.contains("Title"));
        assertTrue(toString.contains("Message"));
        assertTrue(toString.contains("TASK_ASSIGNED"));
    }
}