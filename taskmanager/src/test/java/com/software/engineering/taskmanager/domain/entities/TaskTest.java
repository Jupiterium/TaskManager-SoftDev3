package com.software.engineering.taskmanager.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    
    private Task task;
    private TaskList taskList;
    private LocalDateTime now = LocalDateTime.now();
    
    @BeforeEach
    void setUp() {
        taskList = new TaskList(UUID.randomUUID(), "List", "Desc", null, now, now);
        task = new Task(UUID.randomUUID(), "Task", "Desc", now, TaskStatus.OPEN, TaskPriority.HIGH, taskList, now, now, null);
    }
    
    @Test
    void constructor_CreatesTaskWithAllFields() {
        UUID id = UUID.randomUUID();
        LocalDateTime dueDate = LocalDateTime.now().plusDays(1);
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = LocalDateTime.now();
        
        Task newTask = new Task(id, "Test Task", "Description", dueDate, TaskStatus.OPEN, TaskPriority.MEDIUM, taskList, created, updated, null);
        
        assertEquals(id, newTask.getId());
        assertEquals("Test Task", newTask.getTitle());
        assertEquals("Description", newTask.getDescription());
        assertEquals(dueDate, newTask.getDueDate());
        assertEquals(TaskStatus.OPEN, newTask.getStatus());
        assertEquals(TaskPriority.MEDIUM, newTask.getPriority());
        assertEquals(taskList, newTask.getTaskList());
        assertEquals(created, newTask.getCreated());
        assertEquals(updated, newTask.getUpdated());
    }
    
    @Test
    void setId_UpdatesId() {
        UUID newId = UUID.randomUUID();
        
        task.setId(newId);
        
        assertEquals(newId, task.getId());
    }
    
    @Test
    void setTitle_UpdatesTitle() {
        String newTitle = "Updated Title";
        
        task.setTitle(newTitle);
        
        assertEquals(newTitle, task.getTitle());
    }
    
    @Test
    void setDescription_UpdatesDescription() {
        String newDescription = "Updated Description";
        
        task.setDescription(newDescription);
        
        assertEquals(newDescription, task.getDescription());
    }
    
    @Test
    void setDueDate_UpdatesDueDate() {
        LocalDateTime newDueDate = LocalDateTime.now().plusDays(2);
        
        task.setDueDate(newDueDate);
        
        assertEquals(newDueDate, task.getDueDate());
    }
    
    @Test
    void setStatus_UpdatesStatus() {
        task.setStatus(TaskStatus.OPEN);
        
        assertEquals(TaskStatus.OPEN, task.getStatus());
    }
    
    @Test
    void setPriority_UpdatesPriority() {
        task.setPriority(TaskPriority.LOW);
        
        assertEquals(TaskPriority.LOW, task.getPriority());
    }
    
    @Test
    void setTaskList_UpdatesTaskList() {
        TaskList newTaskList = new TaskList(UUID.randomUUID(), "New List", "New Desc", null, now, now);
        
        task.setTaskList(newTaskList);
        
        assertEquals(newTaskList, task.getTaskList());
    }
    
    @Test
    void setTimestamps_UpdatesTimestamps() {
        LocalDateTime newCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime newUpdated = LocalDateTime.now().plusHours(1);
        
        task.setCreated(newCreated);
        task.setUpdated(newUpdated);
        
        assertEquals(newCreated, task.getCreated());
        assertEquals(newUpdated, task.getUpdated());
    }
    
    @Test
    void setCustomReminderDateTime_UpdatesCustomReminder() {
        LocalDateTime reminderTime = LocalDateTime.now().plusHours(2);
        
        task.setCustomReminderDateTime(reminderTime);
        
        assertEquals(reminderTime, task.getCustomReminderDateTime());
    }
    
    @Test
    void equals_SameObject_ReturnsTrue() {
        assertEquals(task, task);
    }
    
    @Test
    void equals_EqualObjects_ReturnsTrue() {
        Task equalTask = new Task(task.getId(), task.getTitle(), task.getDescription(), task.getDueDate(), 
                                 task.getStatus(), task.getPriority(), task.getTaskList(), task.getCreated(), task.getUpdated(), task.getCustomReminderDateTime());
        
        assertEquals(task, equalTask);
    }
    
    @Test
    void equals_DifferentObjects_ReturnsFalse() {
        Task differentTask = new Task(UUID.randomUUID(), "Different", "Different", now, TaskStatus.OPEN, TaskPriority.LOW, taskList, now, now, null);
        
        assertNotEquals(task, differentTask);
        assertNotEquals(null, task);
        assertNotEquals("string", task);
    }
    
    @Test
    void toString_ReturnsFormattedString() {
        String result = task.toString();
        
        assertTrue(result.contains("Task{"));
        assertTrue(result.contains(task.getTitle()));
        assertTrue(result.contains(task.getDescription()));
    }
}