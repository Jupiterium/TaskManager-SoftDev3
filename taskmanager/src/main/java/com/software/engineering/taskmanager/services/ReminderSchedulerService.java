package com.software.engineering.taskmanager.services;

import com.software.engineering.taskmanager.domain.entities.NotificationType;
import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReminderSchedulerService {

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    @Autowired
    public ReminderSchedulerService(TaskRepository taskRepository, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 10000) // Check every 10 seconds
    public void checkCustomReminders() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenSecondsAgo = now.minusSeconds(10);
        
        List<Task> tasksWithReminders = taskRepository.findByCustomReminderDateTimeBetween(tenSecondsAgo, now);
        
        for (Task task : tasksWithReminders) {
            notificationService.createCustomReminderNotification(task);
        }
    }

    @Scheduled(cron = "0 0 9 * * ?") // Check daily at 9 AM for due soon tasks
    public void checkDueSoonTasks() {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime dayAfterTomorrow = tomorrow.plusDays(1);
        
        List<Task> tasksDueSoon = taskRepository.findByDueDateBetween(tomorrow.toLocalDate().atStartOfDay(), 
                                                                     dayAfterTomorrow.toLocalDate().atStartOfDay());
        
        for (Task task : tasksDueSoon) {
            notificationService.createTaskDueNotification(task);
        }
    }


}