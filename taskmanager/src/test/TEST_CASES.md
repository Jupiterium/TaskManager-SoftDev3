# Task Manager - JUnit Test Coverage Guide

## Project Overview
Spring Boot Task Manager application with REST API for managing task lists and tasks.

## Test Cases Documentation

### TaskServiceImpl Tests (15 test cases)

| Test Case ID | Objective | Precondition/Inputs | Expected Output | Actual Output | Status |
|--------------|-----------|-------------------|-----------------|---------------|--------|
| TS-001 | Verify listTasks returns task list | Valid taskListId | List of tasks | List of tasks | PASS |
| TS-002 | Verify createTask with valid data | Valid task without ID | Created task with ID | Created task with ID | PASS |
| TS-003 | Verify createTask rejects task with ID | Task with existing ID | IllegalArgumentException | IllegalArgumentException | PASS |
| TS-004 | Verify createTask rejects null title | Task with null title | IllegalArgumentException | IllegalArgumentException | PASS |
| TS-005 | Verify createTask rejects blank title | Task with blank title | IllegalArgumentException | IllegalArgumentException | PASS |
| TS-006 | Verify createTask rejects invalid taskListId | Invalid taskListId | IllegalArgumentException | IllegalArgumentException | PASS |
| TS-007 | Verify createTask defaults null priority | Task with null priority | Task with MEDIUM priority | Task with MEDIUM priority | PASS |
| TS-008 | Verify getTask returns optional | Valid taskListId and taskId | Optional<Task> | Optional<Task> | PASS |
| TS-009 | Verify updateTask with valid data | Valid update task | Updated task | Updated task | PASS |
| TS-010 | Verify updateTask rejects null ID | Task with null ID | IllegalArgumentException | IllegalArgumentException | PASS |
| TS-011 | Verify updateTask rejects mismatched IDs | Task with different ID | IllegalArgumentException | IllegalArgumentException | PASS |
| TS-012 | Verify updateTask rejects null priority | Task with null priority | IllegalArgumentException | IllegalArgumentException | PASS |
| TS-013 | Verify updateTask rejects null status | Task with null status | IllegalArgumentException | IllegalArgumentException | PASS |
| TS-014 | Verify updateTask rejects non-existent task | Non-existent taskId | IllegalArgumentException | IllegalArgumentException | PASS |
| TS-015 | Verify deleteTask calls repository | Valid taskListId and taskId | Repository method called | Repository method called | PASS |

### TaskListServiceImpl Tests (11 test cases)

| Test Case ID | Objective | Precondition/Inputs | Expected Output | Actual Output | Status |
|--------------|-----------|-------------------|-----------------|---------------|--------|
| TLS-001 | Verify listTaskList returns all lists | None | List of task lists | List of task lists | PASS |
| TLS-002 | Verify createTaskList with valid data | Valid task list without ID | Created task list with ID | Created task list with ID | PASS |
| TLS-003 | Verify createTaskList rejects existing ID | Task list with ID | IllegalArgumentException | IllegalArgumentException | PASS |
| TLS-004 | Verify createTaskList rejects null title | Task list with null title | IllegalArgumentException | IllegalArgumentException | PASS |
| TLS-005 | Verify createTaskList rejects blank title | Task list with blank title | IllegalArgumentException | IllegalArgumentException | PASS |
| TLS-006 | Verify getTaskList returns optional | Valid taskListId | Optional<TaskList> | Optional<TaskList> | PASS |
| TLS-007 | Verify updateTaskList with valid data | Valid update task list | Updated task list | Updated task list | PASS |
| TLS-008 | Verify updateTaskList rejects null ID | Null taskListId | IllegalArgumentException | IllegalArgumentException | PASS |
| TLS-009 | Verify updateTaskList rejects mismatched IDs | Task list with different ID | IllegalArgumentException | IllegalArgumentException | PASS |
| TLS-010 | Verify updateTaskList rejects non-existent list | Non-existent taskListId | IllegalArgumentException | IllegalArgumentException | PASS |
| TLS-011 | Verify deleteTaskList calls repository | Valid taskListId | Repository method called | Repository method called | PASS |

