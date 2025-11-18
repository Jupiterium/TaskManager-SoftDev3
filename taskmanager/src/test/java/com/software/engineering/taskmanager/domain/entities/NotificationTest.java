package com.software.engineering.taskmanager.domain.entities;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class NotificationTest {

    @Test
    void testNotificationCreation() {
        UUID id = UUID.randomUUID();
        String title = "Test Title";
        String message = "Test Message";
        NotificationType type = NotificationType.TASK_DUE_SOON;
        Task task = new Task();
        boolean isRead = false;
        LocalDateTime created = LocalDateTime.now();

        Notification notification = new Notification(id, title, message, type, task, isRead, created);

        assertEquals(id, notification.getId());
        assertEquals(title, notification.getTitle());
        assertEquals(message, notification.getMessage());
        assertEquals(type, notification.getType());
        assertEquals(task, notification.getTask());
        assertEquals(isRead, notification.isRead());
        assertEquals(created, notification.getCreated());
    }

    @Test
    void testNotificationSetters() {
        Notification notification = new Notification();
        UUID id = UUID.randomUUID();
        String title = "Updated Title";
        String message = "Updated Message";
        NotificationType type = NotificationType.CUSTOM_REMINDER;
        Task task = new Task();
        boolean isRead = true;
        LocalDateTime created = LocalDateTime.now();

        notification.setId(id);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setTask(task);
        notification.setRead(isRead);
        notification.setCreated(created);

        assertEquals(id, notification.getId());
        assertEquals(title, notification.getTitle());
        assertEquals(message, notification.getMessage());
        assertEquals(type, notification.getType());
        assertEquals(task, notification.getTask());
        assertTrue(notification.isRead());
        assertEquals(created, notification.getCreated());
    }

    @Test
    void testNotificationEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        LocalDateTime created = LocalDateTime.now();
        
        Notification notification1 = new Notification(id, "Title", "Message", NotificationType.TASK_DUE_SOON, null, false, created);
        Notification notification2 = new Notification(id, "Title", "Message", NotificationType.TASK_DUE_SOON, null, false, created);
        Notification notification3 = new Notification(UUID.randomUUID(), "Different", "Message", NotificationType.TASK_DUE_SOON, null, false, created);

        assertEquals(notification1, notification2);
        assertNotEquals(notification1, notification3);
        assertEquals(notification1.hashCode(), notification2.hashCode());
        assertNotEquals(notification1.hashCode(), notification3.hashCode());
    }

    @Test
    void testNotificationToString() {
        Notification notification = new Notification(
                UUID.randomUUID(), "Title", "Message", NotificationType.TASK_DUE_SOON, null, false, LocalDateTime.now()
        );
        
        String toString = notification.toString();
        assertTrue(toString.contains("Notification{"));
        assertTrue(toString.contains("title='Title'"));
        assertTrue(toString.contains("message='Message'"));
    }
}