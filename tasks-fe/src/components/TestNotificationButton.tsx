import React from "react";
import { Button } from "@nextui-org/react";
import { TestTube } from "lucide-react";

const TestNotificationButton: React.FC = () => {
  const createTestNotification = async () => {
    try {
      await fetch("http://localhost:8081/notifications/test", {
        method: "POST",
      });
      // Refresh the page to see the new notification
      globalThis.location.reload();
    } catch (error) {
      console.error("Failed to create test notification:", error);
    }
  };

  return (
    <Button
      onPress={createTestNotification}
      color="secondary"
      variant="bordered"
      startContent={<TestTube size={16} />}
      size="sm"
    >
      Create Test Notification
    </Button>
  );
};

export default TestNotificationButton;