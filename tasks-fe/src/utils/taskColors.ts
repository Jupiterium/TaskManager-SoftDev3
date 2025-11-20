import { TaskStatus } from '../domain/TaskStatus';
import { TaskPriority } from '../domain/TaskPriority';

export const getTaskStatusColor = (task: { status?: TaskStatus; dueDate?: Date }) => {
  if (task.status === TaskStatus.CLOSED) return 'bg-green-100 border-green-300';
  
  if (task.dueDate) {
    const now = new Date();
    const dueDate = new Date(task.dueDate);
    const diffHours = (dueDate.getTime() - now.getTime()) / (1000 * 60 * 60);
    
    if (diffHours < 0) return 'bg-red-100 border-red-300'; // Overdue
    if (diffHours < 24) return 'bg-yellow-100 border-yellow-300'; // Due soon
  }
  
  return 'bg-white border-gray-200'; // Normal
};

export const getPriorityColor = (priority: TaskPriority) => {
  switch (priority) {
    case TaskPriority.HIGH: return 'bg-red-500';
    case TaskPriority.MEDIUM: return 'bg-yellow-500';
    case TaskPriority.LOW: return 'bg-green-500';
    default: return 'bg-gray-500';
  }
};