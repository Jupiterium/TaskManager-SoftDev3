package com.software.engineering.taskmanager.integration;

import com.software.engineering.taskmanager.domain.entities.Notification;
import com.software.engineering.taskmanager.domain.entities.NotificationType;
import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.repositories.NotificationRepository;
import com.software.engineering.taskmanager.repositories.TaskRepository;
import com.software.engineering.taskmanager.services.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NotificationServiceIntegrationTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void createAndRetrieveNotifications_WorksEndToEnd() {
        Task task = new Task();
        task.setTitle("Integration Test Task");
        task.setCreated(java.time.LocalDateTime.now());
        task.setUpdated(java.time.LocalDateTime.now());
        task.setPriority(com.software.engineering.taskmanager.domain.entities.TaskPriority.MEDIUM);
        task.setStatus(com.software.engineering.taskmanager.domain.entities.TaskStatus.OPEN);
        task = taskRepository.save(task);

        Notification notification = notificationService.createNotification(
                "Test Title", "Test Message", NotificationType.TASK_DUE_SOON, task
        );

        assertNotNull(notification.getId());
        assertEquals("Test Title", notification.getTitle());
        assertEquals("Test Message", notification.getMessage());
        assertEquals(NotificationType.TASK_DUE_SOON, notification.getType());
        assertEquals(task, notification.getTask());
        assertFalse(notification.isRead());
        assertNotNull(notification.getCreated());

        List<Notification> allNotifications = notificationService.getAllNotifications();
        assertTrue(allNotifications.contains(notification));

        List<Notification> unreadNotifications = notificationService.getUnreadNotifications();
        assertTrue(unreadNotifications.contains(notification));
    }

    @Test
    void markAsReadAndDelete_WorksEndToEnd() {
        Notification notification = notificationService.createNotification(
                "Test Title", "Test Message", NotificationType.CUSTOM_REMINDER, null
        );

        Notification updatedNotification = notificationService.markAsRead(notification.getId());
        assertTrue(updatedNotification.isRead());

        List<Notification> unreadNotifications = notificationService.getUnreadNotifications();
        assertFalse(unreadNotifications.contains(updatedNotification));

        notificationService.deleteNotification(notification.getId());
        assertFalse(notificationRepository.existsById(notification.getId()));
    }

    @Test
    void createTaskSpecificNotifications_WorksEndToEnd() {
        Task task = new Task();
        task.setTitle("Task for Notifications");
        task.setCreated(java.time.LocalDateTime.now());
        task.setUpdated(java.time.LocalDateTime.now());
        task.setPriority(com.software.engineering.taskmanager.domain.entities.TaskPriority.MEDIUM);
        task.setStatus(com.software.engineering.taskmanager.domain.entities.TaskStatus.OPEN);
        task = taskRepository.save(task);

        notificationService.createTaskDueNotification(task);
        notificationService.createCustomReminderNotification(task);

        List<Notification> allNotifications = notificationService.getAllNotifications();
        assertEquals(2, allNotifications.size());

        boolean hasDueNotification = allNotifications.stream()
                .anyMatch(n -> n.getType() == NotificationType.TASK_DUE_SOON);
        boolean hasReminderNotification = allNotifications.stream()
                .anyMatch(n -> n.getType() == NotificationType.CUSTOM_REMINDER);

        assertTrue(hasDueNotification);
        assertTrue(hasReminderNotification);
    }
}