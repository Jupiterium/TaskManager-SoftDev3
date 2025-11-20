import React, { createContext, useContext, useReducer, useEffect, useState } from "react";
import axios from "axios";
import TaskList from "./domain/TaskList";
import Task from "./domain/Task";
import { useOfflineStatus } from "./hooks/useOfflineStatus";

interface AppState {
  taskLists: TaskList[];
  tasks: { [taskListId: string]: Task[] };
}

type Action =
  | { type: "FETCH_TASKLISTS"; payload: TaskList[] }
  | { type: "GET_TASKLIST"; payload: TaskList }
  | { type: "CREATE_TASKLIST"; payload: TaskList }
  | { type: "UPDATE_TASKLIST"; payload: TaskList }
  | { type: "DELETE_TASKLIST"; payload: string }
  | { type: "FETCH_TASKS"; payload: { taskListId: string; tasks: Task[] } }
  | { type: "CREATE_TASK"; payload: { taskListId: string; task: Task } }
  | { type: "GET_TASK"; payload: { taskListId: string; task: Task } }
  | {
      type: "UPDATE_TASK";
      payload: { taskListId: string; taskId: string; task: Task };
    }
  | { type: "DELETE_TASK"; payload: { taskListId: string; taskId: string } };

// Action types
const FETCH_TASKLISTS = "FETCH_TASKLISTS";
const GET_TASKLIST = "GET_TASKLIST";
const CREATE_TASKLIST = "CREATE_TASKLIST";
const UPDATE_TASKLIST = "UPDATE_TASKLIST";
const DELETE_TASKLIST = "DELETE_TASKLIST";
const FETCH_TASKS = "FETCH_TASKS";
const CREATE_TASK = "CREATE_TASK";
const GET_TASK = "GET_TASK";
const UPDATE_TASK = "UPDATE_TASK";
const DELETE_TASK = "DELETE_TASK";

// Reducer
const reducer = (state: AppState, action: Action): AppState => {
  switch (action.type) {
    case FETCH_TASKLISTS:
      return { ...state, taskLists: action.payload };
    case GET_TASKLIST:
      return {
        ...state,
        taskLists: state.taskLists.some((wl) => wl.id === action.payload.id)
          ? state.taskLists.map((wl) =>
              wl.id === action.payload.id ? action.payload : wl
            )
          : [...state.taskLists, action.payload],
      };
    case CREATE_TASKLIST:
      return { ...state, taskLists: [...state.taskLists, action.payload] };
    case UPDATE_TASKLIST:
      return {
        ...state,
        taskLists: state.taskLists.map((wl) =>
          wl.id === action.payload.id ? action.payload : wl
        ),
      };
    case DELETE_TASKLIST:
      return {
        ...state,
        taskLists: state.taskLists.filter((wl) => wl.id !== action.payload),
      };
    case FETCH_TASKS:
      return {
        ...state,
        tasks: {
          ...state.tasks,
          [action.payload.taskListId]: action.payload.tasks,
        },
      };
    case CREATE_TASK:
      return {
        ...state,
        tasks: {
          ...state.tasks,
          [action.payload.taskListId]: [
            ...(state.tasks[action.payload.taskListId] || []),
            action.payload.task,
          ],
        },
      };
    case GET_TASK: {
      // Get existing tasks or initialize empty array
      const existingTasks = state.tasks[action.payload.taskListId] || [];

      // Check if task exists
      const taskExists = existingTasks.some(
        (task) => task.id === action.payload.task.id
      );

      // Either update existing task or add new one
      const updatedTasks = taskExists
        ? existingTasks.map((task) =>
            task.id === action.payload.task.id ? action.payload.task : task
          )
        : [...existingTasks, action.payload.task];

      return {
        ...state,
        tasks: {
          ...state.tasks,
          [action.payload.taskListId]: updatedTasks,
        },
      };
    }
    case UPDATE_TASK:
      return {
        ...state,
        tasks: {
          ...state.tasks,
          [action.payload.taskListId]: state.tasks[
            action.payload.taskListId
          ].map((task) =>
            task.id === action.payload.taskId ? action.payload.task : task
          ),
        },
      };
    case DELETE_TASK:
      return {
        ...state,
        tasks: {
          ...state.tasks,
          [action.payload.taskListId]: state.tasks[
            action.payload.taskListId
          ].filter((task) => task.id !== action.payload.taskId),
        },
      };
    default:
      return state;
  }
};

// Initial state
const initialState: AppState = {
  taskLists: [],
  tasks: {},
};

// Context
interface AppContextType {
  state: AppState;
  isOnline: boolean;
  api: {
    fetchTaskLists: () => Promise<void>;
    getTaskList: (id: string) => Promise<void>;
    createTaskList: (taskList: Omit<TaskList, "id">) => Promise<void>;
    updateTaskList: (id: string, taskList: TaskList) => Promise<void>;
    deleteTaskList: (id: string) => Promise<void>;
    fetchTasks: (taskListId: string) => Promise<void>;
    createTask: (
      taskListId: string,
      task: Omit<Task, "id" | "taskListId">
    ) => Promise<void>;
    getTask: (taskListId: string, taskId: string) => Promise<void>;
    updateTask: (
      taskListId: string,
      taskId: string,
      task: Task
    ) => Promise<void>;
    deleteTask: (taskListId: string, taskId: string) => Promise<void>;
    setCustomReminder: (taskListId: string, taskId: string, reminderDateTime: Date) => Promise<void>;
    removeCustomReminder: (taskListId: string, taskId: string) => Promise<void>;
  };
}

