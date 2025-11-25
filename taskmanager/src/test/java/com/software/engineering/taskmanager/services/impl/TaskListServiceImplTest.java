package com.software.engineering.taskmanager.services.impl;

import com.software.engineering.taskmanager.domain.entities.TaskList;
import com.software.engineering.taskmanager.repositories.TaskListRepository;
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
class TaskListServiceImplTest {
    
    @Mock private TaskListRepository taskListRepository;
    @InjectMocks private TaskListServiceImpl taskListService;
    
    private UUID taskListId = UUID.randomUUID();
    private TaskList mockTaskList;
    
    @BeforeEach
    void setUp() {
        mockTaskList = new TaskList(taskListId, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
    }
    
    @Test
    void listTaskList_ReturnsAllTaskLists() {
        when(taskListRepository.findAll()).thenReturn(List.of(mockTaskList));
        
        List<TaskList> result = taskListService.listTaskList();
        
        assertEquals(1, result.size());
        assertEquals(mockTaskList, result.get(0));
        verify(taskListRepository).findAll();
    }
    
    @Test
    void createTaskList_ValidTaskList_ReturnsCreated() {
        TaskList inputTaskList = new TaskList(null, "New List", "Description", null, null, null);
        when(taskListRepository.save(any(TaskList.class))).thenReturn(mockTaskList);
        
        TaskList result = taskListService.createTaskList(inputTaskList);
        
        assertEquals(mockTaskList, result);
        verify(taskListRepository).save(any(TaskList.class));
    }
    
    @Test
    void createTaskList_TaskListWithId_ThrowsException() {
        TaskList inputTaskList = new TaskList(taskListId, "List", "Description", null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskListService.createTaskList(inputTaskList));
        
        assertEquals("Task list already has an ID!", exception.getMessage());
    }
    
    @Test
    void createTaskList_NullTitle_ThrowsException() {
        TaskList inputTaskList = new TaskList(null, null, "Description", null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskListService.createTaskList(inputTaskList));
        
        assertEquals("Task list title must be present!", exception.getMessage());
    }
    
    @Test
    void createTaskList_BlankTitle_ThrowsException() {
        TaskList inputTaskList = new TaskList(null, "   ", "Description", null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskListService.createTaskList(inputTaskList));
        
        assertEquals("Task list title must be present!", exception.getMessage());
    }
    
    @Test
    void getTaskList_ReturnsOptionalTaskList() {
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(mockTaskList));
        
        Optional<TaskList> result = taskListService.getTaskList(taskListId);
        
        assertTrue(result.isPresent());
        assertEquals(mockTaskList, result.get());
        verify(taskListRepository).findById(taskListId);
    }
    
    @Test
    void updateTaskList_ValidTaskList_ReturnsUpdated() {
        TaskList updateTaskList = new TaskList(taskListId, "Updated", "Updated desc", null, null, null);
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.of(mockTaskList));
        when(taskListRepository.save(any(TaskList.class))).thenReturn(mockTaskList);
        
        TaskList result = taskListService.updateTaskList(taskListId, updateTaskList);
        
        assertEquals(mockTaskList, result);
        verify(taskListRepository).save(mockTaskList);
    }
    
    @Test
    void updateTaskList_NullId_ThrowsException() {
        TaskList updateTaskList = new TaskList(taskListId, "Updated", "Updated desc", null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskListService.updateTaskList(null, updateTaskList));
        
        assertEquals("Task list must have an ID!", exception.getMessage());
    }
    
    @Test
    void updateTaskList_MismatchedIds_ThrowsException() {
        UUID differentId = UUID.randomUUID();
        TaskList updateTaskList = new TaskList(differentId, "Updated", "Updated desc", null, null, null);
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskListService.updateTaskList(taskListId, updateTaskList));
        
        assertEquals("Attempting to change task list ID, this is not permitted!", exception.getMessage());
    }
    
    @Test
    void updateTaskList_TaskListNotFound_ThrowsException() {
        TaskList updateTaskList = new TaskList(taskListId, "Updated", "Updated desc", null, null, null);
        when(taskListRepository.findById(taskListId)).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, 
            () -> taskListService.updateTaskList(taskListId, updateTaskList));
        
        assertEquals("Task list not found!", exception.getMessage());
    }
    
    @Test
    void deleteTaskList_CallsRepository() {
        taskListService.deleteTaskList(taskListId);
        
        verify(taskListRepository).deleteById(taskListId);
    }
}