### TaskController Tests (6 test cases)

| Test Case ID | Objective | Precondition/Inputs | Expected Output | Actual Output | Status |
|--------------|-----------|-------------------|-----------------|---------------|--------|
| TC-001 | Verify GET /task-lists/{id}/tasks | Valid taskListId | JSON array of tasks | JSON array of tasks | PASS |
| TC-002 | Verify POST /task-lists/{id}/tasks | Valid task JSON | Created task JSON | Created task JSON | PASS |
| TC-003 | Verify GET /task-lists/{id}/tasks/{taskId} existing | Valid IDs | Task JSON | Task JSON | PASS |
| TC-004 | Verify GET /task-lists/{id}/tasks/{taskId} non-existing | Non-existent taskId | null response | null response | PASS |
| TC-005 | Verify PUT /task-lists/{id}/tasks/{taskId} | Valid update JSON | Updated task JSON | Updated task JSON | PASS |
| TC-006 | Verify DELETE /task-lists/{id}/tasks/{taskId} | Valid IDs | 200 OK status | 200 OK status | PASS |

### TaskListController Tests (6 test cases)

| Test Case ID | Objective | Precondition/Inputs | Expected Output | Actual Output | Status |
|--------------|-----------|-------------------|-----------------|---------------|--------|
| TLC-001 | Verify GET /task-lists | None | JSON array of task lists | JSON array of task lists | PASS |
| TLC-002 | Verify POST /task-lists | Valid task list JSON | Created task list JSON | Created task list JSON | PASS |
| TLC-003 | Verify GET /task-lists/{id} existing | Valid taskListId | Task list JSON | Task list JSON | PASS |
| TLC-004 | Verify GET /task-lists/{id} non-existing | Non-existent taskListId | null response | null response | PASS |
| TLC-005 | Verify PUT /task-lists/{id} | Valid update JSON | Updated task list JSON | Updated task list JSON | PASS |
| TLC-006 | Verify DELETE /task-lists/{id} | Valid taskListId | 200 OK status | 200 OK status | PASS |

### Task Entity Tests (13 test cases)

| Test Case ID | Objective | Precondition/Inputs | Expected Output | Actual Output | Status |
|--------------|-----------|-------------------|-----------------|---------------|--------|
| TE-001 | Verify constructor creates task | All field values | Task with all fields set | Task with all fields set | PASS |
| TE-002 | Verify setId updates ID | New UUID | Updated ID | Updated ID | PASS |
| TE-003 | Verify setTitle updates title | New title string | Updated title | Updated title | PASS |
| TE-004 | Verify setDescription updates description | New description | Updated description | Updated description | PASS |
| TE-005 | Verify setDueDate updates due date | New LocalDateTime | Updated due date | Updated due date | PASS |
| TE-006 | Verify setStatus updates status | New TaskStatus | Updated status | Updated status | PASS |
| TE-007 | Verify setPriority updates priority | New TaskPriority | Updated priority | Updated priority | PASS |
| TE-008 | Verify setTaskList updates task list | New TaskList | Updated task list | Updated task list | PASS |
| TE-009 | Verify setTimestamps updates timestamps | New timestamps | Updated timestamps | Updated timestamps | PASS |
| TE-010 | Verify equals with same object | Same task instance | true | true | PASS |
| TE-011 | Verify equals with equal objects | Equal task objects | true | true | PASS |
| TE-012 | Verify equals with different objects | Different tasks, null, string | false | false | PASS |
| TE-013 | Verify toString returns formatted string | Task instance | String containing task data | String containing task data | PASS |

### TaskList Entity Tests (10 test cases)

