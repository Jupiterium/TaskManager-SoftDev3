package com.software.engineering.taskmanager.services;

import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.repositories.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReminderSchedulerServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ReminderSchedulerService reminderSchedulerService;

    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task();
        testTask.setId(UUID.randomUUID());
        testTask.setTitle("Test Task");
    }

    @Test
    void checkCustomReminders_CreatesNotificationsForTasksWithReminders() {
        when(taskRepository.findByCustomReminderDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(testTask));

        reminderSchedulerService.checkCustomReminders();

        verify(taskRepository).findByCustomReminderDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(notificationService).createCustomReminderNotification(testTask);
    }

    @Test
    void checkCustomReminders_NoTasksWithReminders_NoNotificationsCreated() {
        when(taskRepository.findByCustomReminderDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        reminderSchedulerService.checkCustomReminders();

        verify(taskRepository).findByCustomReminderDateTimeBetween(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(notificationService, never()).createCustomReminderNotification(any());
    }

    @Test
    void checkDueSoonTasks_CreatesNotificationsForDueSoonTasks() {
        when(taskRepository.findByDueDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of(testTask));

        reminderSchedulerService.checkDueSoonTasks();

        verify(taskRepository).findByDueDateBetween(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(notificationService).createTaskDueNotification(testTask);
    }

    @Test
    void checkDueSoonTasks_NoTasksDueSoon_NoNotificationsCreated() {
        when(taskRepository.findByDueDateBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        reminderSchedulerService.checkDueSoonTasks();

        verify(taskRepository).findByDueDateBetween(any(LocalDateTime.class), any(LocalDateTime.class));
        verify(notificationService, never()).createTaskDueNotification(any());
    }
}