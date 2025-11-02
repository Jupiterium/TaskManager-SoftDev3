# Task Manager - JUnit Test Coverage Guide

## Project Overview
Spring Boot Task Manager application with REST API for managing task lists and tasks.

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

#### TaskServiceIntegrationTest Template

```java
@SpringBootTest
@Transactional
class TaskServiceIntegrationTest {
    
    @Autowired private TaskService taskService;
    @Autowired private TaskListRepository taskListRepository;
    
    private TaskList savedTaskList;
    
    @BeforeEach
    void setUp() {
        TaskList taskList = new TaskList(null, "Integration Test List", "Description", null, LocalDateTime.now(), LocalDateTime.now());
        savedTaskList = taskListRepository.save(taskList);
    }
    
    // End-to-end workflow tests - 5 test cases
    @Test void createTask_IntegrationTest() {}
    @Test void listTasks_IntegrationTest() {}
    @Test void getTask_IntegrationTest() {}
    @Test void updateTask_IntegrationTest() {}
    @Test void deleteTask_IntegrationTest() {}
}
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

- **Service Layer**: 100% line coverage (32 test cases total)
- **Controller Layer**: 100% endpoint coverage (12 test cases total)  
- **Entity Layer**: 100% method coverage (26 test cases total)
- **Integration Tests**: End-to-end workflow coverage (5 test cases total)

**Total Test Cases Required: 75**

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