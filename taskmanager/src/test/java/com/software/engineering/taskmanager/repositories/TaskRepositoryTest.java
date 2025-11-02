package com.software.engineering.taskmanager.repositories;

import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.domain.entities.TaskList;
import com.software.engineering.taskmanager.domain.entities.TaskPriority;
import com.software.engineering.taskmanager.domain.entities.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskRepositoryTest {
    
    @Autowired private TestEntityManager entityManager;
    @Autowired private TaskRepository taskRepository;
    
    private TaskList taskList;
    private Task task;
    
    @BeforeEach
    void setUp() {
        taskList = new TaskList(null, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        taskList = entityManager.persistAndFlush(taskList);
        
        task = new Task(null, "Test Task", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM, taskList, LocalDateTime.now(), LocalDateTime.now());
        task = entityManager.persistAndFlush(task);
    }
    
    @Test
    void findByTaskListId_ReturnsTasksForTaskList() {
        List<Task> tasks = taskRepository.findByTaskListId(taskList.getId());
        
        assertEquals(1, tasks.size());
        assertEquals(task.getId(), tasks.get(0).getId());
    }
    
    @Test
    void findByTaskListIdAndId_ReturnsTask() {
        Optional<Task> found = taskRepository.findByTaskListIdAndId(taskList.getId(), task.getId());
        
        assertTrue(found.isPresent());
        assertEquals(task.getId(), found.get().getId());
    }
    
    @Test
    void deleteByTaskListIdAndId_DeletesTask() {
        taskRepository.deleteByTaskListIdAndId(taskList.getId(), task.getId());
        entityManager.flush();
        
        Optional<Task> deleted = taskRepository.findByTaskListIdAndId(taskList.getId(), task.getId());
        assertFalse(deleted.isPresent());
    }
}