package com.software.engineering.taskmanager.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.software.engineering.taskmanager.domain.dto.TaskListDto;
import com.software.engineering.taskmanager.domain.entities.TaskList;
import com.software.engineering.taskmanager.mappers.TaskListMapper;
import com.software.engineering.taskmanager.services.TaskListService;
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

@WebMvcTest(TaskListController.class)
class TaskListControllerTest {
    
    @Autowired private MockMvc mockMvc;
    @MockitoBean private TaskListService taskListService;
    @MockitoBean private TaskListMapper taskListMapper;
    @Autowired private ObjectMapper objectMapper;
    
    private UUID taskListId = UUID.randomUUID();
    private TaskListDto mockTaskListDto;
    private TaskList mockTaskList;
    
    @BeforeEach
    void setUp() {
        mockTaskListDto = new TaskListDto(taskListId, "Test List", "Description", 0, 0.0, List.of());
        mockTaskList = new TaskList(taskListId, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
    }
    
    @Test
    void listTasksLists_ReturnsAllTaskLists() throws Exception {
        when(taskListService.listTaskList()).thenReturn(List.of(mockTaskList));
        when(taskListMapper.toDto(mockTaskList)).thenReturn(mockTaskListDto);
        
        mockMvc.perform(get("/task-lists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(taskListId.toString()))
                .andExpect(jsonPath("$[0].title").value("Test List"));
        
        verify(taskListService).listTaskList();
    }
    
    @Test
    void createTaskList_ReturnsCreatedTaskList() throws Exception {
        when(taskListMapper.fromDto(any(TaskListDto.class))).thenReturn(mockTaskList);
        when(taskListService.createTaskList(any(TaskList.class))).thenReturn(mockTaskList);
        when(taskListMapper.toDto(mockTaskList)).thenReturn(mockTaskListDto);
        
        mockMvc.perform(post("/task-lists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockTaskListDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskListId.toString()))
                .andExpect(jsonPath("$.title").value("Test List"));
        
        verify(taskListService).createTaskList(any(TaskList.class));
    }
    
    @Test
    void getTaskList_ExistingTaskList_ReturnsTaskList() throws Exception {
        when(taskListService.getTaskList(taskListId)).thenReturn(Optional.of(mockTaskList));
        when(taskListMapper.toDto(mockTaskList)).thenReturn(mockTaskListDto);
        
        mockMvc.perform(get("/task-lists/{task_list_id}", taskListId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskListId.toString()))
                .andExpect(jsonPath("$.title").value("Test List"));
        
        verify(taskListService).getTaskList(taskListId);
    }
    
    @Test
    void getTaskList_NonExistingTaskList_ReturnsEmpty() throws Exception {
        when(taskListService.getTaskList(taskListId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/task-lists/{task_list_id}", taskListId))
                .andExpect(status().isOk())
                .andExpect(content().string("null"));

        verify(taskListService).getTaskList(taskListId);
    }
    @Test
    void updateTaskList_ReturnsUpdatedTaskList() throws Exception {
        when(taskListMapper.fromDto(any(TaskListDto.class))).thenReturn(mockTaskList);
        when(taskListService.updateTaskList(eq(taskListId), any(TaskList.class))).thenReturn(mockTaskList);
        when(taskListMapper.toDto(mockTaskList)).thenReturn(mockTaskListDto);
        
        mockMvc.perform(put("/task-lists/{task_list_id}", taskListId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(mockTaskListDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(taskListId.toString()))
                .andExpect(jsonPath("$.title").value("Test List"));
        
        verify(taskListService).updateTaskList(eq(taskListId), any(TaskList.class));
    }
    
    @Test
    void deleteTaskList_CallsService() throws Exception {
        mockMvc.perform(delete("/task-lists/{task_list_id}", taskListId))
                .andExpect(status().isOk());
        
        verify(taskListService).deleteTaskList(taskListId);
    }
}