import React from 'react';
import { useNavigate } from 'react-router-dom';

const MainMenu: React.FC = () => {
  const navigate = useNavigate();
  const userId = localStorage.getItem('userId') || '';

  const menuOptions = [
    { key: '1', label: 'View Account', path: '/accounts/view' },
    { key: '2', label: 'View Account (Update)', path: '/accounts/update' },
    { key: '3', label: 'Card Listing', path: '/cards' },
    { key: '4', label: 'Card Details', path: '/cards/detail' },
    { key: '5', label: 'Card Update', path: '/cards/update' },
    { key: '6', label: 'Transaction List', path: '/transactions' },
    { key: '7', label: 'Transaction View', path: '/transactions/view' },
    { key: '8', label: 'Transaction Add', path: '/transactions/add' },
    { key: '9', label: 'Bill Payment', path: '/bill-payment' },
    { key: '10', label: 'Report Generation', path: '/reports' },
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
      <div className="bg-blue-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">Main Menu</span>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-sm">User: {userId}</span>
              <button
                onClick={handleLogout}
                className="bg-red-600 hover:bg-red-700 px-3 py-1 rounded text-sm"
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
            <h2 className="text-xl font-semibold text-gray-900">Select an Option</h2>
          </div>
          
          <div className="p-6">
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              {menuOptions.map((option) => (
                <button
                  key={option.key}
                  onClick={() => handleMenuSelect(option.path)}
                  className="text-left p-4 border border-gray-200 rounded-lg hover:bg-blue-50 hover:border-blue-300 transition-colors duration-200"
                >
                  <div className="flex items-center">
                    <span className="bg-blue-100 text-blue-800 text-sm font-medium px-2.5 py-0.5 rounded mr-3">
                      {option.key}
                    </span>
                    <span className="text-gray-900 font-medium">{option.label}</span>
                  </div>
                </button>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default MainMenu;
