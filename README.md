# Task Manager - Software Engineering 3 Project

## Project Overview
A comprehensive Task/Project Management application developed as part of the Software Engineering 3 (3rd Year) module. 
This full-stack application demonstrates modern software engineering practices with a Spring Boot REST API backend and 
a React TypeScript frontend, featuring real-time notifications, task management, and comprehensive testing.


## Architecture
- **Backend**: Spring Boot 3.5.6 REST API with Java 21 (`taskmanager/`)
- **Frontend**: React 18 with TypeScript, Vite, and NextUI (`tasks-fe/`)
- **Database**: PostgreSQL with JPA/Hibernate
- **Testing**: Comprehensive test suite with 152 test cases achieving 100% coverage
- **Containerization**: Docker Compose for both services


## Key Features
### Core Functionality
- **Task Management**: Create, update, delete, and organize tasks
- **Task Lists**: Group tasks into customizable task lists
- **Priority System**: High, Medium, Low priority levels
- **Status Tracking**: Open, In Progress, Completed task states
- **Due Dates**: Set and track task deadlines
- **Custom Reminders**: Personalized notification scheduling

### Advanced Features
- **Real-time Notifications**: Automated task due notifications and custom reminders
- **Offline Support**: Continue working without internet connection
- **Auto-save**: Automatic data persistence
- **Keyboard Shortcuts**: Enhanced productivity features
- **Responsive Design**: Mobile-first UI with Tailwind CSS
- **Error Handling**: Comprehensive error management and user feedback


## Technology Stack
### Backend (taskmanager/)
- **Framework**: Spring Boot 3.5.6
- **Language**: Java 21
- **Database**: PostgreSQL (runtime), H2 (testing)
- **ORM (Object Relational Mapper)**: JPA/Hibernate
- **Build Tool**: Maven
- **Testing**: JUnit 5, Mockito, Spring Boot Test
- **Architecture**: Layered (Controller → Service → Repository)

### Frontend (tasks-fe/)
- **Framework**: React 18.3.1
- **Language**: TypeScript 5.5.3
- **Build Tool**: Vite 5.4.8
- **UI Library**: NextUI 2.4.8
- **Styling**: Tailwind CSS 3.4.14
- **HTTP Client**: Axios 1.7.7
- **Routing**: React Router DOM 6.27.0
- **Animation**: Framer Motion 11.11.9
- **Icons**: Lucide React 0.453.0
- **Testing**: Vitest 2.0.0, Testing Library


## Getting Started
### Prerequisites
- Java 21 or higher
- Node.js 18+ and npm
- PostgreSQL (or use Docker Compose)
- Git

### Backend Setup
1. **Navigate to backend directory**:
   ```bash
   cd taskmanager
   ```

2. **Start PostgreSQL** (using Docker Compose):
   ```bash
   docker-compose up
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```
   -> The API will be available at `http://localhost:8081`

4. **Run tests**:
   ```bash
   ./mvnw test
   ```


### Frontend Setup
1. **Navigate to frontend directory**:
   ```bash
   cd tasks-fe
   ```

2. **Install dependencies**:
   ```bash
   npm install
   ```

3. **Start development server**:
   ```bash
   npm run dev
   ```
   -> The actual application will then be available at `http://localhost:5173`

4. **Run tests**:
   ```bash
   npm test
   ```

5. **Build for production**:
   ```bash
   npm run build
   ```

### Docker Deployment
**Backend with PostgreSQL**:
```bash
cd taskmanager
docker-compose up 
```

**Frontend**:
```bash
cd tasks-fe
docker-compose up 
```
 
