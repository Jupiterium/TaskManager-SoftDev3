# Task Manager - Component Architecture Documentation

## Project Overview
Spring Boot Task Manager application with React frontend, featuring custom reminders, notifications, and offline capabilities.

## System Architecture

```
┌────────────────────────────────────────────────────────────────────────────────┐
│                              Frontend (React)                                  │
├────────────────────────────────────────────────────────────────────────────────┤
│                                                                                │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────────────┐ │
│  │ TaskListsScreen │  │   TasksScreen   │  │    CreateUpdate Screens         │ │
│  │ - View lists    │  │ - Manage tasks  │  │ - Forms & validation            │ │
│  │ - Create new    │  │ - Set reminders │  │ - Custom reminders              │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────────────────────┘ │
│                                                                                │
│  ┌─────────────────────────────────────────────────────────────────────────────│
│  │                    AppProvider (State Management)                           │
│  │  • taskLists[], tasks{}  • API methods  • Offline detection                 │
│  └─────────────────────────────────────────────────────────────────────────────┘
│                                                                                │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐                 │
│  │   Breadcrumb    │  │ Keyboard        │  │ Offline         │                 │
│  │   Navigation    │  │ Shortcuts       │  │ Detection       │                 │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘                 │
└────────────────────────────────────────────────────────────────────────────────┘
                                      │
                                HTTP/REST API
                                      │
┌────────────────────────────────────────────────────────────────────────────────┐
│                           Backend (Spring Boot)                                │
├────────────────────────────────────────────────────────────────────────────────┤
│                                                                                │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────────────┐ │
│  │ TaskListController│ │ TaskController │  │   NotificationController        │ │
│  │ - REST endpoints│  │ - Task CRUD     │  │ - Notification API              │ │
│  └─────────────────┘  │ - Reminders     │  └─────────────────────────────────┘ │
│                       └─────────────────┘                                      │
│                                                                                │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────────────┐ │
│  │ TaskListService │  │   TaskService   │  │    NotificationService          │ │
│  │ - Business logic│  │ - Validation    │  │ - Create notifications          │ │
│  └─────────────────┘  │ - Custom logic  │  └─────────────────────────────────┘ │
│                       └─────────────────┘                                      │
│                                                                                │
│  ┌─────────────────────────────────────────────────────────────────────────────┐ 
│  │                    ReminderSchedulerService                                 │
│  │  @Scheduled - Custom reminders (10s) & Due tasks (daily)                    │
│  └─────────────────────────────────────────────────────────────────────────────│
│                                                                                │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────────────┐ │
│  │TaskListRepository│ │ TaskRepository  │  │   NotificationRepository        │ │
│  │ - JPA methods   │  │ - Custom queries│  │ - Data access                   │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────────────────────┘ │
└────────────────────────────────────────────────────────────────────────────────┘
                                      │
                                 JPA/Hibernate
                                      │
┌────────────────────────────────────────────────────────────────────────────────┐
│                              Database (H2)                                     │
│                                                                                │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────────────────┐ │
│  │  task_lists     │  │     tasks       │  │      notifications              │ │
│  │                 │  │                 │  │                                 │ │
│  │ - id, title     │  │ - id, title     │  │ - id, title, message            │ │
│  │ - description   │  │ - due_date      │  │ - type, task_id                 │ │
│  │ - timestamps    │  │ - custom_reminder│ │ - is_read, timestamps           │ │
│  └─────────────────┘  └─────────────────┘  └─────────────────────────────────┘ │
└────────────────────────────────────────────────────────────────────────────────┘
```

## Communication Flow

### API Communication
| Layer | Protocol | Description |
|-------|----------|-------------|
| Frontend ↔ Backend | HTTP/REST API | JSON data exchange, Error handling |
| Backend ↔ Database | JPA/Hibernate | Object-relational mapping, Query execution |

### Scheduled Operations
| Service | Schedule | Function |
|---------|----------|----------|
| checkCustomReminders() | Every 10 seconds | Find tasks with custom reminders due now, Create CUSTOM_REMINDER notifications |
| checkDueSoonTasks() | Daily at 9 AM | Find tasks due within 24 hours, Create TASK_DUE_SOON notifications |

## Key Features

### Frontend Capabilities
- **State Management**: Centralized AppProvider with reducer pattern
- **User Experience**: Offline detection, keyboard shortcuts, breadcrumb navigation
- **API Integration**: RESTful communication with comprehensive error handling
- **Real-time Updates**: Connection status monitoring and recovery notifications

### Backend Capabilities
- **REST Controllers**: Clean API endpoints following REST conventions
- **Service Layer**: Business logic validation and transaction management
- **Scheduled Tasks**: Automated reminder and notification system
- **Data Layer**: JPA repositories with custom query methods

### Database Design
- **Relational Structure**: Proper foreign key relationships between entities
- **Custom Fields**: Support for custom reminders and notification types
- **Audit Trail**: Created/updated timestamps for all entities
- **UUID Primary Keys**: Globally unique identifiers for all records

## Architecture Principles
This system follows **separation of concerns** with clear boundaries between:
- **Presentation Layer**: React components and user interface
- **Business Logic Layer**: Spring Boot services and validation
- **Data Access Layer**: JPA repositories and database operations