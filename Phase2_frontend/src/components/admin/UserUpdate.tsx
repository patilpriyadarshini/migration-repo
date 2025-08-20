import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { useNavigate, useLocation } from 'react-router-dom';
import { useMutation, useQuery } from '@tanstack/react-query';
import { userCreateSchema } from '../../utils/validation';
import { userAdminApi } from '../../services/api';
import { UserUpdateRequest } from '../../types/api';

const UserUpdate: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [userId, setUserId] = useState(location.state?.userId || '');
  const [success, setSuccess] = useState<string>('');

  const { data: user, isLoading: isLoadingUser, error: userError, refetch } = useQuery({
    queryKey: ['user', userId],
    queryFn: () => userAdminApi.getUser(userId),
    enabled: !!userId,
  });

  const updateMutation = useMutation({
    mutationFn: (data: UserUpdateRequest) => userAdminApi.updateUser(userId, data),
    onSuccess: () => {
      setSuccess('User updated successfully');
      refetch();
    },
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
  } = useForm<UserUpdateRequest>({
    resolver: yupResolver(userCreateSchema.omit(['userId'])),
  });

  React.useEffect(() => {
    if (user) {
      reset({
        firstName: user.firstName,
        lastName: user.lastName,
        password: '',
        userType: user.userType,
      });
    }
  }, [user, reset]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (userId.trim()) {
      setSuccess('');
      refetch();
    }
  };

  const onSubmit = (data: UserUpdateRequest) => {
    setSuccess('');
    updateMutation.mutate(data);
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
              <span className="ml-4 text-lg">User Management - Update User</span>
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
            <h2 className="text-xl font-semibold text-gray-900">Update User Information</h2>
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

            {updateMutation.error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">
                  {(updateMutation.error as any).response?.data?.message || 'User update failed'}
                </p>
              </div>
            )}

            {success && (
              <div className="mb-6 bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded">
                <p className="text-sm">{success}</p>
              </div>
            )}

            {user && (
              <div className="space-y-6">
                <div className="bg-gray-50 rounded-lg p-4">
                  <h3 className="text-lg font-medium text-gray-900 mb-2">Current User Information</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                    <div>
                      <span className="text-gray-600">User ID:</span>
                      <span className="ml-2 font-mono font-medium">{user.userId}</span>
                    </div>
                    <div>
                      <span className="text-gray-600">Current Type:</span>
                      <span className={`ml-2 px-2 py-1 text-xs font-semibold rounded-full ${
                        user.userType === 'A' 
                          ? 'bg-red-100 text-red-800' 
                          : 'bg-blue-100 text-blue-800'
                      }`}>
                        {user.userType === 'A' ? 'Administrator' : 'Regular User'}
                      </span>
                    </div>
                  </div>
                </div>

                <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        First Name <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...register('firstName')}
                        type="text"
                        maxLength={25}
                        placeholder="Enter first name"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-red-500 focus:border-red-500"
                      />
                      {errors.firstName && (
                        <p className="mt-1 text-sm text-red-600">{errors.firstName.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Last Name <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...register('lastName')}
                        type="text"
                        maxLength={25}
                        placeholder="Enter last name"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-red-500 focus:border-red-500"
                      />
                      {errors.lastName && (
                        <p className="mt-1 text-sm text-red-600">{errors.lastName.message}</p>
                      )}
                    </div>

                    <div className="md:col-span-2">
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        New Password <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...register('password')}
                        type="password"
                        maxLength={8}
                        placeholder="8 characters (leave blank to keep current)"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-red-500 focus:border-red-500"
                      />
                      {errors.password && (
                        <p className="mt-1 text-sm text-red-600">{errors.password.message}</p>
                      )}
                    </div>

                    <div className="md:col-span-2">
                      <label className="block text-sm font-medium text-gray-700 mb-3">
                        User Type <span className="text-red-500">*</span>
                      </label>
                      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <label className="flex items-center p-4 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-50">
                          <input
                            {...register('userType')}
                            type="radio"
                            value="U"
                            className="mr-3"
                          />
                          <div>
                            <div className="font-medium">Regular User</div>
                            <div className="text-sm text-gray-600">Standard user access</div>
                          </div>
                        </label>
                        
                        <label className="flex items-center p-4 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-50">
                          <input
                            {...register('userType')}
                            type="radio"
                            value="A"
                            className="mr-3"
                          />
                          <div>
                            <div className="font-medium">Administrator</div>
                            <div className="text-sm text-gray-600">Full system access</div>
                          </div>
                        </label>
                      </div>
                      {errors.userType && (
                        <p className="mt-1 text-sm text-red-600">{errors.userType.message}</p>
                      )}
                    </div>
                  </div>

                  <div className="flex justify-end space-x-4">
                    <button
                      type="button"
                      onClick={() => setUserId('')}
                      className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-red-500"
                    >
                      Search Another User
                    </button>
                    <button
                      type="submit"
                      disabled={updateMutation.isPending}
                      className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
                    >
                      {updateMutation.isPending ? 'Updating...' : 'Update User'}
                    </button>
                  </div>
                </form>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserUpdate;