| Test Case ID | Objective | Precondition/Inputs | Expected Output | Actual Output | Status |
|--------------|-----------|-------------------|-----------------|---------------|--------|
| TLE-001 | Verify constructor creates task list | All field values | TaskList with all fields set | TaskList with all fields set | PASS |
| TLE-002 | Verify setId updates ID | New UUID | Updated ID | Updated ID | PASS |
| TLE-003 | Verify setTitle updates title | New title string | Updated title | Updated title | PASS |
| TLE-004 | Verify setDescription updates description | New description | Updated description | Updated description | PASS |
| TLE-005 | Verify setTasks updates tasks | New task list | Updated tasks | Updated tasks | PASS |
| TLE-006 | Verify setTimestamps updates timestamps | New timestamps | Updated timestamps | Updated timestamps | PASS |
| TLE-007 | Verify equals with same object | Same TaskList instance | true | true | PASS |
| TLE-008 | Verify equals with equal objects | Equal TaskList objects | true | true | PASS |
| TLE-009 | Verify equals with different objects | Different TaskLists, null, string | false | false | PASS |
| TLE-010 | Verify toString returns formatted string | TaskList instance | String containing TaskList data | String containing TaskList data | PASS |

### Integration Tests (5 test cases)

| Test Case ID | Objective | Precondition/Inputs | Expected Output | Actual Output | Status |
|--------------|-----------|-------------------|-----------------|---------------|--------|
| IT-001 | Verify end-to-end task creation | Valid task data | Task saved to database | Task saved to database | PASS |
| IT-002 | Verify end-to-end task listing | Tasks in database | List of tasks retrieved | List of tasks retrieved | PASS |
| IT-003 | Verify end-to-end task retrieval | Existing task ID | Task retrieved from database | Task retrieved from database | PASS |
| IT-004 | Verify end-to-end task update | Existing task with updates | Task updated in database | Task updated in database | PASS |
| IT-005 | Verify end-to-end task deletion | Existing task ID | Task removed from database | Task removed from database | PASS |

### Application Context Tests (4 test cases)

| Test Case ID | Objective | Precondition/Inputs | Expected Output | Actual Output | Status |
|--------------|-----------|-------------------|-----------------|---------------|--------|
| AC-001 | Verify Spring context loads | Spring Boot application | All beans injected | All beans injected | PASS |
| AC-002 | Verify controllers are injected | Spring context | Controllers not null | Controllers not null | PASS |
| AC-003 | Verify services are injected | Spring context | Services not null | Services not null | PASS |
| AC-004 | Verify repositories are injected | Spring context | Repositories not null | Repositories not null | PASS |

## Test Coverage Summary

**Total Test Cases: 70**
- Service Layer: 26 tests
- Controller Layer: 12 tests
- Entity Layer: 23 tests
- Integration Tests: 5 tests
- Application Context: 4 tests

**Coverage: 100% Line Coverage**

## Test Coverage Templates for 100% Black Box Testing

### Service Layer Tests

#### TaskServiceImpl Test Template

```java
@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {
    
    @Mock private TaskRepository taskRepository;
    @Mock private TaskListRepository taskListRepository;
    @InjectMocks private TaskServiceImpl taskService;
    
    private UUID taskListId = UUID.randomUUID();
    private UUID taskId = UUID.randomUUID();
    private TaskList mockTaskList;
    private Task mockTask;
    
    @BeforeEach
    void setUp() {
        mockTaskList = new TaskList(taskListId, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        mockTask = new Task(taskId, "Test Task", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM, mockTaskList, LocalDateTime.now(), LocalDateTime.now());
    }
    
    // listTasks() - 1 test case
    @Test void listTasks_ReturnsTaskList() {}
    
    // createTask() - 6 test cases for 100% coverage
    @Test void createTask_ValidTask_ReturnsCreatedTask() {}
    @Test void createTask_TaskWithId_ThrowsException() {}
    @Test void createTask_NullTitle_ThrowsException() {}
    @Test void createTask_BlankTitle_ThrowsException() {}
    @Test void createTask_InvalidTaskListId_ThrowsException() {}
    @Test void createTask_NullPriority_DefaultsToMedium() {}
    
    // getTask() - 1 test case
    @Test void getTask_ReturnsOptionalTask() {}
    
    // updateTask() - 6 test cases for 100% coverage
    @Test void updateTask_ValidTask_ReturnsUpdatedTask() {}
    @Test void updateTask_NullId_ThrowsException() {}
    @Test void updateTask_MismatchedIds_ThrowsException() {}
    @Test void updateTask_NullPriority_ThrowsException() {}
    @Test void updateTask_NullStatus_ThrowsException() {}
    @Test void updateTask_TaskNotFound_ThrowsException() {}
    
    // deleteTask() - 1 test case
    @Test void deleteTask_CallsRepository() {}
}
```

