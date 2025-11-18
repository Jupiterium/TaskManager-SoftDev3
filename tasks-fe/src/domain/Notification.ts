export interface Notification {
  id: string;
  title: string;
  message: string;
  type: NotificationType;
  taskId?: string;
  taskTitle?: string;
  isRead: boolean;
  created: string;
}

export enum NotificationType {
  TASK_DUE_SOON = "TASK_DUE_SOON",
  TASK_OVERDUE = "TASK_OVERDUE",
  TASK_ASSIGNED = "TASK_ASSIGNED",
  TASK_COMPLETED = "TASK_COMPLETED"
}