import React from 'react';
import { ChevronRight, Home } from 'lucide-react';
import { useNavigate } from 'react-router-dom';

interface BreadcrumbItem {
  label: string;
  path?: string;
}

interface BreadcrumbProps {
  items: BreadcrumbItem[];
}

const Breadcrumb: React.FC<BreadcrumbProps> = ({ items }) => {
  const navigate = useNavigate();

  return (
    <nav className="flex items-center space-x-2 text-sm text-gray-600 mb-4">
      <button onClick={() => navigate('/')} className="flex items-center hover:text-blue-600">
        <Home size={16} />
      </button>
      {items.map((item, index) => (
        <React.Fragment key={index}>
          <ChevronRight size={14} className="text-gray-600" />
          {item.path ? (
            <button onClick={() => navigate(item.path!)} className="text-gray-600 hover:text-blue-600">
              {item.label}
            </button>
          ) : (
            <span className="text-gray-600 font-medium">{item.label}</span>
          )}
        </React.Fragment>
      ))}
    </nav>
  );
};

export default Breadcrumb;