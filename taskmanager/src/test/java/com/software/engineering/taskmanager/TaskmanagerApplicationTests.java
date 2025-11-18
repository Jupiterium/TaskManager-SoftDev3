package com.software.engineering.taskmanager;

import com.software.engineering.taskmanager.controllers.TaskController;
import com.software.engineering.taskmanager.controllers.TaskListController;
import com.software.engineering.taskmanager.controllers.NotificationController;
import com.software.engineering.taskmanager.services.TaskService;
import com.software.engineering.taskmanager.services.TaskListService;
import com.software.engineering.taskmanager.services.NotificationService;
import com.software.engineering.taskmanager.services.ReminderSchedulerService;
import com.software.engineering.taskmanager.repositories.TaskRepository;
import com.software.engineering.taskmanager.repositories.TaskListRepository;
import com.software.engineering.taskmanager.repositories.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/*
The application context tests are crucial for:

Verifying Spring Boot auto-configuration

Ensuring all components can be instantiated

Catching configuration issues early

Testing the actual dependency injection container
*/

@SpringBootTest
class TaskmanagerApplicationTests {

	@Autowired
	private TaskController taskController;
	
	@Autowired
	private TaskListController taskListController;
	
	@Autowired
	private NotificationController notificationController;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private TaskListService taskListService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired
	private ReminderSchedulerService reminderSchedulerService;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Autowired
	private TaskListRepository taskListRepository;
	
	@Autowired
	private NotificationRepository notificationRepository;

	@Test
	void contextLoads() {
		assertNotNull(taskController);
		assertNotNull(taskListController);
		assertNotNull(notificationController);
		assertNotNull(taskService);
		assertNotNull(taskListService);
		assertNotNull(notificationService);
		assertNotNull(reminderSchedulerService);
		assertNotNull(taskRepository);
		assertNotNull(taskListRepository);
		assertNotNull(notificationRepository);
	}

	@Test
	void controllersAreInjected() {
		assertNotNull(taskController);
		assertNotNull(taskListController);
		assertNotNull(notificationController);
	}

	@Test
	void servicesAreInjected() {
		assertNotNull(taskService);
		assertNotNull(taskListService);
		assertNotNull(notificationService);
		assertNotNull(reminderSchedulerService);
	}

	@Test
	void repositoriesAreInjected() {
		assertNotNull(taskRepository);
		assertNotNull(taskListRepository);
		assertNotNull(notificationRepository);
	}

}
