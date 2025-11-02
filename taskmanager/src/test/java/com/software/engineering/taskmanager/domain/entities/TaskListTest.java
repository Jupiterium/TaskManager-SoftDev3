package com.software.engineering.taskmanager.domain.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {
    
    private TaskList taskList;
    private LocalDateTime now = LocalDateTime.now();
    
    @BeforeEach
    void setUp() {
        taskList = new TaskList(UUID.randomUUID(), "List", "Desc", new ArrayList<>(), now, now);
    }
    
    @Test
    void constructor_CreatesTaskListWithAllFields() {
        UUID id = UUID.randomUUID();
        List<Task> tasks = new ArrayList<>();
        LocalDateTime created = LocalDateTime.now();
        LocalDateTime updated = LocalDateTime.now();
        
        TaskList newTaskList = new TaskList(id, "Test List", "Description", tasks, created, updated);
        
        assertEquals(id, newTaskList.getId());
        assertEquals("Test List", newTaskList.getTitle());
        assertEquals("Description", newTaskList.getDescription());
        assertEquals(tasks, newTaskList.getTasks());
        assertEquals(created, newTaskList.getCreated());
        assertEquals(updated, newTaskList.getUpdated());
    }
    
    @Test
    void setId_UpdatesId() {
        UUID newId = UUID.randomUUID();
        
        taskList.setId(newId);
        
        assertEquals(newId, taskList.getId());
    }
    
    @Test
    void setTitle_UpdatesTitle() {
        String newTitle = "Updated Title";
        
        taskList.setTitle(newTitle);
        
        assertEquals(newTitle, taskList.getTitle());
    }
    
    @Test
    void setDescription_UpdatesDescription() {
        String newDescription = "Updated Description";
        
        taskList.setDescription(newDescription);
        
        assertEquals(newDescription, taskList.getDescription());
    }
    
    @Test
    void setTasks_UpdatesTasks() {
        List<Task> newTasks = new ArrayList<>();
        Task task = new Task(UUID.randomUUID(), "Task", "Desc", now, TaskStatus.OPEN, TaskPriority.MEDIUM, taskList, now, now);
        newTasks.add(task);
        
        taskList.setTasks(newTasks);
        
        assertEquals(newTasks, taskList.getTasks());
        assertEquals(1, taskList.getTasks().size());
    }
    
    @Test
    void setTimestamps_UpdatesTimestamps() {
        LocalDateTime newCreated = LocalDateTime.now().minusDays(1);
        LocalDateTime newUpdated = LocalDateTime.now().plusHours(1);
        
        taskList.setCreated(newCreated);
        taskList.setUpdated(newUpdated);
        
        assertEquals(newCreated, taskList.getCreated());
        assertEquals(newUpdated, taskList.getUpdated());
    }
    
    @Test
    void equals_SameObject_ReturnsTrue() {
        assertTrue(taskList.equals(taskList));
    }
    
    @Test
    void equals_EqualObjects_ReturnsTrue() {
        TaskList equalTaskList = new TaskList(taskList.getId(), taskList.getTitle(), taskList.getDescription(), 
                                             taskList.getTasks(), taskList.getCreated(), taskList.getUpdated());
        
        assertTrue(taskList.equals(equalTaskList));
    }
    
    @Test
    void equals_DifferentObjects_ReturnsFalse() {
        TaskList differentTaskList = new TaskList(UUID.randomUUID(), "Different", "Different", new ArrayList<>(), now, now);
        
        assertFalse(taskList.equals(differentTaskList));
        assertFalse(taskList.equals(null));
        assertFalse(taskList.equals("string"));
    }
    
    @Test
    void toString_ReturnsFormattedString() {
        String result = taskList.toString();
        
        assertTrue(result.contains("TaskList{"));
        assertTrue(result.contains(taskList.getTitle()));
        assertTrue(result.contains(taskList.getDescription()));
    }
}