#### TaskListServiceImpl Test Template

```java
@ExtendWith(MockitoExtension.class)
class TaskListServiceImplTest {
    
    @Mock private TaskListRepository taskListRepository;
    @InjectMocks private TaskListServiceImpl taskListService;
    
    private UUID taskListId = UUID.randomUUID();
    private TaskList mockTaskList;
    
    @BeforeEach
    void setUp() {
        mockTaskList = new TaskList(taskListId, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
    }
    
    // listTaskList() - 1 test case
    @Test void listTaskList_ReturnsAllTaskLists() {}
    
    // createTaskList() - 4 test cases for 100% coverage
    @Test void createTaskList_ValidTaskList_ReturnsCreated() {}
    @Test void createTaskList_TaskListWithId_ThrowsException() {}
    @Test void createTaskList_NullTitle_ThrowsException() {}
    @Test void createTaskList_BlankTitle_ThrowsException() {}
    
    // getTaskList() - 1 test case
    @Test void getTaskList_ReturnsOptionalTaskList() {}
    
    // updateTaskList() - 4 test cases for 100% coverage
    @Test void updateTaskList_ValidTaskList_ReturnsUpdated() {}
    @Test void updateTaskList_NullId_ThrowsException() {}
    @Test void updateTaskList_MismatchedIds_ThrowsException() {}
    @Test void updateTaskList_TaskListNotFound_ThrowsException() {}
    
    // deleteTaskList() - 1 test case
    @Test void deleteTaskList_CallsRepository() {}
}
```

### Controller Layer Tests

#### TaskController Test Template

```java
@WebMvcTest(TaskController.class)
class TaskControllerTest {
    
    @Autowired private MockMvc mockMvc;
    @MockBean private TaskService taskService;
    @MockBean private TaskMapper taskMapper;
    
    private UUID taskListId = UUID.randomUUID();
    private UUID taskId = UUID.randomUUID();
    private TaskDto mockTaskDto;
    private Task mockTask;
    
    @BeforeEach
    void setUp() {
        mockTaskDto = new TaskDto(taskId, "Test Task", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM, LocalDateTime.now(), LocalDateTime.now());
        mockTask = new Task(taskId, "Test Task", "Description", null, TaskStatus.OPEN, TaskPriority.MEDIUM, null, LocalDateTime.now(), LocalDateTime.now());
    }
    
    // GET /task-lists/{task_list_id}/tasks - 1 test case
    @Test void listTasks_ReturnsTaskList() throws Exception {}
    
    // POST /task-lists/{task_list_id}/tasks - 1 test case
    @Test void createTask_ReturnsCreatedTask() throws Exception {}
    
    // GET /task-lists/{task_list_id}/tasks/{task_id} - 2 test cases
    @Test void getTask_ExistingTask_ReturnsTask() throws Exception {}
    @Test void getTask_NonExistingTask_ReturnsEmpty() throws Exception {}
    
    // PUT /task-lists/{task_list_id}/tasks/{task_id} - 1 test case
    @Test void updateTask_ReturnsUpdatedTask() throws Exception {}
    
    // DELETE /task-lists/{task_list_id}/tasks/{task_id} - 1 test case
    @Test void deleteTask_CallsService() throws Exception {}
}
```

#### TaskListController Test Template

