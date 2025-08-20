import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import { useNavigate } from 'react-router-dom';
import { useMutation } from '@tanstack/react-query';
import { reportSchema } from '../../utils/validation';
import { reportApi } from '../../services/api';
import { ReportRequest } from '../../types/api';

const ReportGeneration: React.FC = () => {
  const navigate = useNavigate();
  const [success, setSuccess] = useState<string>('');

  const getCurrentDate = () => {
    return new Date().toISOString().split('T')[0];
  };

  const getFirstDayOfMonth = () => {
    const now = new Date();
    return new Date(now.getFullYear(), now.getMonth(), 1).toISOString().split('T')[0];
  };

  const getFirstDayOfYear = () => {
    const now = new Date();
    return new Date(now.getFullYear(), 0, 1).toISOString().split('T')[0];
  };

  const reportMutation = useMutation({
    mutationFn: ({ type, data }: { type: string; data: ReportRequest }) => {
      if (type === 'MONTHLY') return reportApi.generateMonthlyReport(data);
      if (type === 'YEARLY') return reportApi.generateYearlyReport(data);
      return reportApi.generateCustomReport(data);
    },
    onSuccess: (response: any) => {
      setSuccess(`Report generated successfully. Report ID: ${response.reportId}`);
    },
  });

  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
    setValue,
  } = useForm<ReportRequest>({
    resolver: yupResolver(reportSchema),
    defaultValues: {
      reportType: 'monthly',
      startDate: getFirstDayOfMonth(),
      endDate: getCurrentDate(),
    },
  });

  const watchedReportType = watch('reportType');

  const onSubmit = (data: ReportRequest) => {
    setSuccess('');
    const transformedData = {
      ...data,
      reportType: data.reportType.toUpperCase(),
      confirmation: 'Y'
    };
    reportMutation.mutate({ type: transformedData.reportType, data: transformedData });
  };

  const handleBack = () => {
    const userType = localStorage.getItem('userType');
    if (userType === 'A') {
      navigate('/admin-menu');
    } else {
      navigate('/menu');
    }
  };


  const handleReportTypeChange = (type: string) => {
    setValue('reportType', type);
    
    if (type === 'monthly') {
      setValue('startDate', getFirstDayOfMonth());
      setValue('endDate', getCurrentDate());
    } else if (type === 'yearly') {
      setValue('startDate', getFirstDayOfYear());
      setValue('endDate', getCurrentDate());
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <div className="bg-blue-600 text-white shadow-lg">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center py-4">
            <div className="flex items-center">
              <h1 className="text-2xl font-bold">CardDemo</h1>
              <span className="ml-4 text-lg">Report Generation</span>
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
            <h2 className="text-xl font-semibold text-gray-900">Generate Transaction Report</h2>
          </div>
          
          <div className="p-6">
            {reportMutation.error && (
              <div className="mb-6 bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded">
                <p className="text-sm">
                  {(reportMutation.error as any).response?.data?.message || 'Report generation failed'}
                </p>
              </div>
            )}

            {success && (
              <div className="mb-6 bg-green-50 border border-green-200 text-green-700 px-4 py-3 rounded">
                <p className="text-sm">{success}</p>
              </div>
            )}

            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
              <div>
                <label className="block text-sm font-medium text-gray-700 mb-3">
                  Report Type <span className="text-red-500">*</span>
                </label>
                <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                  <label className="flex items-center p-4 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-50">
                    <input
                      {...register('reportType')}
                      type="radio"
                      value="monthly"
                      onChange={() => handleReportTypeChange('monthly')}
                      className="mr-3"
                    />
                    <div>
                      <div className="font-medium">Monthly Report</div>
                      <div className="text-sm text-gray-600">Current month transactions</div>
                    </div>
                  </label>
                  
                  <label className="flex items-center p-4 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-50">
                    <input
                      {...register('reportType')}
                      type="radio"
                      value="yearly"
                      onChange={() => handleReportTypeChange('yearly')}
                      className="mr-3"
                    />
                    <div>
                      <div className="font-medium">Yearly Report</div>
                      <div className="text-sm text-gray-600">Current year transactions</div>
                    </div>
                  </label>
                  
                  <label className="flex items-center p-4 border border-gray-300 rounded-lg cursor-pointer hover:bg-gray-50">
                    <input
                      {...register('reportType')}
                      type="radio"
                      value="custom"
                      onChange={() => handleReportTypeChange('custom')}
                      className="mr-3"
                    />
                    <div>
                      <div className="font-medium">Custom Range</div>
                      <div className="text-sm text-gray-600">Specify date range</div>
                    </div>
                  </label>
                </div>
                {errors.reportType && (
                  <p className="mt-1 text-sm text-red-600">{errors.reportType.message}</p>
                )}
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    Start Date <span className="text-red-500">*</span>
                  </label>
                  <input
                    {...register('startDate')}
                    type="date"
                    max={getCurrentDate()}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {errors.startDate && (
                    <p className="mt-1 text-sm text-red-600">{errors.startDate.message}</p>
                  )}
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">
                    End Date <span className="text-red-500">*</span>
                  </label>
                  <input
                    {...register('endDate')}
                    type="date"
                    max={getCurrentDate()}
                    className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-blue-500 focus:border-blue-500"
                  />
                  {errors.endDate && (
                    <p className="mt-1 text-sm text-red-600">{errors.endDate.message}</p>
                  )}
                </div>
              </div>

              <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
                <h3 className="text-lg font-medium text-gray-900 mb-2">Report Information</h3>
                <div className="text-sm text-gray-600 space-y-1">
                  <p>• The report will include all transactions within the specified date range</p>
                  <p>• Transaction details include amounts, merchants, categories, and dates</p>
                  <p>• Reports are generated in real-time and may take a few moments to process</p>
                  <p>• Large date ranges may result in longer processing times</p>
                </div>
              </div>

              {watchedReportType && (
                <div className="bg-gray-50 border border-gray-200 rounded-lg p-4">
                  <h4 className="font-medium text-gray-900 mb-2">Report Preview</h4>
                  <div className="text-sm text-gray-600">
                    <p><strong>Type:</strong> {watchedReportType.charAt(0).toUpperCase() + watchedReportType.slice(1)} Report</p>
                    {watchedReportType === 'monthly' && (
                      <p><strong>Period:</strong> Current month ({getFirstDayOfMonth()} to {getCurrentDate()})</p>
                    )}
                    {watchedReportType === 'yearly' && (
                      <p><strong>Period:</strong> Current year ({getFirstDayOfYear()} to {getCurrentDate()})</p>
                    )}
                    {watchedReportType === 'custom' && (
                      <p><strong>Period:</strong> Custom date range (specify dates above)</p>
                    )}
                  </div>
                </div>
              )}

              <div className="flex justify-end space-x-4">
                <button
                  type="button"
                  onClick={() => {
                    setValue('reportType', 'monthly');
                    setValue('startDate', '');
                    setValue('endDate', '');
                    setSuccess('');
                  }}
                  className="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-blue-500"
                >
                  Reset Form
                </button>
                <button
                  type="submit"
                  disabled={reportMutation.isPending}
                  className="px-6 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50"
                >
                  {reportMutation.isPending ? 'Generating...' : 'Generate Report'}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ReportGeneration;
