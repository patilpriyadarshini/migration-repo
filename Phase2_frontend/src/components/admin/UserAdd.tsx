import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { userCreateSchema } from '../../utils/validation';
import { userAdminApi } from '../../services/api';
import { UserCreateRequest } from '../../types/api';

const UserAdd: React.FC = () => {
  const navigate = useNavigate();
  const [success, setSuccess] = useState<string>('');

  const createMutation = useMutation({
    mutationFn: (data: UserCreateRequest) => userAdminApi.createUser(data),
    onSuccess: (response: any) => {
      setSuccess(`User created successfully with ID: ${response.userId}`);
      reset();
    },
  });

  const {
    register,
    handleSubmit,
    setValue,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<UserCreateRequest>({
    resolver: yupResolver(userCreateSchema),
    mode: 'onChange',
  });

  useEffect(() => {
    (window as any).setUserAddFormValues = (firstName: string, lastName: string, userId: string, password: string, userType: string) => {
      setValue('firstName', firstName, { shouldValidate: true });
      setValue('lastName', lastName, { shouldValidate: true });
      setValue('userId', userId, { shouldValidate: true });
      setValue('password', password, { shouldValidate: true });
      setValue('userType', userType as 'A' | 'U', { shouldValidate: true });
    };
    
    return () => {
      delete (window as any).setUserAddFormValues;
    };
  }, [setValue]);

  const onSubmit = (data: UserCreateRequest) => {
    setSuccess('');
    createMutation.mutate(data);
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
              <span className="ml-4 text-lg">User Management - Add User</span>
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
            <h2 className="text-xl font-semibold text-gray-900">Create New User</h2>
          </div>
          
          <div className="p-6">
            {createMutation.error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">
                  {(createMutation.error as any).response?.data?.message || 'User creation failed'}
                </p>
              </div>
            )}

            {success && (
              <div className="mb-6 bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded">
                <p className="text-sm">{success}</p>
              </div>
            )}

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

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    User ID <span className="text-red-500">*</span>
                  </label>
                  <input
                    {...register('userId')}
                    type="text"
                    maxLength={8}
                    placeholder="8 characters"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-red-500 focus:border-red-500 font-mono"
                  />
                  {errors.userId && (
                    <p className="mt-1 text-sm text-red-600">{errors.userId.message}</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Password <span className="text-red-500">*</span>
                  </label>
                  <input
                    {...register('password')}
                    type="password"
                    maxLength={8}
                    placeholder="8 characters"
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

              <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                <h3 className="text-lg font-medium text-gray-900 mb-2">User Creation Guidelines</h3>
                <div className="text-sm text-gray-600 space-y-1">
                  <p>• User ID must be exactly 8 characters and unique in the system</p>
                  <p>• Password must be exactly 8 characters for security compliance</p>
                  <p>• Administrator users have access to user management functions</p>
                  <p>• Regular users have access to account and transaction functions</p>
                </div>
              </div>

              <div className="flex justify-end space-x-4">
                <button
                  type="button"
                  onClick={() => reset()}
                  className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-red-500"
                >
                  Clear Form
                </button>
                <button
                  type="submit"
                  disabled={createMutation.isPending || isSubmitting}
                  className="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:opacity-50"
                >
                  {createMutation.isPending || isSubmitting ? 'Creating...' : 'Create User'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserAdd;
