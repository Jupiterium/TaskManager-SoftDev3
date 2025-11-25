package com.software.engineering.taskmanager.mappers.impl;

import com.software.engineering.taskmanager.domain.dto.TaskDto;
import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.domain.entities.TaskPriority;
import com.software.engineering.taskmanager.domain.entities.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperImplTest {
    
    private final TaskMapperImpl mapper = new TaskMapperImpl();
    
    @Test
    void fromDto_ConvertsTaskDtoToTask() {
        UUID id = UUID.randomUUID();
        LocalDateTime dueDate = LocalDateTime.now();
        LocalDateTime reminderTime = LocalDateTime.now().plusHours(1);
        TaskDto dto = new TaskDto(id, "Title", "Description", dueDate, TaskPriority.HIGH, TaskStatus.OPEN, reminderTime);
        
        Task task = mapper.fromDto(dto);
        
        assertEquals(id, task.getId());
        assertEquals("Title", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertEquals(dueDate, task.getDueDate());
        assertEquals(TaskPriority.HIGH, task.getPriority());
        assertEquals(TaskStatus.OPEN, task.getStatus());
        assertEquals(reminderTime, task.getCustomReminderDateTime());
        assertNull(task.getTaskList());
        assertNull(task.getCreated());
        assertNull(task.getUpdated());
    }
    
    @Test
    void toDto_ConvertsTaskToTaskDto() {
        UUID id = UUID.randomUUID();
        LocalDateTime dueDate = LocalDateTime.now();
        LocalDateTime reminderTime = LocalDateTime.now().plusHours(2);
        Task task = new Task(id, "Title", "Description", dueDate, TaskStatus.CLOSED, TaskPriority.LOW, null, null, null, reminderTime);
        
        TaskDto dto = mapper.toDto(task);
        
        assertEquals(id, dto.id());
        assertEquals("Title", dto.title());
        assertEquals("Description", dto.description());
        assertEquals(dueDate, dto.dueDate());
        assertEquals(TaskPriority.LOW, dto.priority());
        assertEquals(TaskStatus.CLOSED, dto.status());
        assertEquals(reminderTime, dto.customReminderDateTime());
    }
}