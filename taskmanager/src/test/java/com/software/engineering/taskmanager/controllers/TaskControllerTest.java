package com.software.engineering.taskmanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.engineering.taskmanager.domain.dto.TaskDto;
import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.domain.entities.TaskPriority;
import com.software.engineering.taskmanager.domain.entities.TaskStatus;
import com.software.engineering.taskmanager.mappers.TaskMapper;
import com.software.engineering.taskmanager.services.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    
    @Autowired private MockMvc mockMvc;
    @MockitoBean private TaskService taskService;
    @MockitoBean private TaskMapper taskMapper;
    @Autowired private ObjectMapper objectMapper;
    
    private UUID taskListId = UUID.randomUUID();
    private UUID taskId = UUID.randomUUID();
    private TaskDto mockTaskDto;
    private Task mockTask;
    
    @BeforeEach
    void setUp() {
        mockTaskDto = new TaskDto(taskId, "Test Task", "Description", null, TaskPriority.MEDIUM, TaskStatus.OPEN, null);
        mockTask = new Task(taskId, "Test Task", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM, null, LocalDateTime.now(), LocalDateTime.now(), null);
    }
    
    @Test
    void listTasks_ReturnsTaskList() throws Exception {
        when(taskService.listTasks(taskListId)).thenReturn(List.of(mockTask));
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);
        
        mockMvc.perform(get("/task-lists/{task_list_id}/tasks", taskListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(taskId.toString()))
                .andExpect(jsonPath("$[0].title").value("Test Task"));
        
        verify(taskService).listTasks(taskListId);
    }
    
    @Test
    void createTask_ReturnsCreatedTask() throws Exception {
        when(taskMapper.fromDto(any(TaskDto.class))).thenReturn(mockTask);
        when(taskService.createTask(eq(taskListId), any(Task.class))).thenReturn(mockTask);
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);
        
        mockMvc.perform(post("/task-lists/{task_list_id}/tasks", taskListId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockTaskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value("Test Task"));
        
        verify(taskService).createTask(eq(taskListId), any(Task.class));
    }
    
    @Test
    void getTask_ExistingTask_ReturnsTask() throws Exception {
        when(taskService.getTask(taskListId, taskId)).thenReturn(Optional.of(mockTask));
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);
        
        mockMvc.perform(get("/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value("Test Task"));
        
        verify(taskService).getTask(taskListId, taskId);
    }
    
    @Test
    void getTask_NonExistingTask_ReturnsEmpty() throws Exception {
        when(taskService.getTask(taskListId, taskId)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId))
                .andExpect(status().isOk())
                .andExpect(content().string("null"));
        
        verify(taskService).getTask(taskListId, taskId);
    }
    
    @Test
    void updateTask_ReturnsUpdatedTask() throws Exception {
        when(taskMapper.fromDto(any(TaskDto.class))).thenReturn(mockTask);
        when(taskService.updateTask(eq(taskListId), eq(taskId), any(Task.class))).thenReturn(mockTask);
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);
        
        mockMvc.perform(put("/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockTaskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskId.toString()))
                .andExpect(jsonPath("$.title").value("Test Task"));
        
        verify(taskService).updateTask(eq(taskListId), eq(taskId), any(Task.class));
    }
    
    @Test
    void deleteTask_CallsService() throws Exception {
        mockMvc.perform(delete("/task-lists/{task_list_id}/tasks/{task_id}", taskListId, taskId))
                .andExpect(status().isOk());
        
        verify(taskService).deleteTask(taskListId, taskId);
    }
    
    @Test
    void setCustomReminder_ReturnsUpdatedTask() throws Exception {
        LocalDateTime reminderTime = LocalDateTime.now().plusHours(2);
        when(taskService.getTask(taskListId, taskId)).thenReturn(Optional.of(mockTask));
        when(taskService.updateTask(eq(taskListId), eq(taskId), any(Task.class))).thenReturn(mockTask);
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);
        
        mockMvc.perform(put("/task-lists/{task_list_id}/tasks/{task_id}/reminder", taskListId, taskId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reminderTime)))
                .andExpect(status().isOk());
        
        verify(taskService).getTask(taskListId, taskId);
        verify(taskService).updateTask(eq(taskListId), eq(taskId), any(Task.class));
    }
    
    @Test
    void removeCustomReminder_ReturnsUpdatedTask() throws Exception {
        when(taskService.getTask(taskListId, taskId)).thenReturn(Optional.of(mockTask));
        when(taskService.updateTask(eq(taskListId), eq(taskId), any(Task.class))).thenReturn(mockTask);
        when(taskMapper.toDto(mockTask)).thenReturn(mockTaskDto);
        
        mockMvc.perform(delete("/task-lists/{task_list_id}/tasks/{task_id}/reminder", taskListId, taskId))
                .andExpect(status().isOk());
        
        verify(taskService).getTask(taskListId, taskId);
        verify(taskService).updateTask(eq(taskListId), eq(taskId), any(Task.class));
    }
}