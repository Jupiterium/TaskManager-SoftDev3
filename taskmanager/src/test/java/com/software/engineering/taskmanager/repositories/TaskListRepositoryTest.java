package com.software.engineering.taskmanager.repositories;

import com.software.engineering.taskmanager.domain.entities.TaskList;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TaskListRepositoryTest {
    
    @Autowired private TestEntityManager entityManager;
    @Autowired private TaskListRepository taskListRepository;
    
    @Test
    void save_PersistsTaskList() {
        TaskList taskList = new TaskList(null, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        
        TaskList saved = taskListRepository.save(taskList);
        
        assertNotNull(saved.getId());
        assertEquals("Test List", saved.getTitle());
    }
    
    @Test
    void findAll_ReturnsAllTaskLists() {
        TaskList taskList1 = new TaskList(null, "List 1", "Desc 1", null, LocalDateTime.now(), LocalDateTime.now());
        TaskList taskList2 = new TaskList(null, "List 2", "Desc 2", null, LocalDateTime.now(), LocalDateTime.now());
        
        entityManager.persistAndFlush(taskList1);
        entityManager.persistAndFlush(taskList2);
        
        List<TaskList> taskLists = taskListRepository.findAll();
        
        assertEquals(2, taskLists.size());
    }
    
    @Test
    void findById_ReturnsTaskList() {
        TaskList taskList = new TaskList(null, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        TaskList saved = entityManager.persistAndFlush(taskList);
        
        Optional<TaskList> found = taskListRepository.findById(saved.getId());
        
        assertTrue(found.isPresent());
        assertEquals(saved.getId(), found.get().getId());
    }
    
    @Test
    void deleteById_RemovesTaskList() {
        TaskList taskList = new TaskList(null, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        TaskList saved = entityManager.persistAndFlush(taskList);
        
        taskListRepository.deleteById(saved.getId());
        entityManager.flush();
        
        Optional<TaskList> deleted = taskListRepository.findById(saved.getId());
        assertFalse(deleted.isPresent());
    }
}