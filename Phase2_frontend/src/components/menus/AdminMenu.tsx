import React from 'react';
import { useNavigate } from 'react-router-dom';

const AdminMenu: React.FC = () => {
  const navigate = useNavigate();
  const userId = localStorage.getItem('userId') || '';

  const menuOptions = [
    { key: '1', label: 'User List', path: '/admin/users' },
    { key: '2', label: 'User Add', path: '/admin/users/add' },
    { key: '3', label: 'User Update', path: '/admin/users/update' },
    { key: '4', label: 'User Delete', path: '/admin/users/delete' },
  ];

  const handleMenuSelect = (path: string) => {
    navigate(path);
  };

  const handleLogout = () => {
    localStorage.removeItem('userId');
    localStorage.removeItem('userType');
    navigate('/login');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-red-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">Admin Menu</span>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-sm">Admin: {userId}</span>
              <button
                onClick={handleLogout}
                className="bg-red-800 hover:bg-red-900 px-3 py-1 rounded text-sm"
              >
                Logout
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="max-w-4xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-900">Administrator Options</h2>
          </div>
          
          <div className="p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {menuOptions.map((option) => (
                <button
                  key={option.key}
                  onClick={() => handleMenuSelect(option.path)}
                  className="text-left p-4 border border-gray-200 rounded-lg hover:bg-red-50 hover:border-red-300 transition-colors duration-200 flex items-center"
                >
                  <span className="bg-red-100 text-red-800 text-sm font-medium px-2.5 py-0.5 rounded mr-3">
                    {option.key}
                  </span>
                  <span className="text-gray-900 font-medium">{option.label}</span>
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminMenu;
