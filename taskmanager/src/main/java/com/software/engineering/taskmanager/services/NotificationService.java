package com.software.engineering.taskmanager.services;

import com.software.engineering.taskmanager.domain.entities.Notification;
import com.software.engineering.taskmanager.domain.entities.NotificationType;
import com.software.engineering.taskmanager.domain.entities.Task;

import java.util.List;
import java.util.UUID;

public interface NotificationService {
    List<Notification> getAllNotifications();
    List<Notification> getUnreadNotifications();
    Notification markAsRead(UUID notificationId);
    void deleteNotification(UUID notificationId);
    Notification createNotification(String title, String message, NotificationType type, Task task);
    void createTaskDueNotification(Task task);
    void createCustomReminderNotification(Task task);
}