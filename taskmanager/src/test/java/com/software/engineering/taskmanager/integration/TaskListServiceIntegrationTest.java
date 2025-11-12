package com.software.engineering.taskmanager.integration;

import com.software.engineering.taskmanager.domain.entities.TaskList;
import com.software.engineering.taskmanager.services.TaskListService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class TaskListServiceIntegrationTest {

    @Autowired
    private TaskListService taskListService;

    @Test
    void createTaskList_IntegrationTest() {
        TaskList taskList = new TaskList(null, "Integration TaskList", "Test Description", null, null, null);

        TaskList createdTaskList = taskListService.createTaskList(taskList);

        assertThat(createdTaskList.getId()).isNotNull();
        assertThat(createdTaskList.getTitle()).isEqualTo("Integration TaskList");
        assertThat(createdTaskList.getDescription()).isEqualTo("Test Description");
        assertThat(createdTaskList.getCreated()).isNotNull();
        assertThat(createdTaskList.getUpdated()).isNotNull();
    }

    @Test
    void listTaskList_IntegrationTest() {
        TaskList taskList1 = new TaskList(null, "TaskList 1", "Description 1", null, null, null);
        TaskList taskList2 = new TaskList(null, "TaskList 2", "Description 2", null, null, null);
        
        taskListService.createTaskList(taskList1);
        taskListService.createTaskList(taskList2);

        List<TaskList> taskLists = taskListService.listTaskList();

        assertThat(taskLists).hasSizeGreaterThanOrEqualTo(2);
        assertThat(taskLists).extracting(TaskList::getTitle).contains("TaskList 1", "TaskList 2");
    }

    @Test
    void getTaskList_IntegrationTest() {
        TaskList taskList = new TaskList(null, "Get TaskList Test", "Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        Optional<TaskList> retrievedTaskList = taskListService.getTaskList(createdTaskList.getId());

        assertThat(retrievedTaskList).isPresent();
        assertThat(retrievedTaskList.get().getTitle()).isEqualTo("Get TaskList Test");
        assertThat(retrievedTaskList.get().getId()).isEqualTo(createdTaskList.getId());
    }

    @Test
    void updateTaskList_IntegrationTest() {
        TaskList taskList = new TaskList(null, "Original Title", "Original Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        TaskList updateTaskList = new TaskList(createdTaskList.getId(), "Updated Title", "Updated Description", null, createdTaskList.getCreated(), createdTaskList.getUpdated());
        TaskList updatedTaskList = taskListService.updateTaskList(createdTaskList.getId(), updateTaskList);

        assertThat(updatedTaskList.getTitle()).isEqualTo("Updated Title");
        assertThat(updatedTaskList.getDescription()).isEqualTo("Updated Description");
        assertThat(updatedTaskList.getUpdated()).isAfter(updatedTaskList.getCreated());
    }

    @Test
    void deleteTaskList_IntegrationTest() {
        TaskList taskList = new TaskList(null, "TaskList to Delete", "Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        taskListService.deleteTaskList(createdTaskList.getId());

        Optional<TaskList> deletedTaskList = taskListService.getTaskList(createdTaskList.getId());
        assertThat(deletedTaskList).isEmpty();
    }
}