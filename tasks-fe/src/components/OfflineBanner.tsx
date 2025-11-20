import React from 'react';
import { Card } from '@nextui-org/react';
import { WifiOff } from 'lucide-react';

interface OfflineBannerProps {
  isVisible: boolean;
}

const OfflineBanner: React.FC<OfflineBannerProps> = ({ isVisible }) => {
  if (!isVisible) return null;

  return (
    <Card className="fixed top-4 left-1/2 transform -translate-x-1/2 z-50 bg-red-500 text-white p-3 shadow-lg">
      <div className="flex items-center space-x-2">
        <WifiOff size={20} />
        <span className="font-medium">Connection failed. Please check your connection.</span>
      </div>
    </Card>
  );
};

export default OfflineBanner;