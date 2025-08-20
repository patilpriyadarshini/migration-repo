import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { useNavigate } from 'react-router-dom';
import { useMutation, useQuery } from '@tanstack/react-query';
import { billPaymentSchema } from '../../utils/validation';
import { billPaymentApi, accountApi } from '../../services/api';
import { BillPaymentRequest } from '../../types/api';

interface BillPaymentFormData {
  paymentAmount: number;
  paymentDate: string;
  confirmation: string;
}

const BillPayment: React.FC = () => {
  const navigate = useNavigate();
  const [accountId, setAccountId] = useState('');
  const [success, setSuccess] = useState<string>('');

  const { data: account, isLoading: isLoadingAccount, error: accountError, refetch } = useQuery({
    queryKey: ['account', accountId],
    queryFn: () => accountApi.getAccount(parseInt(accountId)),
    enabled: !!accountId && accountId.length === 11,
  });

  const paymentMutation = useMutation({
    mutationFn: (data: BillPaymentRequest) => billPaymentApi.processBillPayment(data),
    onSuccess: (response: any) => {
      if (response.success) {
        setSuccess(`Payment processed successfully. New balance: $${response.currentBalance.toFixed(2)}`);
        refetch();
      } else {
        throw new Error(response.message || 'Payment processing failed');
      }
    },
  });

  const {
    register,
    handleSubmit,
    formState: { errors, isValid },
    watch,
    setValue,
  } = useForm<BillPaymentFormData>({
    resolver: yupResolver(billPaymentSchema),
  });

  console.log('Form errors:', errors);
  console.log('Form isValid:', isValid);
  console.log('Form values:', watch());

  const watchedConfirmation = watch('confirmation');

  const handleAccountSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (accountId.length === 11) {
      setSuccess('');
      refetch();
    }
  };

  const onSubmit = (data: BillPaymentFormData) => {
    console.log('Form submitted with data:', data);
    console.log('Account ID:', accountId);
    setSuccess('');
    paymentMutation.mutate({
      ...data,
      accountId: parseInt(accountId),
    });
  };

  const handleBack = () => {
    const userType = localStorage.getItem('userType');
    if (userType === 'A') {
      navigate('/admin-menu');
    } else {
      navigate('/menu');
    }
  };

  const handlePayFullBalance = () => {
    if (account && account.acctCurrBal > 0) {
      setValue('paymentAmount', account.acctCurrBal);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-blue-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">Bill Payment</span>
            </div>
            <button
              onClick={handleBack}
              className="bg-blue-700 hover:bg-blue-800 px-3 py-1 rounded text-sm"
            >
              Back to Menu
            </button>
          </div>
        </div>
      </div>

      <div className="max-w-4xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-900">Account Bill Payment</h2>
          </div>
          
          <div className="p-6">
            {!account && (
              <form onSubmit={handleAccountSearch} className="mb-6">
                <div className="flex items-end space-x-4">
                  <div className="flex-1">
                    <label htmlFor="accountId" className="block text-sm font-medium text-gray-700 mb-1">
                      Account ID <span className="text-red-500">*</span>
                    </label>
                    <input
                      type="text"
                      value={accountId}
                      onChange={(e) => setAccountId(e.target.value)}
                      maxLength={11}
                      placeholder="11-digit account number"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                  </div>
                  <button
                    type="submit"
                    disabled={isLoadingAccount || accountId.length !== 11}
                    className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
                  >
                    {isLoadingAccount ? 'Searching...' : 'Search'}
                  </button>
                </div>
              </form>
            )}

            {accountError && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">Account not found or error occurred</p>
              </div>
            )}

            {paymentMutation.error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">
                  {(paymentMutation.error as any).response?.data?.message || 'Payment processing failed'}
                </p>
              </div>
            )}

            {success && (
              <div className="mb-6 bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded">
                <p className="text-sm">{success}</p>
              </div>
            )}

            {account && (
              <div className="space-y-6">
                <div className="bg-gray-50 rounded-lg p-6">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Account Information</h3>
                  
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-2">
                      <div className="flex justify-between">
                        <span className="text-gray-600">Account ID:</span>
                        <span className="font-mono">{account.acctId}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Account Holder:</span>
                        <span className="font-medium">
                          {account.customerFirstName} {account.customerLastName}
                        </span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Account Status:</span>
                        <span className={`font-medium ${account.acctActiveStatus === 'Y' ? 'text-green-600' : 'text-red-600'}`}>
                          {account.acctActiveStatus === 'Y' ? 'Active' : 'Inactive'}
                        </span>
                      </div>
                    </div>
                    
                    <div className="space-y-2">
                      <div className="flex justify-between">
                        <span className="text-gray-600">Current Balance:</span>
                        <span className="text-2xl font-bold text-red-600">
                          ${account.acctCurrBal?.toFixed(2)}
                        </span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Credit Limit:</span>
                        <span className="font-medium">${account.acctCreditLimit?.toFixed(2)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Available Credit:</span>
                        <span className="font-medium text-green-600">
                          ${((account.acctCreditLimit || 0) - (account.acctCurrBal || 0)).toFixed(2)}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>

                {account.acctCurrBal > 0 ? (
                  <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Payment Amount <span className="text-red-500">*</span>
                        </label>
                        <div className="flex space-x-2">
                          <input
                            {...register('paymentAmount', { valueAsNumber: true })}
                            type="number"
                            step="0.01"
                            placeholder="0.00"
                            className="flex-1 px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                          />
                          <button
                            type="button"
                            onClick={handlePayFullBalance}
                            className="px-3 py-2 bg-gray-600 text-white rounded-md hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 text-sm"
                          >
                            Pay Full
                          </button>
                        </div>
                        {errors.paymentAmount && (
                          <p className="mt-1 text-sm text-red-600">{errors.paymentAmount.message}</p>
                        )}
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Payment Date <span className="text-red-500">*</span>
                        </label>
                        <input
                          {...register('paymentDate')}
                          type="date"
                          defaultValue={new Date().toISOString().split('T')[0]}
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                        />
                        {errors.paymentDate && (
                          <p className="mt-1 text-sm text-red-600">{errors.paymentDate.message}</p>
                        )}
                      </div>
                    </div>

                    <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-4">
                      <h3 className="text-lg font-medium text-gray-900 mb-2">Payment Confirmation</h3>
                      <p className="text-sm text-gray-600 mb-3">
                        Please confirm that you want to process this payment. This action cannot be undone.
                      </p>
                      <div className="flex items-center space-x-4">
                        <label className="flex items-center">
                          <input
                            {...register('confirmation')}
                            type="radio"
                            value="Y"
                            className="mr-2"
                          />
                          <span className="text-sm">Yes, process payment</span>
                        </label>
                        <label className="flex items-center">
                          <input
                            {...register('confirmation')}
                            type="radio"
                            value="N"
                            className="mr-2"
                          />
                          <span className="text-sm">No, cancel payment</span>
                        </label>
                      </div>
                      {errors.confirmation && (
                        <p className="mt-1 text-sm text-red-600">{errors.confirmation.message}</p>
                      )}
                    </div>

                    <div className="flex justify-end space-x-4">
                      <button
                        type="button"
                        onClick={() => setAccountId('')}
                        className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
                      >
                        Search Another Account
                      </button>
                      <button
                        type="submit"
                        disabled={paymentMutation.isPending || watchedConfirmation !== 'Y'}
                        className="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:opacity-50"
                      >
                        {paymentMutation.isPending ? 'Processing...' : 'Process Payment'}
                      </button>
                    </div>
                  </form>
                ) : (
                  <div className="bg-green-50 border border-green-200 rounded-lg p-6 text-center">
                    <h3 className="text-lg font-medium text-green-800 mb-2">No Payment Required</h3>
                    <p className="text-green-700">This account has a zero balance. No payment is needed at this time.</p>
                    <button
                      onClick={() => setAccountId('')}
                      className="mt-4 px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500"
                    >
                      Search Another Account
                    </button>
                  </div>
                )}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default BillPayment;