const AppContext = createContext<AppContextType | undefined>(undefined);

// Provider component
export const AppProvider: React.FC<{ children: React.ReactNode }> = ({
  children,
}) => {
  const [state, dispatch] = useReducer(reducer, initialState);
  const [isOnline, setIsOnline] = useState(true);

  const jsonHeaders = {
    headers: { "Content-Type": "application/json" },
  };

  const handleApiCall = async <T>(apiCall: () => Promise<T>): Promise<T> => {
    try {
      const result = await apiCall();
      setIsOnline(true);
      return result;
    } catch (error) {
      // Only show offline banner for actual connection issues
      if (axios.isAxiosError(error) && (!error.response || error.code === 'ECONNREFUSED' || error.code === 'ERR_NETWORK')) {
        setIsOnline(false);
        throw new Error('Connection failed. Please check your connection.');
      }
      // For validation errors (400, 500, etc.) don't show offline banner
      throw error;
    }
  };

  // API calls
  const api: AppContextType["api"] = {
    fetchTaskLists: async () => {
      const response = await handleApiCall(() => 
        axios.get<TaskList[]>("/api/task-lists", jsonHeaders)
      );
      dispatch({ type: FETCH_TASKLISTS, payload: response.data });
    },
    getTaskList: async (id: string) => {
      const response = await handleApiCall(() => 
        axios.get<TaskList>(`/api/task-lists/${id}`, jsonHeaders)
      );
      dispatch({ type: GET_TASKLIST, payload: response.data });
    },
    createTaskList: async (taskList) => {
      const response = await handleApiCall(() => 
        axios.post<TaskList>("/api/task-lists", taskList, jsonHeaders)
      );
      dispatch({ type: CREATE_TASKLIST, payload: response.data });
    },
    getTask: async (taskListId: string, taskId: string) => {
      const response = await handleApiCall(() => 
        axios.get<Task>(`/api/task-lists/${taskListId}/tasks/${taskId}`, jsonHeaders)
      );
      dispatch({
        type: GET_TASK,
        payload: { taskListId, task: response.data },
      });
    },
    updateTaskList: async (id, taskList) => {
      const response = await handleApiCall(() => 
        axios.put<TaskList>(`/api/task-lists/${id}`, taskList, jsonHeaders)
      );
      dispatch({ type: UPDATE_TASKLIST, payload: response.data });
    },
    deleteTaskList: async (id) => {
      await handleApiCall(() => 
        axios.delete(`/api/task-lists/${id}`, jsonHeaders)
      );
      dispatch({ type: DELETE_TASKLIST, payload: id });
    },
    fetchTasks: async (taskListId) => {
      const response = await handleApiCall(() => 
        axios.get<Task[]>(`/api/task-lists/${taskListId}/tasks`, jsonHeaders)
      );
      dispatch({
        type: FETCH_TASKS,
        payload: { taskListId, tasks: response.data },
      });
    },
    createTask: async (taskListId, task) => {
      const response = await handleApiCall(() => 
        axios.post<Task>(`/api/task-lists/${taskListId}/tasks`, task, jsonHeaders)
      );
      dispatch({
        type: CREATE_TASK,
        payload: { taskListId, task: response.data },
      });
    },
    updateTask: async (taskListId, taskId, task) => {
      const response = await handleApiCall(() => 
        axios.put<Task>(`/api/task-lists/${taskListId}/tasks/${taskId}`, task, jsonHeaders)
      );
      dispatch({
        type: UPDATE_TASK,
        payload: { taskListId, taskId, task: response.data },
      });
    },
    deleteTask: async (taskListId, taskId) => {
      await handleApiCall(() => 
        axios.delete(`/api/task-lists/${taskListId}/tasks/${taskId}`, jsonHeaders)
      );
      dispatch({ type: DELETE_TASK, payload: { taskListId, taskId } });
    },
    setCustomReminder: async (taskListId, taskId, reminderDateTime) => {
      const response = await handleApiCall(() => 
        axios.put<Task>(`/api/task-lists/${taskListId}/tasks/${taskId}/reminder`, reminderDateTime.toISOString(), jsonHeaders)
      );
      dispatch({
        type: UPDATE_TASK,
        payload: { taskListId, taskId, task: response.data },
      });
    },
    removeCustomReminder: async (taskListId, taskId) => {
      const response = await handleApiCall(() => 
        axios.delete<Task>(`/api/task-lists/${taskListId}/tasks/${taskId}/reminder`, jsonHeaders)
      );
      dispatch({
        type: UPDATE_TASK,
        payload: { taskListId, taskId, task: response.data },
      });
    },
  };

  useEffect(() => {
    api.fetchTaskLists();
  }, []);

  return (
    <AppContext.Provider value={{ state, isOnline, api }}>{children}</AppContext.Provider>
  );
};

// Custom hook to use the context
export const useAppContext = (): AppContextType => {
  const context = useContext(AppContext);
  if (context === undefined) {
    throw new Error("useAppContext must be used within an AppProvider");
  }
  return context;
};