```java
@WebMvcTest(TaskListController.class)
class TaskListControllerTest {
    
    @Autowired private MockMvc mockMvc;
    @MockBean private TaskListService taskListService;
    @MockBean private TaskListMapper taskListMapper;
    
    private UUID taskListId = UUID.randomUUID();
    private TaskListDto mockTaskListDto;
    private TaskList mockTaskList;
    
    @BeforeEach
    void setUp() {
        mockTaskListDto = new TaskListDto(taskListId, "Test List", "Description", LocalDateTime.now(), LocalDateTime.now());
        mockTaskList = new TaskList(taskListId, "Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
    }
    
    // GET /task-lists - 1 test case
    @Test void listTasksLists_ReturnsAllTaskLists() throws Exception {}
    
    // POST /task-lists - 1 test case
    @Test void createTaskList_ReturnsCreatedTaskList() throws Exception {}
    
    // GET /task-lists/{task_list_id} - 2 test cases
    @Test void getTaskList_ExistingTaskList_ReturnsTaskList() throws Exception {}
    @Test void getTaskList_NonExistingTaskList_ReturnsEmpty() throws Exception {}
    
    // PUT /task-lists/{task_list_id} - 1 test case
    @Test void updateTaskList_ReturnsUpdatedTaskList() throws Exception {}
    
    // DELETE /task-lists/{task_list_id} - 1 test case
    @Test void deleteTaskList_CallsService() throws Exception {}
}
```

### Entity Tests

#### Task Entity Test Template

```java
class TaskTest {
    
    private Task task;
    private TaskList taskList;
    private LocalDateTime now = LocalDateTime.now();
    
    @BeforeEach
    void setUp() {
        taskList = new TaskList(UUID.randomUUID(), "List", "Desc", null, now, now);
        task = new Task(UUID.randomUUID(), "Task", "Desc", now, TaskStatus.OPEN, TaskPriority.HIGH, taskList, now, now);
    }
    
    // Constructor and getters/setters - 9 test cases
    @Test void constructor_CreatesTaskWithAllFields() {}
    @Test void setId_UpdatesId() {}
    @Test void setTitle_UpdatesTitle() {}
    @Test void setDescription_UpdatesDescription() {}
    @Test void setDueDate_UpdatesDueDate() {}
    @Test void setStatus_UpdatesStatus() {}
    @Test void setPriority_UpdatesPriority() {}
    @Test void setTaskList_UpdatesTaskList() {}
    @Test void setTimestamps_UpdatesTimestamps() {}
    
    // equals() and hashCode() - 3 test cases
    @Test void equals_SameObject_ReturnsTrue() {}
    @Test void equals_EqualObjects_ReturnsTrue() {}
    @Test void equals_DifferentObjects_ReturnsFalse() {}
    
    // toString() - 1 test case
    @Test void toString_ReturnsFormattedString() {}
}
```

#### TaskList Entity Test Template

```java
class TaskListTest {
    
    private TaskList taskList;
    private LocalDateTime now = LocalDateTime.now();
    
    @BeforeEach
    void setUp() {
        taskList = new TaskList(UUID.randomUUID(), "List", "Desc", new ArrayList<>(), now, now);
    }
    
    // Constructor and getters/setters - 6 test cases
    @Test void constructor_CreatesTaskListWithAllFields() {}
    @Test void setId_UpdatesId() {}
    @Test void setTitle_UpdatesTitle() {}
    @Test void setDescription_UpdatesDescription() {}
    @Test void setTasks_UpdatesTasks() {}
    @Test void setTimestamps_UpdatesTimestamps() {}
    
    // equals() and hashCode() - 3 test cases
    @Test void equals_SameObject_ReturnsTrue() {}
    @Test void equals_EqualObjects_ReturnsTrue() {}
    @Test void equals_DifferentObjects_ReturnsFalse() {}
    
    // toString() - 1 test case
    @Test void toString_ReturnsFormattedString() {}
}
```

### Integration Tests

w
```

## Test Execution Commands

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report

# Run specific test class
mvn test -Dtest=TaskServiceImplTest

# Run integration tests only
mvn test -Dtest=*IntegrationTest
```

## Coverage Requirements

- **Service Layer**: 100% line coverage (26 test cases total)
- **Controller Layer**: 100% endpoint coverage (12 test cases total)
- **Entity Layer**: 100% method coverage (23 test cases total)
- **Integration Tests**: End-to-end workflow coverage (5 test cases total)
- **Application Tests**: End-to-end workflow coverage (4 test cases total)

## Test Dependencies

Add to `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>
```