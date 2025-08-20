import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useMutation, useQuery } from '@tanstack/react-query';
import { userAdminApi } from '../../services/api';

const UserDelete: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [userId, setUserId] = useState(location.state?.userId || '');
  const [confirmation, setConfirmation] = useState('');
  const [success, setSuccess] = useState<string>('');

  const { data: user, isLoading: isLoadingUser, error: userError, refetch } = useQuery({
    queryKey: ['user', userId],
    queryFn: () => userAdminApi.getUser(userId),
    enabled: !!userId,
  });

  const deleteMutation = useMutation({
    mutationFn: () => userAdminApi.deleteUser(userId),
    onSuccess: () => {
      setSuccess('User deleted successfully');
      setTimeout(() => {
        navigate('/admin/users');
      }, 2000);
    },
  });

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (userId.trim()) {
      setSuccess('');
      setConfirmation('');
      refetch();
    }
  };

  const handleDelete = () => {
    if (confirmation === 'Y') {
      setSuccess('');
      deleteMutation.mutate();
    }
  };

  const handleBack = () => {
    navigate('/admin/users');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-red-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">User Management - Delete User</span>
            </div>
            <button
              onClick={handleBack}
              className="bg-red-800 hover:bg-red-900 px-3 py-1 rounded text-sm"
            >
              Back to User List
            </button>
          </div>
        </div>
      </div>

      <div className="max-w-4xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-900">Delete User</h2>
          </div>
          
          <div className="p-6">
            {!user && (
              <form onSubmit={handleSearch} className="mb-6">
                <div className="flex items-end space-x-4">
                  <div className="flex-1">
                    <label htmlFor="userId" className="block text-sm font-medium text-gray-700 mb-1">
                      User ID <span className="text-red-500">*</span>
                    </label>
                    <input
                      type="text"
                      value={userId}
                      onChange={(e) => setUserId(e.target.value)}
                      maxLength={8}
                      placeholder="8-character user ID"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-red-500 focus:border-red-500 font-mono"
                    />
                  </div>
                  <button
                    type="submit"
                    disabled={isLoadingUser || !userId.trim()}
                    className="px-6 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 disabled:opacity-50"
                  >
                    {isLoadingUser ? 'Searching...' : 'Search'}
                  </button>
                </div>
              </form>
            )}

            {userError && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">User not found or error occurred</p>
              </div>
            )}

            {deleteMutation.error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">
                  {(deleteMutation.error as any).response?.data?.message || 'User deletion failed'}
                </p>
              </div>
            )}

            {success && (
              <div className="mb-6 bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded">
                <p className="text-sm">{success}</p>
              </div>
            )}

            {user && !success && (
              <div className="space-y-6">
                <div className="bg-red-50 border border-red-200 rounded-lg p-6">
                  <div className="flex items-start">
                    <div className="flex-shrink-0">
                      <svg className="h-6 w-6 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-2.5L13.732 4c-.77-.833-1.732-.833-2.5 0L4.268 19.5c-.77.833.192 2.5 1.732 2.5z" />
                      </svg>
                    </div>
                    <div className="ml-3">
                      <h3 className="text-lg font-medium text-red-800">Warning: User Deletion</h3>
                      <p className="mt-2 text-sm text-red-700">
                        You are about to permanently delete this user. This action cannot be undone.
                      </p>
                    </div>
                  </div>
                </div>

                <div className="bg-gray-50 rounded-lg p-6">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">User Information</h3>
                  
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <div className="flex justify-between">
                        <span className="text-gray-600">User ID:</span>
                        <span className="font-mono font-medium">{user.userId}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Name:</span>
                        <span className="font-medium">{user.firstName} {user.lastName}</span>
                      </div>
                    </div>
                    
                    <div className="space-y-2">
                      <div className="flex justify-between">
                        <span className="text-gray-600">User Type:</span>
                        <span className={`px-2 py-1 text-xs font-semibold rounded-full ${
                          user.userType === 'A' 
                            ? 'bg-red-100 text-red-800' 
                            : 'bg-blue-100 text-blue-800'
                        }`}>
                          {user.userType === 'A' ? 'Administrator' : 'Regular User'}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                  <h3 className="text-lg font-medium text-gray-900 mb-2">Deletion Confirmation</h3>
                  <p className="text-sm text-gray-600 mb-3">
                    Please confirm that you want to delete this user. This action is permanent and cannot be reversed.
                  </p>
                  <div className="flex items-center space-x-4">
                    <label className="flex items-center">
                      <input
                        type="radio"
                        value="Y"
                        checked={confirmation === 'Y'}
                        onChange={(e) => setConfirmation(e.target.value)}
                        className="mr-2"
                      />
                      <span className="text-sm">Yes, delete this user permanently</span>
                    </label>
                    <label className="flex items-center">
                      <input
                        type="radio"
                        value="N"
                        checked={confirmation === 'N'}
                        onChange={(e) => setConfirmation(e.target.value)}
                        className="mr-2"
                      />
                      <span className="text-sm">No, cancel deletion</span>
                    </label>
                  </div>
                </div>

                <div className="flex justify-end space-x-4">
                  <button
                    onClick={() => setUserId('')}
                    className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-red-500"
                  >
                    Search Another User
                  </button>
                  <button
                    onClick={handleDelete}
                    disabled={deleteMutation.isPending || confirmation !== 'Y'}
                    className="px-6 py-2 bg-red-600 text-white rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 disabled:opacity-50"
                  >
                    {deleteMutation.isPending ? 'Deleting...' : 'Delete User'}
                  </button>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserDelete;
