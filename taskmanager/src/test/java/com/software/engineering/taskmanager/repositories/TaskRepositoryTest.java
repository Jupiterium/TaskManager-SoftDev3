package com.software.engineering.taskmanager.repositories;

import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.domain.entities.TaskList;
import com.software.engineering.taskmanager.domain.entities.TaskPriority;
import com.software.engineering.taskmanager.domain.entities.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskRepositoryTest {
    
    @Autowired private TestEntityManager entityManager;
    @Autowired private TaskRepository taskRepository;
    
    private TaskList taskList;
    private Task task;
    
    @BeforeEach
    void setUp() {
        taskList = new TaskList(null, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        taskList = entityManager.persistAndFlush(taskList);
        
        task = new Task(null, "Test Task", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM, taskList, LocalDateTime.now(), LocalDateTime.now(), null);
        task = entityManager.persistAndFlush(task);
    }
    
    @Test
    void findByTaskListId_ReturnsTasksForTaskList() {
        List<Task> tasks = taskRepository.findByTaskListId(taskList.getId());
        
        assertEquals(1, tasks.size());
        assertEquals(task.getId(), tasks.get(0).getId());
    }
    
    @Test
    void findByTaskListIdAndId_ReturnsTask() {
        Optional<Task> found = taskRepository.findByTaskListIdAndId(taskList.getId(), task.getId());
        
        assertTrue(found.isPresent());
        assertEquals(task.getId(), found.get().getId());
    }
    
    @Test
    void deleteByTaskListIdAndId_DeletesTask() {
        taskRepository.deleteByTaskListIdAndId(taskList.getId(), task.getId());
        entityManager.flush();
        
        Optional<Task> deleted = taskRepository.findByTaskListIdAndId(taskList.getId(), task.getId());
        assertFalse(deleted.isPresent());
    }

    @Test
    void findByCustomReminderDateTimeBetween_ReturnsTasksWithRemindersInRange() {
        LocalDateTime reminderTime = LocalDateTime.now().plusHours(1);
        Task taskWithReminder = new Task(null, "Reminder Task", "Has reminder", null, TaskStatus.OPEN, TaskPriority.HIGH, taskList, LocalDateTime.now(), LocalDateTime.now(), null);
        taskWithReminder.setCustomReminderDateTime(reminderTime);
        entityManager.persistAndFlush(taskWithReminder);

        LocalDateTime start = reminderTime.minusMinutes(30);
        LocalDateTime end = reminderTime.plusMinutes(30);
        
        List<Task> tasksWithReminders = taskRepository.findByCustomReminderDateTimeBetween(start, end);
        
        assertEquals(1, tasksWithReminders.size());
        assertEquals(taskWithReminder.getId(), tasksWithReminders.get(0).getId());
    }

    @Test
    void findByDueDateBetween_ReturnsTasksWithDueDatesInRange() {
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        Task taskWithDueDate = new Task(null, "Due Task", "Has due date", dueDate, TaskStatus.OPEN, TaskPriority.MEDIUM, taskList, LocalDateTime.now(), LocalDateTime.now(), null);
        entityManager.persistAndFlush(taskWithDueDate);

        LocalDateTime start = dueDate.minusHours(12);
        LocalDateTime end = dueDate.plusHours(12);
        
        List<Task> tasksWithDueDates = taskRepository.findByDueDateBetween(start, end);
        
        assertEquals(1, tasksWithDueDates.size());
        assertEquals(taskWithDueDate.getId(), tasksWithDueDates.get(0).getId());
    }
}