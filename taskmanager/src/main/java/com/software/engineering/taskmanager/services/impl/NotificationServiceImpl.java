package com.software.engineering.taskmanager.services.impl;

import com.software.engineering.taskmanager.domain.entities.Notification;
import com.software.engineering.taskmanager.domain.entities.NotificationType;
import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.repositories.NotificationRepository;
import com.software.engineering.taskmanager.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAllByOrderByCreatedDesc();
    }

    @Override
    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedDesc();
    }

    @Override
    public Notification markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(UUID notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    @Override
    public Notification createNotification(String title, String message, NotificationType type, Task task) {
        Notification notification = new Notification(
                null,
                title,
                message,
                type,
                task,
                false,
                LocalDateTime.now()
        );
        return notificationRepository.save(notification);
    }

    @Override
    public void createTaskDueNotification(Task task) {
        String title = "Task Due Soon";
        String message = String.format("Task '%s' is due soon!", task.getTitle());
        createNotification(title, message, NotificationType.TASK_DUE_SOON, task);
    }
}