## Project Structure
```
TaskManager-SoftDev3/
├── taskmanager/ # Spring Boot Backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/software/engineering/taskmanager/
│   │   │   │   ├── controllers/ # REST Controllers
│   │   │   │   │   ├── TaskController.java
│   │   │   │   │   ├── TaskListController.java
│   │   │   │   │   ├── NotificationController.java
│   │   │   │   │   └── GlobalExceptionHandler.java
│   │   │   │   ├── services/ # Business Logic
│   │   │   │   │   ├── TaskService.java
│   │   │   │   │   ├── TaskListService.java
│   │   │   │   │   ├── NotificationService.java
│   │   │   │   │   └── ReminderSchedulerService.java
│   │   │   │   ├── repositories/ # Data Access
│   │   │   │   │   ├── TaskRepository.java
│   │   │   │   │   ├── TaskListRepository.java
│   │   │   │   │   └── NotificationRepository.java
│   │   │   │   ├── domain/
│   │   │   │   │   ├── entities/ # JPA Entities
│   │   │   │   │   └── dto/ # Data Transfer Objects
│   │   │   │   ├── mappers/ # Entity-DTO Mappers
│   │   │   │   └── TaskmanagerApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/ # 152 Test Cases
│   │       ├── java/ # Unit & Integration Tests
│   │       └── TEST_CASES.md  # Test Documentation
│   ├── pom.xml # Maven Configuration
│   ├── docker-compose.yml # PostgreSQL Container
│   └── README.md
│
├── tasks-fe/ # React Frontend
│   ├── src/
│   │   ├── components/ # React Components
│   │   │   ├── TaskListsScreen.tsx # Task Lists Management
│   │   │   ├── TasksScreen.tsx # Task Management
│   │   │   ├── CreateUpdateTaskScreen.tsx
│   │   │   ├── CreateUpdateTaskListScreen.tsx
│   │   │   ├── NotificationPanel.tsx # Notification System
│   │   │   ├── Header.tsx # Navigation
│   │   │   ├── OfflineBanner.tsx # Offline Support
│   │   │   └── BackOnlineBanner.tsx
│   │   ├── domain/ # TypeScript Types
│   │   │   ├── Task.ts
│   │   │   ├── TaskList.ts
│   │   │   ├── Notification.ts
│   │   │   ├── TaskStatus.ts
│   │   │   └── TaskPriority.ts
│   │   ├── hooks/ # Custom React Hooks
│   │   │   ├── useAutoSave.ts # Auto-save Functionality
│   │   │   ├── useOfflineStatus.ts # Offline Detection
│   │   │   └── useKeyboardShortcuts.ts # Keyboard Navigation
│   │   ├── utils/
│   │   │   └── taskColors.ts # UI Utilities
│   │   ├── App.tsx # Main Application
│   │   ├── AppProvider.tsx # Context Provider
│   │   └── main.tsx # Entry Point
│   ├── test/ # Frontend Tests
│   ├── package.json # Dependencies
│   ├── vite.config.ts # Vite Configuration
│   ├── tailwind.config.js # Tailwind CSS Config
│   ├── docker-compose.yml # Frontend Container
│   ├── Dockerfile
│   └── README.md
└── README.md # The main readme file (this file)
```


## API Endpoints
### Task Lists
- `GET /task-lists` - Get all task lists
- `POST /task-lists` - Create new task list
- `GET /task-lists/{id}` - Get specific task list
- `PUT /task-lists/{id}` - Update task list
- `DELETE /task-lists/{id}` - Delete task list

### Tasks
- `GET /task-lists/{id}/tasks` - Get tasks for a list
- `POST /task-lists/{id}/tasks` - Create new task
- `GET /task-lists/{id}/tasks/{taskId}` - Get specific task
- `PUT /task-lists/{id}/tasks/{taskId}` - Update task
- `DELETE /task-lists/{id}/tasks/{taskId}` - Delete task

### Notifications
- `GET /notifications` - Get all notifications
- `GET /notifications/unread` - Get unread notifications
- `PUT /notifications/{id}/read` - Mark as read
- `DELETE /notifications/{id}` - Delete notification
- `POST /notifications/test` - Create test notification


## Database Configuration
### PostgreSQL (Production)
```properties
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
server.port=8081
```

### H2 (Testing)
Automatically configured for test environment with in-memory database.


## Testing
### Backend Testing (152 Test Cases)
- **Unit Tests**: Service layer, Controller layer, Entity layer
- **Integration Tests**: Components & End-to-end workflows, Error scenarios
- **Repository Tests**: Data access layer validation
- **Coverage**: 100% line, branch, and method coverage

**Test Categories**:
- Service Layer: 41 tests (NotificationService, TaskService, TaskListService, ReminderScheduler)
- Controller Layer: 18 tests (REST API endpoints)
- Entity Layer: 36 tests (JPA entities and DTOs)
- Repository Layer: 12 tests (Data access)
- Mapper Layer: 7 tests (Entity-DTO conversion)
- Integration Tests: 34 tests (Workflows and error scenarios)
- Application Tests: 4 tests (Spring context)

### Frontend Testing
- **Unit Tests**: Component testing with Vitest
- **Integration Tests**: User interaction flows
- **E2E Tests**: Complete application workflows

**Run Tests**:
```bash
# Backend
cd taskmanager && ./mvnw test

# Frontend
cd tasks-fe && npm run test
```

## Development Features
### Backend Features
- **Layered Architecture**: Clean separation of concerns
- **Exception Handling**: Global exception handler with proper HTTP status codes
- **Data Validation**: Input validation with proper error messages
- **Scheduled Tasks**: Automatic reminder notifications
- **Custom Queries**: Optimized database queries for performance

### Frontend Features
- **Offline Support**: Works without internet connection
- **Auto-save**: Automatic data persistence using localStorage
- **Breadcrumb Navigation**: Subtle visual element which helps with the site navigation
- **Keyboard Shortcuts**: Modern & Enhanced productivity 
  - Alt+N - New Task/Task List (depending where you are)
  - Alt+Enter - Submit/Save
  - Alt+S - Quick Save
  - Esc - Cancel/Back
- **Responsive Design**: Approaching various devices
- **Real-time Updates**: Live notification system
- **Error Boundaries**: Proper error handling


## Development Team (x4)
3rd Year Software Design Students @ TUS (Technological University of the Shannon):
- Jenny (Huong) Tran, Daniel Raducan, Makar Semikin, Steven (Yu Lin) Wan


## License
This project is developed for educational purposes as part of the Software Engineering 3 module. 
