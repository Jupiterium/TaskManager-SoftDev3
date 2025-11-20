import React from 'react';
import { Card } from '@nextui-org/react';
import { Wifi } from 'lucide-react';

interface BackOnlineBannerProps {
  isVisible: boolean;
}

const BackOnlineBanner: React.FC<BackOnlineBannerProps> = ({ isVisible }) => {
  if (!isVisible) return null;

  return (
    <Card className="fixed top-4 left-1/2 transform -translate-x-1/2 z-50 bg-green-500 text-white px-10 py-3 shadow-lg transition-opacity duration-700">
      <div className="flex items-center space-x-2">
        <Wifi size={20} />
        <span className="font-medium">Back online!</span>
      </div>
    </Card>
  );
};

export default BackOnlineBanner;