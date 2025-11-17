package com.software.engineering.taskmanager.controllers;

import com.software.engineering.taskmanager.domain.dto.NotificationDto;
import com.software.engineering.taskmanager.mappers.NotificationMapper;
import com.software.engineering.taskmanager.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        List<NotificationDto> notifications = notificationService.getAllNotifications()
                .stream()
                .map(notificationMapper::toDto)
                .toList();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications() {
        List<NotificationDto> notifications = notificationService.getUnreadNotifications()
                .stream()
                .map(notificationMapper::toDto)
                .toList();
        return ResponseEntity.ok(notifications);
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable UUID id) {
        NotificationDto notification = notificationMapper.toDto(
                notificationService.markAsRead(id)
        );
        return ResponseEntity.ok(notification);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/test")
    public ResponseEntity<NotificationDto> createTestNotification() {
        NotificationDto notification = notificationMapper.toDto(
                notificationService.createNotification(
                        "Test Notification",
                        "This is a test notification",
                        com.software.engineering.taskmanager.domain.entities.NotificationType.TASK_DUE_SOON,
                        null
                )
        );
        return ResponseEntity.ok(notification);
    }
}