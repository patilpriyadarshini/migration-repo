import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { useNavigate, useLocation } from 'react-router-dom';
import { useMutation, useQuery } from '@tanstack/react-query';
import { cardUpdateSchema } from '../../utils/validation';
import { cardApi } from '../../services/api';
import { CardUpdateRequest } from '../../types/api';


const CardUpdate: React.FC = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const [cardNumber, setCardNumber] = useState(location.state?.cardNumber || '');
  const [success, setSuccess] = useState<string>('');

  const { data: card, isLoading: isLoadingCard, error: cardError, refetch } = useQuery({
    queryKey: ['card', cardNumber],
    queryFn: () => cardApi.getCard(cardNumber),
    enabled: !!cardNumber && cardNumber.length === 16,
  });

  const updateMutation = useMutation({
    mutationFn: (data: CardUpdateRequest) => cardApi.updateCard(cardNumber, data),
    onSuccess: () => {
      setSuccess('Card updated successfully');
      refetch();
    },
  });

  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
    reset,
  } = useForm<CardUpdateRequest>({
    resolver: yupResolver(cardUpdateSchema),
    mode: 'onChange',
  });

  useEffect(() => {
    if (card) {
      reset({
        cardName: card.cardName,
        cardStatus: card.cardStatus,
        expiryMonth: card.expiryMonth,
        expiryYear: card.expiryYear,
      });
    }
  }, [card, reset]);

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (cardNumber.length === 16) {
      setSuccess('');
      refetch();
    }
  };

  const onSubmit = (data: CardUpdateRequest) => {
    setSuccess('');
    updateMutation.mutate(data);
  };

  const handleBack = () => {
    navigate('/cards');
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-blue-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">Card Update</span>
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
            <h2 className="text-xl font-semibold text-gray-900">Card Update</h2>
          </div>
          
          <div className="p-6">
            {!card && (
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
                    disabled={isLoadingCard || cardNumber.length !== 16}
                    className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
                  >
                    {isLoadingCard ? 'Searching...' : 'Search'}
                  </button>
                </div>
              </form>
            )}

            {cardError && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">Card not found or error occurred</p>
              </div>
            )}

            {updateMutation.error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">
                  {(updateMutation.error as any).response?.data?.message || 'Update failed'}
                </p>
              </div>
            )}

            {success && (
              <div className="mb-6 bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded">
                <p className="text-sm">{success}</p>
              </div>
            )}

            {card && (
              <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                <div className="bg-gray-50 rounded-lg p-4 mb-6">
                  <h3 className="text-lg font-medium text-gray-900 mb-2">Card Information</h3>
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-4 text-sm">
                    <div>
                      <span className="text-gray-600">Card Number:</span>
                      <span className="ml-2 font-mono">{card.cardNum}</span>
                    </div>
                    <div>
                      <span className="text-gray-600">Account ID:</span>
                      <span className="ml-2 font-mono">{card.acctId}</span>
                    </div>
                  </div>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Card Name <span className="text-red-500">*</span>
                    </label>
                    <input
                      {...register('cardName')}
                      type="text"
                      maxLength={50}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    />
                    {errors.cardName && (
                      <p className="mt-1 text-sm text-red-600">{errors.cardName.message}</p>
                    )}
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Card Status <span className="text-red-500">*</span>
                    </label>
                    <select
                      {...register('cardStatus')}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    >
                      <option value="Y">Active</option>
                      <option value="N">Inactive</option>
                    </select>
                    {errors.cardStatus && (
                      <p className="mt-1 text-sm text-red-600">{errors.cardStatus.message}</p>
                    )}
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Expiry Month <span className="text-red-500">*</span>
                    </label>
                    <select
                      {...register('expiryMonth')}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    >
                      {Array.from({ length: 12 }, (_, i) => {
                        const month = (i + 1).toString().padStart(2, '0');
                        return (
                          <option key={month} value={month}>
                            {month}
                          </option>
                        );
                      })}
                    </select>
                    {errors.expiryMonth && (
                      <p className="mt-1 text-sm text-red-600">{errors.expiryMonth.message}</p>
                    )}
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-1">
                      Expiry Year <span className="text-red-500">*</span>
                    </label>
                    <select
                      {...register('expiryYear')}
                      className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                    >
                      {Array.from({ length: 10 }, (_, i) => {
                        const year = (new Date().getFullYear() + i).toString();
                        return (
                          <option key={year} value={year}>
                            {year}
                          </option>
                        );
                      })}
                    </select>
                    {errors.expiryYear && (
                      <p className="mt-1 text-sm text-red-600">{errors.expiryYear.message}</p>
                    )}
                  </div>
                </div>

                <div className="flex justify-end space-x-4">
                  <button
                    type="button"
                    onClick={() => setCardNumber('')}
                    className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
                  >
                    Search Another
                  </button>
                  <button
                    type="submit"
                    disabled={updateMutation.isPending || isSubmitting}
                    className="px-6 py-2 bg-green-600 text-white rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:opacity-50"
                  >
                    {updateMutation.isPending || isSubmitting ? 'Updating...' : 'Update Card'}
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

export default CardUpdate;
