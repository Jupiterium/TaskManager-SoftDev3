import { parseDate } from "@internationalized/date";
import {
  Button,
  Checkbox,
  DateInput,
  Progress,
  Spacer,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Spinner,
} from "@nextui-org/react";
import { ArrowLeft, Bell, BellOff, Edit, Minus, Plus, Trash } from "lucide-react";
import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAppContext } from "../AppProvider";
import Breadcrumb from "./Breadcrumb";
import { getPriorityColor } from "../utils/taskColors";
import { useKeyboardShortcuts } from "../hooks/useKeyboardShortcuts";
import Task from "../domain/Task";
import { TaskStatus } from "../domain/TaskStatus";

const TaskListScreen: React.FC = () => {
  const { state, api } = useAppContext();
  const { listId } = useParams();
  const [isLoading, setIsLoading] = useState(true);
  const navigate = useNavigate();
  
  useKeyboardShortcuts({
    onEscape: () => {
      api.fetchTaskLists();
      navigate('/');
    },
    onAltN: () => navigate(`/task-lists/${listId}/new-task`)
  });

  // Find task list directly from state instead of maintaining separate state
  const taskList = state.taskLists.find((tl) => listId === tl.id);

  // Single useEffect to handle all initial data loading
  useEffect(() => {
    const loadInitialData = async () => {
      if (!listId) return;

      setIsLoading(true);
      try {
        // Only fetch if we don't already have the task list
        if (!taskList) {
          await api.getTaskList(listId);
        }

        // Attempt to fetch tasks - this may 404, but we try anyway
        // If tasks aren't available yet, then that's an expected scenario we can handle
        await api.fetchTasks(listId).catch((error) => {
          console.log("Tasks not available yet:", error.message);
          // We're explicitly allowing this error as the tasks might not exist yet
        });
      } catch (error) {
        console.error("Error loading task list:", error);
      } finally {
        setIsLoading(false);
      }
    };

    loadInitialData();
  }, [listId]); // Only depend on listId

  // Calculate completion percentage based on tasks
  const completionPercentage = React.useMemo(() => {
    if (listId && state.tasks[listId]) {
      const tasks = state.tasks[listId];
      const closeTaskCount = tasks.filter(
        (task) => task.status === TaskStatus.CLOSED
      ).length;
      return tasks.length > 0 ? (closeTaskCount / tasks.length) * 100 : 0;
    }
    return 0;
  }, [state.tasks, listId]);

  const toggleStatus = (task: Task) => {
    if (listId) {
      const updatedTask = { ...task };
      updatedTask.status =
        task.status === TaskStatus.CLOSED ? TaskStatus.OPEN : TaskStatus.CLOSED;

      api
        .updateTask(listId, task.id, updatedTask)
        .then(() => {
          api.fetchTasks(listId);
          api.fetchTaskLists();
        });
    }
  };

  const deleteTaskList = async () => {
    if (null != listId) {
      await api.deleteTaskList(listId);
      navigate("/");
    }
  };

  const tableRows = () => {
    if (null != listId && null != state.tasks[listId]) {
      return state.tasks[listId].map((task) => (
        <TableRow key={task.id} className="border-t">
          <TableCell className="px-4 py-2 text-center">
            <Checkbox
              isSelected={TaskStatus.CLOSED == task.status}
              onValueChange={() => toggleStatus(task)}
              aria-label={`Mark task "${task.title}" as ${
                TaskStatus.CLOSED == task.status ? "open" : "closed"
              }`}
            />
          </TableCell>
          <TableCell className="px-4 py-2">{task.title}</TableCell>
          <TableCell className="px-4 py-2 text-center">
            <div className={`w-3 h-3 rounded-full ${getPriorityColor(task.priority)} mx-auto`}></div>
          </TableCell>
          <TableCell className="px-4 py-2">
            {task.dueDate && (
              <DateInput
                isDisabled
                defaultValue={parseDate(
                  new Date(task.dueDate).toISOString().split("T")[0]
                )}
                aria-label={`Due date for task "${task.title}"`}
              />
            )}
          </TableCell>
          <TableCell className="px-4 py-2">
            {task.customReminderDateTime ? (
              <div className="flex items-center space-x-2">
                <Bell className="h-4 w-4 text-blue-500" />
                <span className="text-sm">
                  {new Date(task.customReminderDateTime).toLocaleString()}
                </span>
              </div>
            ) : (
              <div className="flex justify-center">
                <BellOff className="h-4 w-4 text-gray-400" />
              </div>
            )}
          </TableCell>
          <TableCell className="px-4 py-2">
            <div className="flex space-x-2">
              <Button
                variant="ghost"
                aria-label={`Edit task "${task.title}"`}
                onPress={() =>
                  navigate(`/task-lists/${listId}/edit-task/${task.id}`)
                }
              >
                <Edit className="h-4 w-4" />
              </Button>
              {task.customReminderDateTime ? (
                <Button
                  variant="ghost"
                  onPress={() => api.removeCustomReminder(listId, task.id!)}
                  aria-label={`Remove reminder for "${task.title}"`}
                >
                  <BellOff className="h-4 w-4" />
                </Button>
              ) : (
                <Button
                  variant="ghost"
                  onPress={() => {
                    const reminderTime = new Date();
                    reminderTime.setHours(reminderTime.getHours() + 1);
                    api.setCustomReminder(listId, task.id!, reminderTime);
                  }}
                  aria-label={`Set 1-hour reminder for "${task.title}"`}
                >
                  <Bell className="h-4 w-4" />
                </Button>
              )}
              <Button
                variant="bordered"
                color="danger"
                onPress={() => api.deleteTask(listId, task.id)}
                aria-label={`Delete task "${task.title}"`}
                className="hover:bg-danger hover:text-white"
              >
                <Trash className="h-4 w-4" />
              </Button>
            </div>
          </TableCell>
        </TableRow>
      ));
    } else {
      return null;
    }
  };

  if (isLoading) {
    return <Spinner />;
  }

  return (
    <div className="p-4 max-w-4xl mx-auto">
      <Breadcrumb items={[
        { label: taskList ? taskList.title : 'Loading...' }
      ]} />
      
      <div className="flex items-center justify-between mb-6">
        <div className="flex w-full items-center justify-between">
          <Button
            variant="ghost"
            aria-label="Go back to Task Lists"
            onPress={() => {
              api.fetchTaskLists();
              navigate("/");
            }}
          >
            <ArrowLeft className="h-4 w-4" />
          </Button>

          <h1 className="text-2xl font-bold mx-4">
            {taskList ? taskList.title : "Unknown Task List"}
          </h1>

          <Button
            variant="ghost"
            aria-label={`Edit task list`}
            onPress={() => navigate(`/edit-task-list/${listId}`)}
          >
            <Edit className="h-4 w-4" />
          </Button>
        </div>
      </div>

      <Progress
        value={completionPercentage}
        className="mb-4"
        aria-label="Task completion progress"
      />
      <Button
        onPress={() => navigate(`/task-lists/${listId}/new-task`)}
        aria-label="Add new task"
        className="mb-4 w-full"
        color="primary"
      >
        <Plus className="h-4 w-4" /> Add a Task
      </Button>
      <div className="border rounded-lg overflow-hidden">
        <Table className="w-full" aria-label="Tasks list">
          <TableHeader>
            <TableColumn>Completed</TableColumn>
            <TableColumn>Title</TableColumn>
            <TableColumn>Priority</TableColumn>
            <TableColumn>Due Date</TableColumn>
            <TableColumn>Reminder</TableColumn>
            <TableColumn>Actions</TableColumn>
          </TableHeader>
          <TableBody>{tableRows()}</TableBody>
        </Table>
      </div>
      <Spacer y={4} />
      <div className="flex justify-end">
        <Button
          variant="bordered"
          color="danger"
          startContent={<Minus size={20} />}
          onPress={deleteTaskList}
          aria-label="Delete current task list"
          className="hover:bg-danger hover:text-white"
        >
          Delete Task List
        </Button>
      </div>

      <Spacer y={4} />
    </div>
  );
};

export default TaskListScreen;
