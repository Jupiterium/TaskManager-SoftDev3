package com.software.engineering.taskmanager.mappers.impl;

import com.software.engineering.taskmanager.domain.dto.TaskDto;
import com.software.engineering.taskmanager.domain.dto.TaskListDto;
import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.domain.entities.TaskList;
import com.software.engineering.taskmanager.domain.entities.TaskPriority;
import com.software.engineering.taskmanager.domain.entities.TaskStatus;
import com.software.engineering.taskmanager.mappers.TaskMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskListMapperImplTest {
    
    @Mock private TaskMapper taskMapper;
    private TaskListMapperImpl mapper;
    
    @BeforeEach
    void setUp() {
        mapper = new TaskListMapperImpl(taskMapper);
    }
    
    @Test
    void fromDto_ConvertsTaskListDtoToTaskList() {
        UUID id = UUID.randomUUID();
        TaskDto taskDto = new TaskDto(UUID.randomUUID(), "Task", "Desc", null, TaskPriority.MEDIUM, TaskStatus.OPEN, null);
        Task task = new Task(taskDto.id(), "Task", "Desc", null, TaskStatus.OPEN, TaskPriority.MEDIUM, null, null, null, null);
        TaskListDto dto = new TaskListDto(id, "List", "Description", 1, 0.5, List.of(taskDto));
        
        when(taskMapper.fromDto(taskDto)).thenReturn(task);
        
        TaskList taskList = mapper.fromDto(dto);
        
        assertEquals(id, taskList.getId());
        assertEquals("List", taskList.getTitle());
        assertEquals("Description", taskList.getDescription());
        assertEquals(1, taskList.getTasks().size());
        assertEquals(task, taskList.getTasks().get(0));
        assertNull(taskList.getCreated());
        assertNull(taskList.getUpdated());
    }
    
    @Test
    void toDto_ConvertsTaskListToTaskListDto() {
        UUID id = UUID.randomUUID();
        Task closedTask = new Task(UUID.randomUUID(), "Task1", "Desc", null, TaskStatus.CLOSED, TaskPriority.HIGH, null, null, null, null);
        Task openTask = new Task(UUID.randomUUID(), "Task2", "Desc", null, TaskStatus.OPEN, TaskPriority.LOW, null, null, null, null);
        TaskList taskList = new TaskList(id, "List", "Description", List.of(closedTask, openTask), LocalDateTime.now(), LocalDateTime.now());
        
        TaskDto closedDto = new TaskDto(closedTask.getId(), "Task1", "Desc", null, TaskPriority.HIGH, TaskStatus.CLOSED, null);
        TaskDto openDto = new TaskDto(openTask.getId(), "Task2", "Desc", null, TaskPriority.LOW, TaskStatus.OPEN, null);
        
        when(taskMapper.toDto(closedTask)).thenReturn(closedDto);
        when(taskMapper.toDto(openTask)).thenReturn(openDto);
        
        TaskListDto dto = mapper.toDto(taskList);
        
        assertEquals(id, dto.id());
        assertEquals("List", dto.title());
        assertEquals("Description", dto.description());
        assertEquals(2, dto.count());
        assertEquals(0.5, dto.progress());
        assertEquals(2, dto.tasks().size());
    }
    
    @Test
    void toDto_NullTasks_HandlesGracefully() {
        UUID id = UUID.randomUUID();
        TaskList taskList = new TaskList(id, "List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        
        TaskListDto dto = mapper.toDto(taskList);
        
        assertEquals(0, dto.count());
        assertNull(dto.progress());
        assertNull(dto.tasks());
    }
}