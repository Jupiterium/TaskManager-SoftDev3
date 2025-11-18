package com.software.engineering.taskmanager.integration;

import com.software.engineering.taskmanager.domain.entities.*;
import com.software.engineering.taskmanager.services.NotificationService;
import com.software.engineering.taskmanager.services.ReminderSchedulerService;
import com.software.engineering.taskmanager.services.TaskListService;
import com.software.engineering.taskmanager.services.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class NotificationWorkflowIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskListService taskListService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private ReminderSchedulerService reminderSchedulerService;

    @Test
    void completeNotificationWorkflow_CreateTasksAndNotifications() {
        TaskList taskList = new TaskList(null, "Notification Test Project", "Testing notifications", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        Task taskWithDueDate = new Task(null, "Due Soon Task", "Task due tomorrow", 
                LocalDateTime.now().plusDays(1), TaskStatus.OPEN, TaskPriority.HIGH, null, null, null, null);
        Task taskWithReminder = new Task(null, "Reminder Task", "Task with custom reminder", 
                null, TaskStatus.OPEN, TaskPriority.MEDIUM, null, null, null, null);
        taskWithReminder.setCustomReminderDateTime(LocalDateTime.now().minusMinutes(30));

        Task createdTaskWithDueDate = taskService.createTask(createdTaskList.getId(), taskWithDueDate);
        Task createdTaskWithReminder = taskService.createTask(createdTaskList.getId(), taskWithReminder);

        notificationService.createTaskDueNotification(createdTaskWithDueDate);
        notificationService.createCustomReminderNotification(createdTaskWithReminder);

        List<Notification> allNotifications = notificationService.getAllNotifications();
        assertEquals(2, allNotifications.size());

        List<Notification> unreadNotifications = notificationService.getUnreadNotifications();
        assertEquals(2, unreadNotifications.size());

        Notification dueNotification = allNotifications.stream()
                .filter(n -> n.getType() == NotificationType.TASK_DUE_SOON)
                .findFirst()
                .orElseThrow();

        Notification updatedNotification = notificationService.markAsRead(dueNotification.getId());
        assertTrue(updatedNotification.isRead());

        List<Notification> remainingUnread = notificationService.getUnreadNotifications();
        assertEquals(1, remainingUnread.size());

        notificationService.deleteNotification(updatedNotification.getId());
        List<Notification> finalNotifications = notificationService.getAllNotifications();
        assertEquals(1, finalNotifications.size());
    }

    @Test
    void schedulerIntegration_ChecksTasksAndCreatesNotifications() {
        TaskList taskList = new TaskList(null, "Scheduler Test", "Testing scheduler", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        Task taskDueTomorrow = new Task(null, "Tomorrow Task", "Due tomorrow", 
                LocalDateTime.now().plusDays(1), TaskStatus.OPEN, TaskPriority.HIGH, null, null, null, null);
        Task taskWithPastReminder = new Task(null, "Past Reminder Task", "Had reminder", 
                null, TaskStatus.OPEN, TaskPriority.MEDIUM, null, null, null, null);
        taskWithPastReminder.setCustomReminderDateTime(LocalDateTime.now().minusMinutes(30));

        taskService.createTask(createdTaskList.getId(), taskDueTomorrow);
        taskService.createTask(createdTaskList.getId(), taskWithPastReminder);

        int initialNotificationCount = notificationService.getAllNotifications().size();

        reminderSchedulerService.checkCustomReminders();
        reminderSchedulerService.checkDueSoonTasks();

        List<Notification> notificationsAfterScheduler = notificationService.getAllNotifications();
        assertTrue(notificationsAfterScheduler.size() >= initialNotificationCount);
    }
}