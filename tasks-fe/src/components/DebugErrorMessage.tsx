import React from 'react';
import { Card } from '@nextui-org/react';

interface DebugErrorMessageProps {
  message: string;
  isVisible: boolean;
}

const DebugErrorMessage: React.FC<DebugErrorMessageProps> = ({ message, isVisible }) => {
  if (!isVisible || !message) return null;

  return (
    <Card className="fixed bottom-4 left-4 z-40 text-red-700 p-2 text-xs max-w-xs">
      <div className="font-arial">
        Debug: {message}
      </div>
    </Card>
  );
};

export default DebugErrorMessage;