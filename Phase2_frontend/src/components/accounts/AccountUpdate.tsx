import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { useNavigate, useLocation } from 'react-router-dom';
import { accountUpdateSchema } from '../../utils/validation';
import { accountApi } from '../../services/api';
import { AccountResponse, AccountUpdateRequest } from '../../types/api';

interface AccountSearchForm {
  accountId: string;
}

const AccountUpdate: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [account, setAccount] = useState<AccountResponse | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isUpdating, setIsUpdating] = useState(false);
  const [error, setError] = useState<string>('');
  const [success, setSuccess] = useState<string>('');

  const searchForm = useForm<AccountSearchForm>();
  const updateForm = useForm<AccountUpdateRequest>({
    resolver: yupResolver(accountUpdateSchema),
  });

  useEffect(() => {
    if (location.state?.accountId) {
      searchForm.setValue('accountId', location.state.accountId.toString());
      handleSearch({ accountId: location.state.accountId.toString() });
    }
  }, [location.state]);

  const handleSearch = async (data: AccountSearchForm) => {
    setIsLoading(true);
    setError('');
    setAccount(null);
    setSuccess('');

    try {
      const accountId = parseInt(data.accountId);
      const response = await accountApi.getAccount(accountId);
      setAccount(response);
      
      const formatSsn = (ssn: string) => {
        const cleaned = ssn.replace(/\D/g, ''); // Remove non-digits
        if (cleaned.length === 9) {
          return `${cleaned.slice(0, 3)}-${cleaned.slice(3, 5)}-${cleaned.slice(5)}`;
        }
        return ssn; // Return as-is if not 9 digits
      };

      updateForm.reset({
        acctActiveStatus: response.acctActiveStatus,
        acctCurrBal: response.acctCurrBal,
        acctCreditLimit: response.acctCreditLimit,
        acctCashCreditLimit: response.acctCashCreditLimit,
        acctOpenDate: response.acctOpenDate,
        acctExpiraionDate: response.acctExpiraionDate,
        acctReissueDate: response.acctReissueDate,
        acctCurrCycCredit: response.acctCurrCycCredit,
        acctCurrCycDebit: response.acctCurrCycDebit,
        acctAddrZip: response.acctAddrZip,
        acctGroupId: response.acctGroupId,
        customerFirstName: response.customerFirstName || '',
        customerLastName: response.customerLastName || '',
        customerSsn: formatSsn(response.customerSsn || ''),
        customerDateOfBirth: response.customerDateOfBirth || '',
        customerFicoScore: response.customerFicoScore || 0,
        customerPhone1: response.customerPhone1 || '',
        customerPhone2: response.customerPhone2 || '',
        customerAddress1: response.customerAddress1 || '',
        customerAddress2: response.customerAddress2 || '',
        customerCity: response.customerCity || '',
        customerState: response.customerState || '',
        customerZipCode: response.customerZipCode || '',
        customerCountry: response.customerCountry || '',
        customerGovtIssuedId: response.customerGovtIssuedId || '',
        customerEftAccountId: response.customerEftAccountId || '',
        customerPriCardHolderInd: response.customerPriCardHolderInd || 'N',
      });
    } catch (err: any) {
      setError(err.response?.data?.message || 'Account not found');
    } finally {
      setIsLoading(false);
    }
  };

  const onUpdate = async (data: AccountUpdateRequest) => {
    if (!account) return;

    setIsUpdating(true);
    setError('');
    setSuccess('');

    try {
      const transformedData = {
        ...data,
        customerSsn: data.customerSsn // Keep dashes for backend validation
      };
      
      await accountApi.updateAccount(account.acctId, transformedData);
      setSuccess('Account updated successfully');
    } catch (err: any) {
      setError(err.response?.data?.message || 'Update failed');
    } finally {
      setIsUpdating(false);
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

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-blue-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">Account Update</span>
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

      <div className="max-w-6xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-900">Account Update</h2>
          </div>
          
          <div className="p-6">
            {!account && (
              <form onSubmit={searchForm.handleSubmit(handleSearch)} className="mb-6">
                <div className="flex items-end space-x-4">
                  <div className="flex-1">
                    <label htmlFor="accountId" className="block text-sm font-medium text-gray-700 mb-1">
                      Account ID <span className="text-red-500">*</span>
                    </label>
                    <input
                      {...searchForm.register('accountId', {
                        required: 'Account ID is required',
                        pattern: {
                          value: /^\d{11}$/,
                          message: 'Account ID must be exactly 11 digits'
                        }
                      })}
                      type="text"
                      maxLength={11}
                      placeholder="11-digit account number"
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {searchForm.formState.errors.accountId && (
                      <p className="mt-1 text-sm text-red-600">{searchForm.formState.errors.accountId.message}</p>
                    )}
                  </div>
                  <button
                    type="submit"
                    disabled={isLoading}
                    className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
                  >
                    {isLoading ? 'Searching...' : 'Search'}
                  </button>
                </div>
              </form>
            )}

            {error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">{error}</p>
              </div>
            )}

            {success && (
              <div className="mb-6 bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded">
                <p className="text-sm">{success}</p>
              </div>
            )}

            {account && (
              <form onSubmit={updateForm.handleSubmit(onUpdate)} className="space-y-6">
                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div className="space-y-4">
                    <h3 className="text-lg font-medium text-gray-900">Account Information</h3>
                    
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Account Status <span className="text-red-500">*</span>
                      </label>
                      <select
                        {...updateForm.register('acctActiveStatus')}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      >
                        <option value="Y">Active</option>
                        <option value="N">Inactive</option>
                      </select>
                      {updateForm.formState.errors.acctActiveStatus && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctActiveStatus.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Current Balance <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('acctCurrBal', { valueAsNumber: true })}
                        type="number"
                        step="0.01"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.acctCurrBal && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctCurrBal.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Credit Limit <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('acctCreditLimit', { valueAsNumber: true })}
                        type="number"
                        step="0.01"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.acctCreditLimit && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctCreditLimit.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Cash Credit Limit <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('acctCashCreditLimit', { valueAsNumber: true })}
                        type="number"
                        step="0.01"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.acctCashCreditLimit && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctCashCreditLimit.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Open Date <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('acctOpenDate')}
                        type="date"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.acctOpenDate && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctOpenDate.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Expiration Date <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('acctExpiraionDate')}
                        type="date"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.acctExpiraionDate && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctExpiraionDate.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Reissue Date <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('acctReissueDate')}
                        type="date"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.acctReissueDate && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctReissueDate.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Current Cycle Credit <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('acctCurrCycCredit', { valueAsNumber: true })}
                        type="number"
                        step="0.01"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.acctCurrCycCredit && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctCurrCycCredit.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Current Cycle Debit <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('acctCurrCycDebit', { valueAsNumber: true })}
                        type="number"
                        step="0.01"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.acctCurrCycDebit && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctCurrCycDebit.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Address ZIP <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('acctAddrZip')}
                        type="text"
                        maxLength={5}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.acctAddrZip && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctAddrZip.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Group ID <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('acctGroupId')}
                        type="text"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.acctGroupId && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.acctGroupId.message}</p>
                      )}
                    </div>
                  </div>

                  <div className="space-y-4">
                    <h3 className="text-lg font-medium text-gray-900">Customer Information</h3>
                    
                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          First Name <span className="text-red-500">*</span>
                        </label>
                        <input
                          {...updateForm.register('customerFirstName')}
                          type="text"
                          maxLength={25}
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                        />
                        {updateForm.formState.errors.customerFirstName && (
                          <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerFirstName.message}</p>
                        )}
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Last Name <span className="text-red-500">*</span>
                        </label>
                        <input
                          {...updateForm.register('customerLastName')}
                          type="text"
                          maxLength={25}
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                        />
                        {updateForm.formState.errors.customerLastName && (
                          <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerLastName.message}</p>
                        )}
                      </div>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        SSN <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('customerSsn')}
                        type="text"
                        maxLength={11}
                        placeholder="XXX-XX-XXXX"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.customerSsn && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerSsn.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        FICO Score <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('customerFicoScore', { valueAsNumber: true })}
                        type="number"
                        min={300}
                        max={850}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.customerFicoScore && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerFicoScore.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Date of Birth <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('customerDateOfBirth')}
                        type="date"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.customerDateOfBirth && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerDateOfBirth.message}</p>
                      )}
                    </div>

                    <div className="grid grid-cols-2 gap-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Phone 1 <span className="text-red-500">*</span>
                        </label>
                        <input
                          {...updateForm.register('customerPhone1')}
                          type="tel"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                        />
                        {updateForm.formState.errors.customerPhone1 && (
                          <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerPhone1.message}</p>
                        )}
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          Phone 2
                        </label>
                        <input
                          {...updateForm.register('customerPhone2')}
                          type="tel"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                        />
                        {updateForm.formState.errors.customerPhone2 && (
                          <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerPhone2.message}</p>
                        )}
                      </div>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Address Line 1 <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('customerAddress1')}
                        type="text"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.customerAddress1 && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerAddress1.message}</p>
                      )}
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Address Line 2
                      </label>
                      <input
                        {...updateForm.register('customerAddress2')}
                        type="text"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.customerAddress2 && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerAddress2.message}</p>
                      )}
                    </div>

                    <div className="grid grid-cols-3 gap-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          City <span className="text-red-500">*</span>
                        </label>
                        <input
                          {...updateForm.register('customerCity')}
                          type="text"
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                        />
                        {updateForm.formState.errors.customerCity && (
                          <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerCity.message}</p>
                        )}
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          State <span className="text-red-500">*</span>
                        </label>
                        <input
                          {...updateForm.register('customerState')}
                          type="text"
                          maxLength={2}
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                        />
                        {updateForm.formState.errors.customerState && (
                          <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerState.message}</p>
                        )}
                      </div>

                      <div>
                        <label className="block text-sm font-medium text-gray-700 mb-1">
                          ZIP Code <span className="text-red-500">*</span>
                        </label>
                        <input
                          {...updateForm.register('customerZipCode')}
                          type="text"
                          maxLength={5}
                          className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                        />
                        {updateForm.formState.errors.customerZipCode && (
                          <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerZipCode.message}</p>
                        )}
                      </div>
                    </div>

                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Country <span className="text-red-500">*</span>
                      </label>
                      <input
                        {...updateForm.register('customerCountry')}
                        type="text"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.customerCountry && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerCountry.message}</p>
                      )}
                    </div>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Government Issued ID
                      </label>
                      <input
                        {...updateForm.register('customerGovtIssuedId')}
                        type="text"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.customerGovtIssuedId && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerGovtIssuedId.message}</p>
                      )}
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        EFT Account ID
                      </label>
                      <input
                        {...updateForm.register('customerEftAccountId')}
                        type="text"
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      />
                      {updateForm.formState.errors.customerEftAccountId && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerEftAccountId.message}</p>
                      )}
                    </div>
                    <div>
                      <label className="block text-sm font-medium text-gray-700 mb-1">
                        Primary Card Holder
                      </label>
                      <select
                        {...updateForm.register('customerPriCardHolderInd')}
                        className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                      >
                        <option value="Y">Yes</option>
                        <option value="N">No</option>
                      </select>
                      {updateForm.formState.errors.customerPriCardHolderInd && (
                        <p className="mt-1 text-sm text-red-600">{updateForm.formState.errors.customerPriCardHolderInd.message}</p>
                      )}
                    </div>
                  </div>
                </div>

                <div className="flex justify-end space-x-4">
                  <button
                    type="button"
                    onClick={() => setAccount(null)}
                    className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    Search Another
                  </button>
                  <button
                    type="submit"
                    disabled={isUpdating}
                    className="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:opacity-50"
                  >
                    {isUpdating ? 'Updating...' : 'Update Account'}
                  </button>
                </div>
              </form>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default AccountUpdate;
