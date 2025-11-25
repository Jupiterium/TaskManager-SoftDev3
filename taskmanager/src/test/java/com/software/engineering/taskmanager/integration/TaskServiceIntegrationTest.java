package com.software.engineering.taskmanager.integration;

import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.domain.entities.TaskList;
import com.software.engineering.taskmanager.domain.entities.TaskPriority;
import com.software.engineering.taskmanager.domain.entities.TaskStatus;
import com.software.engineering.taskmanager.repositories.TaskListRepository;
import com.software.engineering.taskmanager.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TaskServiceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskListRepository taskListRepository;

    private TaskList savedTaskList;

    @BeforeEach
    void setUp() {
        TaskList taskList = new TaskList(null, "Integration Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        savedTaskList = taskListRepository.save(taskList);
    }

    @Test
    void createTask_IntegrationTest() {
        Task task = new Task(null, "Integration Task", "Test Description", null, null, TaskPriority.HIGH, null, null, null, null);

        Task createdTask = taskService.createTask(savedTaskList.getId(), task);

        assertThat(createdTask.getId()).isNotNull();
        assertThat(createdTask.getTitle()).isEqualTo("Integration Task");
        assertThat(createdTask.getStatus()).isEqualTo(TaskStatus.OPEN);
        assertThat(createdTask.getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(createdTask.getTaskList().getId()).isEqualTo(savedTaskList.getId());
    }

    @Test
    void listTasks_IntegrationTest() {
        Task task1 = new Task(null, "Task 1", "Description 1", null, null, TaskPriority.HIGH, null, null, null, null);
        Task task2 = new Task(null, "Task 2", "Description 2", null, null, TaskPriority.LOW, null, null, null, null);
        
        taskService.createTask(savedTaskList.getId(), task1);
        taskService.createTask(savedTaskList.getId(), task2);

        List<Task> tasks = taskService.listTasks(savedTaskList.getId());

        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting(Task::getTitle).containsExactlyInAnyOrder("Task 1", "Task 2");
    }

    @Test
    void getTask_IntegrationTest() {
        Task task = new Task(null, "Get Task Test", "Description", null, null, TaskPriority.MEDIUM, null, null, null, null);
        Task createdTask = taskService.createTask(savedTaskList.getId(), task);

        Optional<Task> retrievedTask = taskService.getTask(savedTaskList.getId(), createdTask.getId());

        assertThat(retrievedTask).isPresent();
        assertThat(retrievedTask.get().getTitle()).isEqualTo("Get Task Test");
        assertThat(retrievedTask.get().getId()).isEqualTo(createdTask.getId());
    }

    @Test
    void updateTask_IntegrationTest() {
        Task task = new Task(null, "Original Title", "Original Description", null, null, TaskPriority.LOW, null, null, null, null);
        Task createdTask = taskService.createTask(savedTaskList.getId(), task);

        Task updateTask = new Task(createdTask.getId(), "Updated Title", "Updated Description", LocalDateTime.now().plusDays(1), TaskStatus.CLOSED, TaskPriority.HIGH, null, null, null, null);
        Task updatedTask = taskService.updateTask(savedTaskList.getId(), createdTask.getId(), updateTask);

        assertThat(updatedTask.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedTask.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedTask.getStatus()).isEqualTo(TaskStatus.CLOSED);
        assertThat(updatedTask.getPriority()).isEqualTo(TaskPriority.HIGH);
        assertThat(updatedTask.getDueDate()).isNotNull();
    }

    @Test
    void deleteTask_IntegrationTest() {
        Task task = new Task(null, "Task to Delete", "Description", null, null, TaskPriority.MEDIUM, null, null, null, null);
        Task createdTask = taskService.createTask(savedTaskList.getId(), task);

        taskService.deleteTask(savedTaskList.getId(), createdTask.getId());

        Optional<Task> deletedTask = taskService.getTask(savedTaskList.getId(), createdTask.getId());
        assertThat(deletedTask).isEmpty();
    }
    
    @Test
    void createTaskWithCustomReminder_IntegrationTest() {
        LocalDateTime reminderTime = LocalDateTime.now().plusHours(2);
        Task task = new Task(null, "Task with Reminder", "Has custom reminder", null, null, TaskPriority.MEDIUM, null, null, null, reminderTime);

        Task createdTask = taskService.createTask(savedTaskList.getId(), task);

        assertThat(createdTask.getCustomReminderDateTime()).isEqualTo(reminderTime);
        
        Optional<Task> retrievedTask = taskService.getTask(savedTaskList.getId(), createdTask.getId());
        assertThat(retrievedTask).isPresent();
        assertThat(retrievedTask.get().getCustomReminderDateTime()).isEqualTo(reminderTime);
    }
    
    @Test
    void updateTaskCustomReminder_IntegrationTest() {
        Task task = new Task(null, "Task for Reminder Update", "Description", null, null, TaskPriority.LOW, null, null, null, null);
        Task createdTask = taskService.createTask(savedTaskList.getId(), task);
        
        LocalDateTime reminderTime = LocalDateTime.now().plusHours(3);
        Task updateTask = new Task(createdTask.getId(), "Updated Task", "Updated Description", null, TaskStatus.OPEN, TaskPriority.LOW, null, null, null, reminderTime);
        Task updatedTask = taskService.updateTask(savedTaskList.getId(), createdTask.getId(), updateTask);

        assertThat(updatedTask.getCustomReminderDateTime()).isEqualTo(reminderTime);
        
        // Remove reminder
        Task removeReminderTask = new Task(createdTask.getId(), "Updated Task", "Updated Description", null, TaskStatus.OPEN, TaskPriority.LOW, null, null, null, null);
        Task taskWithoutReminder = taskService.updateTask(savedTaskList.getId(), createdTask.getId(), removeReminderTask);
        
        assertThat(taskWithoutReminder.getCustomReminderDateTime()).isNull();
    }
}