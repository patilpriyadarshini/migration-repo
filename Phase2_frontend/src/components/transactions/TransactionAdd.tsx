import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { transactionCreateSchema } from '../../utils/validation';
import { transactionApi } from '../../services/api';
import { TransactionCreateRequest } from '../../types/api';

const TransactionAdd: React.FC = () => {
  const navigate = useNavigate();
  const [success, setSuccess] = useState<string>('');

  const createMutation = useMutation({
    mutationFn: (data: TransactionCreateRequest) => transactionApi.createTransaction(data),
    onSuccess: (response) => {
      setSuccess(`Transaction created successfully with ID: ${response.tranId}`);
      reset();
    },
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
    watch,
  } = useForm<TransactionCreateRequest>({
    resolver: yupResolver(transactionCreateSchema),
    defaultValues: {
      confirmation: 'N',
      origDate: new Date().toISOString().split('T')[0],
      procDate: new Date().toISOString().split('T')[0],
    },
  });

  const watchedConfirmation = watch('confirmation');

  const onSubmit = (data: TransactionCreateRequest) => {
    setSuccess('');
    createMutation.mutate(data);
  };

  const handleBack = () => {
    navigate('/transactions');
  };

  const getCurrentDate = () => {
    return new Date().toISOString().split('T')[0];
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-blue-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">Add Transaction</span>
            </div>
            <button
              onClick={handleBack}
              className="bg-blue-700 hover:bg-blue-800 px-3 py-1 rounded text-sm"
            >
              Back to Transaction List
            </button>
          </div>
        </div>
      </div>

      <div className="max-w-4xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-900">Create New Transaction</h2>
          </div>
          
          <div className="p-6">
            {createMutation.error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">
                  {(createMutation.error as any).response?.data?.message || 'Transaction creation failed'}
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
                    Card Number
                  </label>
                  <input
                    {...register('cardNum')}
                    type="text"
                    maxLength={16}
                    placeholder="16-digit card number"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {errors.cardNum && (
                    <p className="mt-1 text-sm text-red-600">{errors.cardNum.message}</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Account ID (Alternative)
                  </label>
                  <input
                    {...register('acctId')}
                    type="text"
                    maxLength={11}
                    placeholder="11-digit account ID"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {errors.acctId && (
                    <p className="mt-1 text-sm text-red-600">{errors.acctId.message}</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Transaction Type <span className="text-red-500">*</span>
                  </label>
                  <select
                    {...register('tranTypeCd')}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  >
                    <option value="">Select Type</option>
                    <option value="01">Purchase</option>
                    <option value="02">Cash Advance</option>
                    <option value="03">Payment</option>
                    <option value="04">Refund</option>
                  </select>
                  {errors.tranTypeCd && (
                    <p className="mt-1 text-sm text-red-600">{errors.tranTypeCd.message}</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Transaction Category <span className="text-red-500">*</span>
                  </label>
                  <select
                    {...register('tranCatCd')}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  >
                    <option value="">Select Category</option>
                    <option value="01">Retail</option>
                    <option value="02">Gas</option>
                    <option value="03">Grocery</option>
                    <option value="04">Restaurant</option>
                    <option value="05">Online</option>
                  </select>
                  {errors.tranCatCd && (
                    <p className="mt-1 text-sm text-red-600">{errors.tranCatCd.message}</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Transaction Source <span className="text-red-500">*</span>
                  </label>
                  <input
                    {...register('tranSource')}
                    type="text"
                    maxLength={10}
                    placeholder="e.g., POS, ATM, ONLINE"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {errors.tranSource && (
                    <p className="mt-1 text-sm text-red-600">{errors.tranSource.message}</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Transaction Amount <span className="text-red-500">*</span>
                  </label>
                  <input
                    {...register('tranAmt', { valueAsNumber: true })}
                    type="number"
                    step="0.01"
                    placeholder="0.00"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {errors.tranAmt && (
                    <p className="mt-1 text-sm text-red-600">{errors.tranAmt.message}</p>
                  )}
                </div>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700 mb-1">
                  Transaction Description <span className="text-red-500">*</span>
                </label>
                <input
                  {...register('tranDesc')}
                  type="text"
                  maxLength={26}
                  placeholder="Transaction description"
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                />
                {errors.tranDesc && (
                  <p className="mt-1 text-sm text-red-600">{errors.tranDesc.message}</p>
                )}
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Original Date <span className="text-red-500">*</span>
                  </label>
                  <input
                    {...register('origDate')}
                    type="date"
                    max={getCurrentDate()}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {errors.origDate && (
                    <p className="mt-1 text-sm text-red-600">{errors.origDate.message}</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Processing Date <span className="text-red-500">*</span>
                  </label>
                  <input
                    {...register('procDate')}
                    type="date"
                    max={getCurrentDate()}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {errors.procDate && (
                    <p className="mt-1 text-sm text-red-600">{errors.procDate.message}</p>
                  )}
                </div>
              </div>

              <div className="space-y-4">
                <h3 className="text-lg font-medium text-gray-900">Merchant Information</h3>
                
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Merchant ID <span className="text-red-500">*</span>
                    </label>
                    <input
                      {...register('merchantId')}
                      type="text"
                      placeholder="Merchant ID"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {errors.merchantId && (
                      <p className="mt-1 text-sm text-red-600">{errors.merchantId.message}</p>
                    )}
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Merchant Name <span className="text-red-500">*</span>
                    </label>
                    <input
                      {...register('merchantName')}
                      type="text"
                      maxLength={50}
                      placeholder="Merchant name"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {errors.merchantName && (
                      <p className="mt-1 text-sm text-red-600">{errors.merchantName.message}</p>
                    )}
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Merchant City <span className="text-red-500">*</span>
                    </label>
                    <input
                      {...register('merchantCity')}
                      type="text"
                      maxLength={50}
                      placeholder="City"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {errors.merchantCity && (
                      <p className="mt-1 text-sm text-red-600">{errors.merchantCity.message}</p>
                    )}
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Merchant ZIP <span className="text-red-500">*</span>
                    </label>
                    <input
                      {...register('merchantZip')}
                      type="text"
                      maxLength={5}
                      placeholder="12345"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {errors.merchantZip && (
                      <p className="mt-1 text-sm text-red-600">{errors.merchantZip.message}</p>
                    )}
                  </div>
                </div>
              </div>

              <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                <h3 className="text-lg font-medium text-gray-900 mb-2">Confirmation Required</h3>
                <div className="flex items-center space-x-4">
                  <label className="flex items-center">
                    <input
                      {...register('confirmation')}
                      type="radio"
                      value="Y"
                      className="mr-2"
                    />
                    <span className="text-sm">Yes, create this transaction</span>
                  </label>
                  <label className="flex items-center">
                    <input
                      {...register('confirmation')}
                      type="radio"
                      value="N"
                      className="mr-2"
                    />
                    <span className="text-sm">No, do not create</span>
                  </label>
                </div>
                {errors.confirmation && (
                  <p className="mt-1 text-sm text-red-600">{errors.confirmation.message}</p>
                )}
              </div>

              <div className="flex justify-end space-x-4">
                <button
                  type="button"
                  onClick={() => reset()}
                  className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  Clear Form
                </button>
                <button
                  type="submit"
                  disabled={createMutation.isPending || watchedConfirmation !== 'Y'}
                  className="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:opacity-50"
                >
                  {createMutation.isPending ? 'Creating...' : 'Create Transaction'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default TransactionAdd;
