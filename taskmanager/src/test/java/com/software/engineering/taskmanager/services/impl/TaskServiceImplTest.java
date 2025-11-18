package com.software.engineering.taskmanager.services.impl;

import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.domain.entities.TaskList;
import com.software.engineering.taskmanager.domain.entities.TaskPriority;
import com.software.engineering.taskmanager.domain.entities.TaskStatus;
import com.software.engineering.taskmanager.repositories.TaskListRepository;
import com.software.engineering.taskmanager.repositories.TaskRepository;
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
class TaskServiceImplTest {
    
    @Mock private TaskRepository taskRepository;
    @Mock private TaskListRepository taskListRepository;
    @InjectMocks private TaskServiceImpl taskService;
    
    private UUID taskListId = UUID.randomUUID();
    private UUID taskId = UUID.randomUUID();
    private TaskList mockTaskList;
    private Task mockTask;
    
    @BeforeEach
    void setUp() {
        mockTaskList = new TaskList(taskListId, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        mockTask = new Task(taskId, "Test Task", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM, mockTaskList, LocalDateTime.now(), LocalDateTime.now(), null);
    }
    
    @Test
    void listTasks_ReturnsTaskList() {
        when(taskRepository.findByTaskListId(taskListId)).thenReturn(List.of(mockTask));
        
        List<Task> result = taskService.listTasks(taskListId);
        
        assertEquals(1, result.size());
        assertEquals(mockTask, result.get(0));
        verify(taskRepository).findByTaskListId(taskListId);
    }
    
    @Test
    void createTask_ValidTask_ReturnsCreatedTask() {
        Task inputTask = new Task(null, "New Task", "Description", null, null, null, null, null, null, null);
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(mockTaskList));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
        
        Task result = taskService.createTask(taskListId, inputTask);
        
        assertEquals(mockTask, result);
        verify(taskRepository).save(any(Task.class));
    }
    
    @Test
    void createTask_TaskWithId_ThrowsException() {
        Task inputTask = new Task(taskId, "Task", "Description", null, null, null, null, null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskService.createTask(taskListId, inputTask));
        
        assertEquals("Task already has an ID", exception.getMessage());
    }
    
    @Test
    void createTask_NullTitle_ThrowsException() {
        Task inputTask = new Task(null, null, "Description", null, null, null, null, null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskService.createTask(taskListId, inputTask));
        
        assertEquals("Task must have a title", exception.getMessage());
    }
    
    @Test
    void createTask_BlankTitle_ThrowsException() {
        Task inputTask = new Task(null, "   ", "Description", null, null, null, null, null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskService.createTask(taskListId, inputTask));
        
        assertEquals("Task must have a title", exception.getMessage());
    }
    
    @Test
    void createTask_InvalidTaskListId_ThrowsException() {
        Task inputTask = new Task(null, "Task", "Description", null, null, null, null, null, null, null);
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskService.createTask(taskListId, inputTask));
        
        assertEquals("Invalid Task List Id provided", exception.getMessage());
    }
    
    @Test
    void createTask_NullPriority_DefaultsToMedium() {
        Task inputTask = new Task(null, "Task", "Description", null, null, null, null, null, null, null);
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(mockTaskList));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
        
        taskService.createTask(taskListId, inputTask);
        
        verify(taskRepository).save(argThat(task -> task.getPriority() == TaskPriority.MEDIUM));
    }
    
    @Test
    void getTask_ReturnsOptionalTask() {
        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(mockTask));
        
        Optional<Task> result = taskService.getTask(taskListId, taskId);
        
        assertTrue(result.isPresent());
        assertEquals(mockTask, result.get());
        verify(taskRepository).findByTaskListIdAndId(taskListId, taskId);
    }
    
    @Test
    void updateTask_ValidTask_ReturnsUpdatedTask() {
        Task updateTask = new Task(taskId, "Updated", "Updated desc", null, TaskStatus.OPEN, TaskPriority.HIGH, null, null, null, null);
        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.of(mockTask));
        when(taskRepository.save(any(Task.class))).thenReturn(mockTask);
        
        Task result = taskService.updateTask(taskListId, taskId, updateTask);
        
        assertEquals(mockTask, result);
        verify(taskRepository).save(mockTask);
    }
    
    @Test
    void updateTask_NullId_ThrowsException() {
        Task updateTask = new Task(null, "Updated", "Updated desc", null, TaskStatus.CLOSED, TaskPriority.HIGH, null, null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskService.updateTask(taskListId, taskId, updateTask));
        
        assertEquals("Task must have an ID", exception.getMessage());
    }
    
    @Test
    void updateTask_MismatchedIds_ThrowsException() {
        UUID differentId = UUID.randomUUID();
        Task updateTask = new Task(differentId, "Updated", "Updated desc", null, TaskStatus.OPEN, TaskPriority.HIGH, null, null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskService.updateTask(taskListId, taskId, updateTask));
        
        assertEquals("Task IDs do not match", exception.getMessage());
    }
    
    @Test
    void updateTask_NullPriority_ThrowsException() {
        Task updateTask = new Task(taskId, "Updated", "Updated desc", null, TaskStatus.CLOSED, null, null, null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskService.updateTask(taskListId, taskId, updateTask));
        
        assertEquals("Task must have a valid priority", exception.getMessage());
    }
    
    @Test
    void updateTask_NullStatus_ThrowsException() {
        Task updateTask = new Task(taskId, "Updated", "Updated desc", null, null, TaskPriority.HIGH, null, null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskService.updateTask(taskListId, taskId, updateTask));
        
        assertEquals("Task must have a valid status", exception.getMessage());
    }
    
    @Test
    void updateTask_TaskNotFound_ThrowsException() {
        Task updateTask = new Task(taskId, "Updated", "Updated desc", null, TaskStatus.CLOSED, TaskPriority.HIGH, null, null, null, null);
        when(taskRepository.findByTaskListIdAndId(taskListId, taskId)).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskService.updateTask(taskListId, taskId, updateTask));
        
        assertEquals("Task not found!", exception.getMessage());
    }
    
    @Test
    void deleteTask_CallsRepository() {
        taskService.deleteTask(taskListId, taskId);
        
        verify(taskRepository).deleteByTaskListIdAndId(taskListId, taskId);
    }
}