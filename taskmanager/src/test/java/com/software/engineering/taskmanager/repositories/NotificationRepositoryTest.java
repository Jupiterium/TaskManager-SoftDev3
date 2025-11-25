package com.software.engineering.taskmanager.repositories;

import com.software.engineering.taskmanager.domain.entities.Notification;
import com.software.engineering.taskmanager.domain.entities.NotificationType;
import com.software.engineering.taskmanager.domain.entities.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class NotificationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    void findByIsReadFalseOrderByCreatedDesc_ReturnsUnreadNotifications() {
        Notification readNotification = createNotification("Read", true, LocalDateTime.now().minusHours(1));
        Notification unreadNotification1 = createNotification("Unread1", false, LocalDateTime.now().minusMinutes(30));
        Notification unreadNotification2 = createNotification("Unread2", false, LocalDateTime.now());

        entityManager.persistAndFlush(readNotification);
        entityManager.persistAndFlush(unreadNotification1);
        entityManager.persistAndFlush(unreadNotification2);

        List<Notification> unreadNotifications = notificationRepository.findByIsReadFalseOrderByCreatedDesc();

        assertEquals(2, unreadNotifications.size());
        assertEquals("Unread2", unreadNotifications.get(0).getTitle());
        assertEquals("Unread1", unreadNotifications.get(1).getTitle());
    }

    @Test
    void findAllByOrderByCreatedDesc_ReturnsAllNotificationsOrderedByCreated() {
        Notification notification1 = createNotification("First", false, LocalDateTime.now().minusHours(2));
        Notification notification2 = createNotification("Second", true, LocalDateTime.now().minusHours(1));
        Notification notification3 = createNotification("Third", false, LocalDateTime.now());

        entityManager.persistAndFlush(notification1);
        entityManager.persistAndFlush(notification2);
        entityManager.persistAndFlush(notification3);

        List<Notification> allNotifications = notificationRepository.findAllByOrderByCreatedDesc();

        assertEquals(3, allNotifications.size());
        assertEquals("Third", allNotifications.get(0).getTitle());
        assertEquals("Second", allNotifications.get(1).getTitle());
        assertEquals("First", allNotifications.get(2).getTitle());
    }

    @Test
    void findByTaskIdOrderByCreatedDesc_ReturnsNotificationsForTask() {
        Task task = createTaskWithTimestamps();
        entityManager.persistAndFlush(task);

        Notification notification1 = createNotificationWithTask("Task Notification 1", task, LocalDateTime.now().minusHours(1));
        Notification notification2 = createNotificationWithTask("Task Notification 2", task, LocalDateTime.now());
        Notification otherNotification = createNotification("Other", false, LocalDateTime.now());

        entityManager.persistAndFlush(notification1);
        entityManager.persistAndFlush(notification2);
        entityManager.persistAndFlush(otherNotification);

        List<Notification> taskNotifications = notificationRepository.findByTaskIdOrderByCreatedDesc(task.getId());

        assertEquals(2, taskNotifications.size());
        assertEquals("Task Notification 2", taskNotifications.get(0).getTitle());
        assertEquals("Task Notification 1", taskNotifications.get(1).getTitle());
    }

    private Notification createNotification(String title, boolean isRead, LocalDateTime created) {
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage("Test message");
        notification.setType(NotificationType.TASK_DUE_SOON);
        notification.setRead(isRead);
        notification.setCreated(created);
        return notification;
    }

    private Notification createNotificationWithTask(String title, Task task, LocalDateTime created) {
        Notification notification = createNotification(title, false, created);
        notification.setTask(task);
        return notification;
    }

    private Task createTaskWithTimestamps() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setCreated(java.time.LocalDateTime.now());
        task.setUpdated(java.time.LocalDateTime.now());
        task.setPriority(com.software.engineering.taskmanager.domain.entities.TaskPriority.MEDIUM);
        task.setStatus(com.software.engineering.taskmanager.domain.entities.TaskStatus.OPEN);
        return task;
    }
}