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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskManagerWorkflowIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskListService taskListService;

    @Test
    @Transactional
    void completeWorkflow_CreateTaskListAndTasks_IntegrationTest() {
        // Create TaskList
        TaskList taskList = new TaskList(null, "Project Alpha", "Main project tasks", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        // Create multiple tasks
        Task task1 = new Task(null, "Setup Database", "Configure database schema", LocalDateTime.now().plusDays(3), null, TaskPriority.HIGH, null, null, null);
        Task task2 = new Task(null, "Implement API", "Create REST endpoints", LocalDateTime.now().plusDays(7), null, TaskPriority.MEDIUM, null, null, null);
        Task task3 = new Task(null, "Write Tests", "Unit and integration tests", null, null, null, null, null, null);

        Task createdTask1 = taskService.createTask(createdTaskList.getId(), task1);
        Task createdTask2 = taskService.createTask(createdTaskList.getId(), task2);
        Task createdTask3 = taskService.createTask(createdTaskList.getId(), task3);

        // Verify tasks were created
        List<Task> tasks = taskService.listTasks(createdTaskList.getId());
        assertThat(tasks).hasSize(3);
        assertThat(tasks).extracting(Task::getTitle).containsExactlyInAnyOrder("Setup Database", "Implement API", "Write Tests");

        // Update task status
        Task updateTask1 = new Task(createdTask1.getId(), "Setup Database", "Database configured successfully", createdTask1.getDueDate(), TaskStatus.CLOSED, TaskPriority.HIGH, null, null, null);
        Task updatedTask1 = taskService.updateTask(createdTaskList.getId(), createdTask1.getId(), updateTask1);

        assertThat(updatedTask1.getStatus()).isEqualTo(TaskStatus.CLOSED);
        assertThat(updatedTask1.getDescription()).isEqualTo("Database configured successfully");

        // Update TaskList
        TaskList updateTaskList = new TaskList(createdTaskList.getId(), "Project Alpha - Phase 1", "Updated project description", null, createdTaskList.getCreated(), createdTaskList.getUpdated());
        TaskList updatedTaskList = taskListService.updateTaskList(createdTaskList.getId(), updateTaskList);

        assertThat(updatedTaskList.getTitle()).isEqualTo("Project Alpha - Phase 1");

        // Delete one task
        taskService.deleteTask(createdTaskList.getId(), createdTask2.getId());
        List<Task> remainingTasks = taskService.listTasks(createdTaskList.getId());
        assertThat(remainingTasks).hasSize(2);
        assertThat(remainingTasks).extracting(Task::getTitle).containsExactlyInAnyOrder("Setup Database", "Write Tests");

        // Verify final state
        assertThat(taskListService.getTaskList(createdTaskList.getId())).isPresent();
        assertThat(taskService.getTask(createdTaskList.getId(), createdTask1.getId())).isPresent();
        assertThat(taskService.getTask(createdTaskList.getId(), createdTask2.getId())).isEmpty();
        assertThat(taskService.getTask(createdTaskList.getId(), createdTask3.getId())).isPresent();
    }

    @Test
    void cascadeDelete_DeleteTaskListWithTasks_IntegrationTest() {
        // Create TaskList with tasks
        TaskList taskList = new TaskList(null, "Temporary Project", "Will be deleted", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        Task task1 = new Task(null, "Task 1", "Description 1", null, null, TaskPriority.LOW, null, null, null);
        Task task2 = new Task(null, "Task 2", "Description 2", null, null, TaskPriority.MEDIUM, null, null, null);

        taskService.createTask(createdTaskList.getId(), task1);
        taskService.createTask(createdTaskList.getId(), task2);

        // Verify tasks exist
        List<Task> tasks = taskService.listTasks(createdTaskList.getId());
        assertThat(tasks).hasSize(2);

        // Delete TaskList (should cascade delete tasks)
        taskListService.deleteTaskList(createdTaskList.getId());

        // Verify TaskList and tasks are deleted
        assertThat(taskListService.getTaskList(createdTaskList.getId())).isEmpty();
        assertThat(taskService.listTasks(createdTaskList.getId())).isEmpty();
    }
}