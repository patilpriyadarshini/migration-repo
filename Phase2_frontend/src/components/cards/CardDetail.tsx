import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useNavigate, useLocation } from 'react-router-dom';
import { cardApi } from '../../services/api';


const CardDetail: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [cardNumber, setCardNumber] = useState(location.state?.cardNumber || '');

  const { data: card, isLoading, error, refetch } = useQuery({
    queryKey: ['card', cardNumber],
    queryFn: () => cardApi.getCard(cardNumber),
    enabled: !!cardNumber && cardNumber.length === 16,
  });

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (cardNumber.length === 16) {
      refetch();
    }
  };

  const handleBack = () => {
    navigate('/cards');
  };

  const handleUpdate = () => {
    if (card) {
      navigate('/cards/update', { state: { cardNumber: card.cardNum } });
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-blue-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">Card Details</span>
            </div>
            <button
              onClick={handleBack}
              className="bg-blue-700 hover:bg-blue-800 px-3 py-1 rounded text-sm"
            >
              Back to Card List
            </button>
          </div>
        </div>
      </div>

      <div className="max-w-4xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
        <div className="bg-white shadow rounded-lg">
          <div className="px-6 py-4 border-b border-gray-200">
            <h2 className="text-xl font-semibold text-gray-900">Card Information</h2>
          </div>
          
          <div className="p-6">
            <form onSubmit={handleSearch} className="mb-6">
              <div className="flex items-end space-x-4">
                <div className="flex-1">
                  <label htmlFor="cardNumber" className="block text-sm font-medium text-gray-700 mb-1">
                    Card Number <span className="text-red-500">*</span>
                  </label>
                  <input
                    type="text"
                    value={cardNumber}
                    onChange={(e) => setCardNumber(e.target.value)}
                    maxLength={16}
                    placeholder="16-digit card number"
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                </div>
                <button
                  type="submit"
                  disabled={isLoading || cardNumber.length !== 16}
                  className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
                >
                  {isLoading ? 'Searching...' : 'Search'}
                </button>
              </div>
            </form>

            {error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">Card not found or error occurred</p>
              </div>
            )}

            {card && (
              <div className="space-y-6">
                <div className="flex justify-between items-center">
                  <h3 className="text-lg font-medium text-gray-900">Card Details</h3>
                  <button
                    onClick={handleUpdate}
                    className="px-4 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    Update Card
                  </button>
                </div>

                <div className="bg-gray-50 rounded-lg p-6">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div className="space-y-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Card Number</label>
                        <p className="mt-1 text-lg font-mono bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {card.cardNum}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Account ID</label>
                        <p className="mt-1 text-lg font-mono bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {card.acctId}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Card Name</label>
                        <p className="mt-1 text-lg bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {card.cardName}
                        </p>
                      </div>
                    </div>

                    <div className="space-y-4">
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Card Status</label>
                        <div className="mt-1">
                          <span className={`inline-flex px-3 py-2 text-sm font-semibold rounded-md ${
                            card.cardStatus === 'Y' 
                              ? 'bg-green-100 text-green-800 border border-green-200' 
                              : 'bg-red-100 text-red-800 border border-red-200'
                          }`}>
                            {card.cardStatus === 'Y' ? 'Active' : 'Inactive'}
                          </span>
                        </div>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Expiry Month</label>
                        <p className="mt-1 text-lg font-mono bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {card.expiryMonth}
                        </p>
                      </div>
                      
                      <div>
                        <label className="block text-sm font-medium text-gray-700">Expiry Year</label>
                        <p className="mt-1 text-lg font-mono bg-white px-3 py-2 border border-gray-300 rounded-md">
                          {card.expiryYear}
                        </p>
                      </div>
                    </div>
                  </div>
                </div>

                <div className="flex justify-end space-x-4">
                  <button
                    onClick={() => setCardNumber('')}
                    className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    Search Another
                  </button>
                  <button
                    onClick={handleUpdate}
                    className="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500"
                  >
                    Update Card
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

export default CardDetail;
