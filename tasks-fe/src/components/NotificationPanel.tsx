import React, { useEffect, useState } from "react";
import {
  Button,
  Card,
  CardBody,
  Badge,
  Popover,
  PopoverTrigger,
  PopoverContent,
} from "@nextui-org/react";
import { Bell, X, ExternalLink } from "lucide-react";
import { Notification } from "../domain/Notification";
import { useNavigate } from "react-router-dom";
import { useAppContext } from "../AppProvider";

const NotificationPanel: React.FC = () => {
  const [notifications, setNotifications] = useState<Notification[]>([]);
  const [unreadCount, setUnreadCount] = useState(0);
  const [isOpen, setIsOpen] = useState(false);
  const navigate = useNavigate();
  const { api } = useAppContext();

  const fetchNotifications = async () => {
    try {
      const data = await api.fetchNotifications();
      setNotifications(data || []);
      setUnreadCount((data || []).filter((n: Notification) => !n.isRead).length);
    } catch (error) {
      // On error, set empty arrays to prevent crashes
      setNotifications([]);
      setUnreadCount(0);
      console.error("Failed to fetch notifications:", error);
    }
  };

  const markAsRead = async (notificationId: string) => {
    try {
      await api.markNotificationAsRead(notificationId);
      fetchNotifications();
    } catch (error) {
      console.error("Failed to mark notification as read:", error);
    }
  };

  const dismissNotification = async (notificationId: string) => {
    try {
      await api.deleteNotification(notificationId);
      fetchNotifications();
    } catch (error) {
      console.error("Failed to dismiss notification:", error);
    }
  };

  const handleNotificationClick = (notification: Notification) => {
    if (notification.taskId && notification.taskListId) {
      // Navigate to edit task page
      navigate(`/task-lists/${notification.taskListId}/edit-task/${notification.taskId}`);
    } else if (notification.taskListId) {
      // Navigate to task list if only taskListId is available
      navigate(`/task-lists/${notification.taskListId}`);
    }
    markAsRead(notification.id);
    setIsOpen(false);
  };

  useEffect(() => {
    fetchNotifications();
    // Poll for new notifications every 30 seconds
    const interval = setInterval(fetchNotifications, 30000);
    return () => clearInterval(interval);
  }, [api]);

  return (
    <Popover isOpen={isOpen} onOpenChange={setIsOpen} placement="bottom-end">
      <PopoverTrigger>
        <Button
          isIconOnly
          variant="light"
          className="relative"
          aria-label="Notifications"
        >
          <Bell size={20} />
          {unreadCount > 0 && (
            <Badge
              content={unreadCount}
              color="danger"
              size="sm"
              className="absolute -top-1 -right-1"
            />
          )}
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-80 p-0">
        <div className="p-4 border-b">
          <h3 className="text-lg font-semibold">Notifications</h3>
        </div>
        <div className="max-h-96 overflow-y-auto">
          {notifications.length === 0 ? (
            <div className="p-4 text-center text-gray-500">
              No new notifications
            </div>
          ) : (
            notifications.map((notification) => (
              <Card
                key={notification.id}
                className="m-2 cursor-pointer hover:bg-gray-50"
                isPressable
                onPress={() => handleNotificationClick(notification)}
              >
                <CardBody className="p-3">
                  <div className="flex justify-between items-start">
                    <div className="flex-1">
                      <div className="flex items-center gap-2">
                        <h4 className="font-medium text-sm">
                          {notification.title}
                        </h4>
                        {notification.taskId && (
                          <ExternalLink size={12} className="text-blue-500" />
                        )}
                      </div>
                      <p className="text-xs text-gray-600 mt-1">
                        {notification.message}
                      </p>
                      {notification.taskTitle && (
                        <p className="text-xs text-blue-600 mt-1">
                          Task: {notification.taskTitle}
                        </p>
                      )}
                      <p className="text-xs text-gray-400 mt-2">
                        {new Date(notification.created).toLocaleString()}
                      </p>
                    </div>
                    <Button
                      isIconOnly
                      size="sm"
                      variant="light"
                      onClick={(e) => {
                        e.stopPropagation();
                        dismissNotification(notification.id);
                      }}
                      aria-label="Dismiss notification"
                    >
                      <X size={14} />
                    </Button>
                  </div>
                </CardBody>
              </Card>
            ))
          )}
        </div>
      </PopoverContent>
    </Popover>
  );
};

export default NotificationPanel;