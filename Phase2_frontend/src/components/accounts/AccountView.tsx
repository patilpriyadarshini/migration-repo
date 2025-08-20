import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { accountApi } from '../../services/api';
import { AccountResponse } from '../../types/api';

interface AccountSearchForm {
  accountId: string;
}

const AccountView: React.FC = () => {
  const navigate = useNavigate();
  const [account, setAccount] = useState<AccountResponse | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string>('');

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<AccountSearchForm>({
    mode: 'onChange',
  });

  const onSubmit = async (data: AccountSearchForm) => {
    setIsLoading(true);
    setError('');
    setAccount(null);

    try {
      const accountId = parseInt(data.accountId);
      const response = await accountApi.getAccount(accountId);
      setAccount(response);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Account not found');
    } finally {
      setIsLoading(false);
    }
  };

  const handleBack = () => {
    const userType = localStorage.getItem('userType');
    if (userType === 'A') {
      navigate('/admin-menu');
    } else {
      navigate('/menu');
    }
  };

  const handleUpdate = () => {
    if (account) {
      navigate('/accounts/update', { state: { accountId: account.acctId } });
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-blue-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">Account View</span>
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
            <h2 className="text-xl font-semibold text-gray-900">Account Search</h2>
          </div>
          
          <div className="p-6">
            <form onSubmit={handleSubmit(onSubmit)} className="mb-6">
              <div className="flex items-end space-x-4">
                <div className="flex-1">
                  <label htmlFor="accountId" className="block text-sm font-medium text-gray-700 mb-1">
                    Account ID <span className="text-red-500">*</span>
                  </label>
                  <input
                    {...register('accountId', {
                      required: 'Account ID is required',
                      pattern: {
                        value: /^\d{11}$/,
                        message: 'Account ID must be exactly 11 digits'
                      }
                    })}
                    id="accountId"
                    type="text"
                    maxLength={11}
                    placeholder="11-digit account number"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {errors.accountId && (
                    <p className="mt-1 text-sm text-red-600">{errors.accountId.message}</p>
                  )}
                </div>
                <button
                  type="submit"
                  disabled={isLoading || isSubmitting}
                  className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
                >
                  {isLoading || isSubmitting ? 'Searching...' : 'Search'}
                </button>
              </div>
            </form>

            {error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">{error}</p>
              </div>
            )}

            {account && (
              <div className="space-y-6">
                <div className="flex justify-between items-center">
                  <h3 className="text-lg font-medium text-gray-900">Account Information</h3>
                  <button
                    onClick={handleUpdate}
                    className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    Update Account
                  </button>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="space-y-4">
                    <h4 className="font-medium text-gray-900">Account Details</h4>
                    <div className="space-y-2">
                      <div className="flex justify-between">
                        <span className="text-gray-600">Account ID:</span>
                        <span className="font-medium">{account.acctId}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Status:</span>
                        <span className={`font-medium ${account.acctActiveStatus === 'Y' ? 'text-green-600' : 'text-red-600'}`}>
                          {account.acctActiveStatus === 'Y' ? 'Active' : 'Inactive'}
                        </span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Current Balance:</span>
                        <span className="font-medium">${account.acctCurrBal?.toFixed(2)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Credit Limit:</span>
                        <span className="font-medium">${account.acctCreditLimit?.toFixed(2)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Cash Credit Limit:</span>
                        <span className="font-medium">${account.acctCashCreditLimit?.toFixed(2)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Open Date:</span>
                        <span className="font-medium">{account.acctOpenDate}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Expiration Date:</span>
                        <span className="font-medium">{account.acctExpiraionDate}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Reissue Date:</span>
                        <span className="font-medium">{account.acctReissueDate}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Current Cycle Credit:</span>
                        <span className="font-medium">${account.acctCurrCycCredit?.toFixed(2)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Current Cycle Debit:</span>
                        <span className="font-medium">${account.acctCurrCycDebit?.toFixed(2)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Account Group:</span>
                        <span className="font-medium">{account.acctGroupId}</span>
                      </div>
                    </div>
                  </div>

                  <div className="space-y-4">
                    <h4 className="font-medium text-gray-900">Customer Information</h4>
                    <div className="space-y-2">
                      <div className="flex justify-between">
                        <span className="text-gray-600">Name:</span>
                        <span className="font-medium">
                          {account.customerFirstName} {account.customerLastName}
                        </span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">SSN:</span>
                        <span className="font-medium">{account.customerSsn}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Date of Birth:</span>
                        <span className="font-medium">{account.customerDateOfBirth}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">FICO Score:</span>
                        <span className="font-medium">{account.customerFicoScore}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Phone:</span>
                        <span className="font-medium">{account.customerPhone1}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Phone 2:</span>
                        <span className="font-medium">{account.customerPhone2}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Customer ID:</span>
                        <span className="font-medium">{account.acctId}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Government Issued ID Ref:</span>
                        <span className="font-medium">{account.customerGovtIssuedId}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">EFT Account ID:</span>
                        <span className="font-medium">{account.customerEftAccountId}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Primary Card Holder:</span>
                        <span className="font-medium">{account.customerPriCardHolderInd === 'Y' ? 'Yes' : 'No'}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-gray-600">Address:</span>
                        <span className="font-medium">
                          {account.customerAddress1}<br />
                          {account.customerCity}, {account.customerState} {account.customerZipCode}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AccountView;
