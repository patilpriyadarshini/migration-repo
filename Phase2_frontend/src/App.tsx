import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import LoginForm from './components/auth/LoginForm';
import MainMenu from './components/menus/MainMenu';
import AdminMenu from './components/menus/AdminMenu';
import AccountView from './components/accounts/AccountView';
import AccountUpdate from './components/accounts/AccountUpdate';
import CardList from './components/cards/CardList';
import CardDetail from './components/cards/CardDetail';
import CardUpdate from './components/cards/CardUpdate';
import TransactionList from './components/transactions/TransactionList';
import TransactionDetail from './components/transactions/TransactionDetail';
import TransactionAdd from './components/transactions/TransactionAdd';
import BillPayment from './components/billing/BillPayment';
import ReportGeneration from './components/reports/ReportGeneration';
import UserList from './components/admin/UserList';
import UserAdd from './components/admin/UserAdd';
import UserUpdate from './components/admin/UserUpdate';
import UserDelete from './components/admin/UserDelete';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: 1,
      refetchOnWindowFocus: false,
    },
  },
});

const ProtectedRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const userId = localStorage.getItem('userId');
  return userId ? <>{children}</> : <Navigate to="/login" replace />;
};

const AdminRoute: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const userId = localStorage.getItem('userId');
  const userType = localStorage.getItem('userType');
  
  if (!userId) {
    return <Navigate to="/login" replace />;
  }
  
  if (userType !== 'A') {
    return <Navigate to="/menu" replace />;
  }
  
  return <>{children}</>;
};

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <Router>
        <div className="App">
          <Routes>
            <Route path="/login" element={<LoginForm />} />
            <Route
              path="/menu"
              element={
                <ProtectedRoute>
                  <MainMenu />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin-menu"
              element={
                <AdminRoute>
                  <AdminMenu />
                </AdminRoute>
              }
            />
            <Route
              path="/accounts/view"
              element={
                <ProtectedRoute>
                  <AccountView />
                </ProtectedRoute>
              }
            />
            <Route
              path="/accounts/update"
              element={
                <ProtectedRoute>
                  <AccountUpdate />
                </ProtectedRoute>
              }
            />
            <Route
              path="/cards"
              element={
                <ProtectedRoute>
                  <CardList />
                </ProtectedRoute>
              }
            />
            <Route
              path="/cards/detail"
              element={
                <ProtectedRoute>
                  <CardDetail />
                </ProtectedRoute>
              }
            />
            <Route
              path="/cards/update"
              element={
                <ProtectedRoute>
                  <CardUpdate />
                </ProtectedRoute>
              }
            />
            <Route
              path="/transactions"
              element={
                <ProtectedRoute>
                  <TransactionList />
                </ProtectedRoute>
              }
            />
            <Route
              path="/transactions/view"
              element={
                <ProtectedRoute>
                  <TransactionDetail />
                </ProtectedRoute>
              }
            />
            <Route
              path="/transactions/add"
              element={
                <ProtectedRoute>
                  <TransactionAdd />
                </ProtectedRoute>
              }
            />
            <Route
              path="/bill-payment"
              element={
                <ProtectedRoute>
                  <BillPayment />
                </ProtectedRoute>
              }
            />
            <Route
              path="/reports"
              element={
                <ProtectedRoute>
                  <ReportGeneration />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/users"
              element={
                <AdminRoute>
                  <UserList />
                </AdminRoute>
              }
            />
            <Route
              path="/admin/users/add"
              element={
                <AdminRoute>
                  <UserAdd />
                </AdminRoute>
              }
            />
            <Route
              path="/admin/users/update"
              element={
                <AdminRoute>
                  <UserUpdate />
                </AdminRoute>
              }
            />
            <Route
              path="/admin/users/delete"
              element={
                <AdminRoute>
                  <UserDelete />
                </AdminRoute>
              }
            />
            <Route path="/" element={<Navigate to="/login" replace />} />
            <Route path="*" element={<Navigate to="/login" replace />} />
          </Routes>
        </div>
      </Router>
    </QueryClientProvider>
  );
}

export default App;
