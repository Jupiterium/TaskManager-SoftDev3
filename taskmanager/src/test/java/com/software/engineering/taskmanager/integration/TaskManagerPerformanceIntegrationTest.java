package com.software.engineering.taskmanager.integration;

import com.software.engineering.taskmanager.domain.entities.Task;
import com.software.engineering.taskmanager.domain.entities.TaskList;
import com.software.engineering.taskmanager.domain.entities.TaskPriority;
import com.software.engineering.taskmanager.services.TaskListService;
import com.software.engineering.taskmanager.services.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TaskManagerPerformanceIntegrationTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskListService taskListService;

    @Test
    @Transactional
    void bulkOperations_CreateMultipleTasksAndTaskLists_IntegrationTest() {
        // Create multiple TaskLists
        List<TaskList> createdTaskLists = IntStream.range(0, 5)
                .mapToObj(i -> new TaskList(null, "TaskList " + i, "Description " + i, null, null, null))
                .map(taskListService::createTaskList)
                .toList();

        assertThat(createdTaskLists).hasSize(5);

        // Create multiple tasks for each TaskList
        createdTaskLists.forEach(taskList -> {
            IntStream.range(0, 10)
                    .mapToObj(i -> new Task(null, "Task " + i, "Description " + i, null, null, TaskPriority.MEDIUM, null, null, null, null))
                    .forEach(task -> taskService.createTask(taskList.getId(), task));
        });

        // Verify all tasks were created
        createdTaskLists.forEach(taskList -> {
            List<Task> tasks = taskService.listTasks(taskList.getId());
            assertThat(tasks).hasSize(10);
        });

        // Verify total TaskLists
        List<TaskList> allTaskLists = taskListService.listTaskList();
        assertThat(allTaskLists).hasSizeGreaterThanOrEqualTo(5);
    }

    @Test
    @Transactional
    void concurrentOperations_SequentialTaskCreation_IntegrationTest() {
        // Create a TaskList
        TaskList taskList = new TaskList(null, "Sequential Test List", "Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        // Create tasks sequentially to simulate concurrent-like load
        List<Task> createdTasks = IntStream.range(0, 10)
                .mapToObj(i -> {
                    Task task = new Task(null, "Sequential Task " + i, "Description " + i, null, null, TaskPriority.LOW, null, null, null, null);
                    return taskService.createTask(createdTaskList.getId(), task);
                })
                .toList();

        // Verify all tasks were created
        assertThat(createdTasks).hasSize(10);
        List<Task> tasks = taskService.listTasks(createdTaskList.getId());
        assertThat(tasks).hasSize(10);
        assertThat(tasks).extracting(Task::getTitle).allMatch(title -> title.startsWith("Sequential Task"));
    }

    @Test
    @Transactional
    void dataConsistency_UpdateOperationsWithTransactions_IntegrationTest() {
        // Create TaskList and Task
        TaskList taskList = new TaskList(null, "Consistency Test List", "Description", null, null, null);
        TaskList createdTaskList = taskListService.createTaskList(taskList);

        Task task = new Task(null, "Consistency Task", "Original Description", null, null, TaskPriority.MEDIUM, null, null, null, null);
        Task createdTask = taskService.createTask(createdTaskList.getId(), task);

        // Perform multiple updates to verify consistency
        IntStream.range(0, 5).forEach(i -> {
            Task updateTask = new Task(
                    createdTask.getId(),
                    "Updated Task " + i,
                    "Updated Description " + i,
                    null,
                    createdTask.getStatus(),
                    TaskPriority.HIGH,
                    null,
                    null,
                    null,
                    null
            );
            taskService.updateTask(createdTaskList.getId(), createdTask.getId(), updateTask);
        });

        // Verify final state
        Task finalTask = taskService.getTask(createdTaskList.getId(), createdTask.getId()).orElseThrow();
        assertThat(finalTask.getTitle()).isEqualTo("Updated Task 4");
        assertThat(finalTask.getDescription()).isEqualTo("Updated Description 4");
        assertThat(finalTask.getPriority()).isEqualTo(TaskPriority.HIGH);
    }
}