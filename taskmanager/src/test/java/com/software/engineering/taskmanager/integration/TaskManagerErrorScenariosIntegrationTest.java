package com.software.engineering.taskmanager.integration;

import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.domain.entities.TaskList;
import com.software.engineering.taskmanager.domain.entities.TaskPriority;
import com.software.engineering.taskmanager.domain.entities.TaskStatus;
import com.software.engineering.taskmanager.services.TaskListService;
import com.software.engineering.taskmanager.services.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TaskManagerErrorScenariosIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskListService taskListService;

    @Test
    void createTask_WithInvalidTaskListId_ThrowsException() {
        UUID invalidTaskListId = UUID.randomUUID();
        Task task = new Task(null, "Test Task", "Description", null, null, TaskPriority.MEDIUM, null, null, null);

        assertThatThrownBy(() -> taskService.createTask(invalidTaskListId, task))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid Task List Id provided");
    }

    @Test
    void createTask_WithExistingId_ThrowsException() {
        TaskList taskList = new TaskList(null, "Test List", "Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        UUID existingId = UUID.randomUUID();
        Task task = new Task(existingId, "Test Task", "Description", null, null, TaskPriority.MEDIUM, null, null, null);

        assertThatThrownBy(() -> taskService.createTask(createdTaskList.getId(), task))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task already has an ID");
    }

    @Test
    void createTask_WithNullTitle_ThrowsException() {
        TaskList taskList = new TaskList(null, "Test List", "Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        Task task = new Task(null, null, "Description", null, null, TaskPriority.MEDIUM, null, null, null);

        assertThatThrownBy(() -> taskService.createTask(createdTaskList.getId(), task))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task must have a title");
    }

    @Test
    void createTask_WithBlankTitle_ThrowsException() {
        TaskList taskList = new TaskList(null, "Test List", "Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        Task task = new Task(null, "   ", "Description", null, null, TaskPriority.MEDIUM, null, null, null);

        assertThatThrownBy(() -> taskService.createTask(createdTaskList.getId(), task))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task must have a title");
    }

    @Test
    void updateTask_WithNonExistentTask_ThrowsException() {
        TaskList taskList = new TaskList(null, "Test List", "Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        UUID nonExistentTaskId = UUID.randomUUID();
        Task updateTask = new Task(nonExistentTaskId, "Updated Task", "Description", null, TaskStatus.OPEN, TaskPriority.HIGH, null, null, null);

        assertThatThrownBy(() -> taskService.updateTask(createdTaskList.getId(), nonExistentTaskId, updateTask))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task not found!");
    }

    @Test
    void updateTask_WithMismatchedIds_ThrowsException() {
        TaskList taskList = new TaskList(null, "Test List", "Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        Task task = new Task(null, "Original Task", "Description", null, null, TaskPriority.MEDIUM, null, null, null);
        Task createdTask = taskService.createTask(createdTaskList.getId(), task);

        UUID differentId = UUID.randomUUID();
        Task updateTask = new Task(differentId, "Updated Task", "Description", null, TaskStatus.OPEN, TaskPriority.HIGH, null, null, null);

        assertThatThrownBy(() -> taskService.updateTask(createdTaskList.getId(), createdTask.getId(), updateTask))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task IDs do not match");
    }

    @Test
    void createTaskList_WithExistingId_ThrowsException() {
        UUID existingId = UUID.randomUUID();
        TaskList taskList = new TaskList(existingId, "Test List", "Description", null, null, null);

        assertThatThrownBy(() -> taskListService.createTaskList(taskList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task list already has an ID!");
    }

    @Test
    void createTaskList_WithNullTitle_ThrowsException() {
        TaskList taskList = new TaskList(null, null, "Description", null, null, null);

        assertThatThrownBy(() -> taskListService.createTaskList(taskList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task list title must be present! ");
    }

    @Test
    void createTaskList_WithBlankTitle_ThrowsException() {
        TaskList taskList = new TaskList(null, "   ", "Description", null, null, null);

        assertThatThrownBy(() -> taskListService.createTaskList(taskList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task list title must be present! ");
    }

    @Test
    void updateTaskList_WithNonExistentTaskList_ThrowsException() {
        UUID nonExistentId = UUID.randomUUID();
        TaskList updateTaskList = new TaskList(nonExistentId, "Updated List", "Description", null, null, null);

        assertThatThrownBy(() -> taskListService.updateTaskList(nonExistentId, updateTaskList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Task list not found!");
    }

    @Test
    void updateTaskList_WithMismatchedIds_ThrowsException() {
        TaskList taskList = new TaskList(null, "Original List", "Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        UUID differentId = UUID.randomUUID();
        TaskList updateTaskList = new TaskList(differentId, "Updated List", "Description", null, null, null);

        assertThatThrownBy(() -> taskListService.updateTaskList(createdTaskList.getId(), updateTaskList))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Attempting to change task list ID, this is not permitted!");
    }
}