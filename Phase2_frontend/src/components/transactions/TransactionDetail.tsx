import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useNavigate, useLocation } from 'react-router-dom';
import { transactionApi } from '../../services/api';

const TransactionDetail: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [transactionId, setTransactionId] = useState(location.state?.transactionId || '');

  const { data: transaction, isLoading, error, refetch } = useQuery({
    queryKey: ['transaction', transactionId],
    queryFn: () => transactionApi.getTransaction(transactionId),
    enabled: !!transactionId,
  });

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (transactionId.trim()) {
      refetch();
    }
  };

  const handleBack = () => {
    navigate('/transactions');
  };

  const formatCurrency = (amount: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(amount);
  };

  const formatDateTime = (dateTime: string) => {
    return new Date(dateTime).toLocaleString();
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-blue-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">Transaction Details</span>
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
            <h2 className="text-xl font-semibold text-gray-900">Transaction Information</h2>
          </div>
          
          <div className="p-6">
            <form onSubmit={handleSearch} className="mb-6">
              <div className="flex items-end space-x-4">
                <div className="flex-1">
                  <label htmlFor="transactionId" className="block text-sm font-medium text-gray-700 mb-1">
                    Transaction ID <span className="text-red-500">*</span>
                  </label>
                  <input
                    type="text"
                    value={transactionId}
                    onChange={(e) => setTransactionId(e.target.value)}
                    placeholder="Enter transaction ID"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <button
                  type="submit"
                  disabled={isLoading || !transactionId.trim()}
                  className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
                >
                  {isLoading ? 'Searching...' : 'Search'}
                </button>
              </div>
            </form>

            {error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">Transaction not found or error occurred</p>
              </div>
            )}

            {transaction && (
              <div className="space-y-6">
                <div className="bg-gray-50 rounded-lg p-6">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Transaction Details</h3>
                  
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Transaction ID</label>
                        <p className="mt-1 text-lg font-mono bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {transaction.tranId}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Card Number</label>
                        <p className="mt-1 text-lg font-mono bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {transaction.cardNum}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Transaction Type</label>
                        <p className="mt-1 text-lg bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {transaction.tranTypeCd}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Category</label>
                        <p className="mt-1 text-lg bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {transaction.tranCatCd}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Source</label>
                        <p className="mt-1 text-lg bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {transaction.tranSource}
                        </p>
                      </div>
                    </div>

                    <div className="space-y-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Description</label>
                        <p className="mt-1 text-lg bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {transaction.tranDesc}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Amount</label>
                        <p className="mt-1 text-2xl font-bold text-green-600 bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {formatCurrency(transaction.tranAmt)}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Original Date</label>
                        <p className="mt-1 text-lg bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {formatDateTime(transaction.origTs)}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Processed Date</label>
                        <p className="mt-1 text-lg bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {formatDateTime(transaction.procTs)}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="bg-gray-50 rounded-lg p-6">
                  <h3 className="text-lg font-medium text-gray-900 mb-4">Merchant Information</h3>
                  
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Merchant ID</label>
                        <p className="mt-1 text-lg font-mono bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {transaction.merchantId}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Merchant Name</label>
                        <p className="mt-1 text-lg bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {transaction.merchantName}
                        </p>
                      </div>
                    </div>

                    <div className="space-y-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Merchant City</label>
                        <p className="mt-1 text-lg bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {transaction.merchantCity}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Merchant ZIP</label>
                        <p className="mt-1 text-lg font-mono bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {transaction.merchantZip}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="flex justify-end space-x-4">
                  <button
                    onClick={() => setTransactionId('')}
                    className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    Search Another
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

export default TransactionDetail;
