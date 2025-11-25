package com.software.engineering.taskmanager.services.impl;

import com.software.engineering.taskmanager.domain.entities.Notification;
import com.software.engineering.taskmanager.domain.entities.NotificationType;
import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private Notification testNotification;
    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(UUID.randomUUID());
        testTask.setTitle("Test Task");

        testNotification = new Notification();
        testNotification.setId(UUID.randomUUID());
        testNotification.setTitle("Test Notification");
        testNotification.setMessage("Test Message");
        testNotification.setType(NotificationType.TASK_DUE_SOON);
        testNotification.setRead(false);
        testNotification.setCreated(LocalDateTime.now());
    }

    @Test
    void getAllNotifications_ReturnsAllNotifications() {
        when(notificationRepository.findAllByOrderByCreatedDesc()).thenReturn(List.of(testNotification));

        List<Notification> result = notificationService.getAllNotifications();

        assertEquals(1, result.size());
        assertEquals(testNotification, result.get(0));
        verify(notificationRepository).findAllByOrderByCreatedDesc();
    }

    @Test
    void getUnreadNotifications_ReturnsUnreadNotifications() {
        when(notificationRepository.findByIsReadFalseOrderByCreatedDesc()).thenReturn(List.of(testNotification));

        List<Notification> result = notificationService.getUnreadNotifications();

        assertEquals(1, result.size());
        assertEquals(testNotification, result.get(0));
        verify(notificationRepository).findByIsReadFalseOrderByCreatedDesc();
    }

    @Test
    void markAsRead_UpdatesNotificationAndReturns() {
        UUID notificationId = testNotification.getId();
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.of(testNotification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        Notification result = notificationService.markAsRead(notificationId);

        assertTrue(result.isRead());
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository).save(testNotification);
    }

    @Test
    void markAsRead_ThrowsExceptionWhenNotificationNotFound() {
        UUID notificationId = UUID.randomUUID();
        when(notificationRepository.findById(notificationId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> notificationService.markAsRead(notificationId));
        verify(notificationRepository).findById(notificationId);
        verify(notificationRepository, never()).save(any());
    }

    @Test
    void deleteNotification_CallsRepositoryDelete() {
        UUID notificationId = UUID.randomUUID();

        notificationService.deleteNotification(notificationId);

        verify(notificationRepository).deleteById(notificationId);
    }

    @Test
    void createNotification_CreatesAndSavesNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        Notification result = notificationService.createNotification(
                "Title", "Message", NotificationType.TASK_DUE_SOON, testTask
        );

        assertNotNull(result);
        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createTaskDueNotification_CreatesCorrectNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        notificationService.createTaskDueNotification(testTask);

        verify(notificationRepository).save(any(Notification.class));
    }

    @Test
    void createCustomReminderNotification_CreatesCorrectNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

        notificationService.createCustomReminderNotification(testTask);

        verify(notificationRepository).save(any(Notification.class));
    }
}