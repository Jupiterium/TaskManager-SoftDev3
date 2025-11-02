package com.software.engineering.taskmanager.services.impl;

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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TaskServiceIntegrationTest {
    
    @Autowired private TaskService taskService;
    @Autowired private TaskListRepository taskListRepository;
    
    private TaskList savedTaskList;
    
    @BeforeEach
    void setUp() {
        TaskList taskList = new TaskList(null, "Integration Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        savedTaskList = taskListRepository.save(taskList);
    }
    
    @Test
    void createTask_IntegrationTest() {
        Task inputTask = new Task(null, "Integration Task", "Description", null, null, TaskPriority.HIGH, null, null, null);
        
        Task createdTask = taskService.createTask(savedTaskList.getId(), inputTask);
        
        assertNotNull(createdTask.getId());
        assertEquals("Integration Task", createdTask.getTitle());
        assertEquals(TaskStatus.OPEN, createdTask.getStatus());
        assertEquals(TaskPriority.HIGH, createdTask.getPriority());
        assertNotNull(createdTask.getCreated());
        assertNotNull(createdTask.getUpdated());
    }
    
    @Test
    void listTasks_IntegrationTest() {
        Task task1 = new Task(null, "Task 1", "Description 1", null, null, TaskPriority.HIGH, null, null, null);
        Task task2 = new Task(null, "Task 2", "Description 2", null, null, TaskPriority.LOW, null, null, null);
        
        taskService.createTask(savedTaskList.getId(), task1);
        taskService.createTask(savedTaskList.getId(), task2);
        
        List<Task> tasks = taskService.listTasks(savedTaskList.getId());
        
        assertEquals(2, tasks.size());
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Task 1")));
        assertTrue(tasks.stream().anyMatch(t -> t.getTitle().equals("Task 2")));
    }
    
    @Test
    void getTask_IntegrationTest() {
        Task inputTask = new Task(null, "Get Task Test", "Description", null, null, TaskPriority.MEDIUM, null, null, null);
        Task createdTask = taskService.createTask(savedTaskList.getId(), inputTask);
        
        Optional<Task> retrievedTask = taskService.getTask(savedTaskList.getId(), createdTask.getId());
        
        assertTrue(retrievedTask.isPresent());
        assertEquals(createdTask.getId(), retrievedTask.get().getId());
        assertEquals("Get Task Test", retrievedTask.get().getTitle());
    }
    
    @Test
    void updateTask_IntegrationTest() {
        Task inputTask = new Task(null, "Original Task", "Original Description", null, null, TaskPriority.LOW, null, null, null);
        Task createdTask = taskService.createTask(savedTaskList.getId(), inputTask);
        
        Task updateTask = new Task(createdTask.getId(), "Updated Task", "Updated Description", 
                                  LocalDateTime.now().plusDays(1), TaskStatus.OPEN, TaskPriority.HIGH, null, null, null);
        
        Task updatedTask = taskService.updateTask(savedTaskList.getId(), createdTask.getId(), updateTask);
        
        assertEquals("Updated Task", updatedTask.getTitle());
        assertEquals("Updated Description", updatedTask.getDescription());
        assertEquals(TaskStatus.OPEN, updatedTask.getStatus());
        assertEquals(TaskPriority.HIGH, updatedTask.getPriority());
        assertNotNull(updatedTask.getDueDate());
    }
    
    @Test
    void deleteTask_IntegrationTest() {
        Task inputTask = new Task(null, "Delete Task Test", "Description", null, null, TaskPriority.MEDIUM, null, null, null);
        Task createdTask = taskService.createTask(savedTaskList.getId(), inputTask);
        
        taskService.deleteTask(savedTaskList.getId(), createdTask.getId());
        
        Optional<Task> deletedTask = taskService.getTask(savedTaskList.getId(), createdTask.getId());
        assertFalse(deletedTask.isPresent());
    }
}