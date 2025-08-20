import React from 'react'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import LoginForm from '../LoginForm'

const createTestQueryClient = () => new QueryClient({
  defaultOptions: {
    queries: { retry: false },
    mutations: { retry: false },
  },
})

const renderWithProviders = (component: React.ReactElement) => {
  const queryClient = createTestQueryClient()
  return render(
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        {component}
      </BrowserRouter>
    </QueryClientProvider>
  )
}

const mockNavigate = vi.fn()
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  }
})

describe('LoginForm', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    localStorage.clear()
  })

  it('renders login form with required fields', () => {
    renderWithProviders(<LoginForm />)
    
    expect(screen.getByText('CardDemo System')).toBeInTheDocument()
    expect(screen.getByText('Sign in to your account')).toBeInTheDocument()
    expect(screen.getByRole('textbox', { name: /user id/i })).toBeInTheDocument()
    expect(screen.getByLabelText(/password/i)).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /sign in/i })).toBeInTheDocument()
  })

  it('validates required fields', async () => {
    renderWithProviders(<LoginForm />)
    
    const submitButton = screen.getByRole('button', { name: /sign in/i })
    fireEvent.click(submitButton)
    
    await waitFor(() => {
      expect(screen.getByText('User ID cannot be empty')).toBeInTheDocument()
      expect(screen.getByText('Password cannot be empty')).toBeInTheDocument()
    })
  })

  it('validates field length requirements', async () => {
    renderWithProviders(<LoginForm />)
    
    const userIdInput = screen.getByRole('textbox', { name: /user id/i })
    const passwordInput = screen.getByLabelText(/password/i)
    
    fireEvent.change(userIdInput, { target: { value: '123' } })
    fireEvent.change(passwordInput, { target: { value: '456' } })
    
    const submitButton = screen.getByRole('button', { name: /sign in/i })
    fireEvent.click(submitButton)
    
    await waitFor(() => {
      expect(screen.getByText('User ID must be exactly 8 characters')).toBeInTheDocument()
      expect(screen.getByText('Password must be exactly 8 characters')).toBeInTheDocument()
    })
  })

  it('successfully logs in admin user and navigates to admin menu', async () => {
    renderWithProviders(<LoginForm />)
    
    const userIdInput = screen.getByRole('textbox', { name: /user id/i })
    const passwordInput = screen.getByLabelText(/password/i)
    
    fireEvent.change(userIdInput, { target: { value: 'ADMIN001' } })
    fireEvent.change(passwordInput, { target: { value: 'admin123' } })
    
    const submitButton = screen.getByRole('button', { name: /sign in/i })
    fireEvent.click(submitButton)
    
    await waitFor(() => {
      expect(localStorage.getItem('userId')).toBe('ADMIN001')
      expect(localStorage.getItem('userType')).toBe('A')
      expect(mockNavigate).toHaveBeenCalledWith('/admin-menu')
    })
  })

  it('successfully logs in normal user and navigates to main menu', async () => {
    renderWithProviders(<LoginForm />)
    
    const userIdInput = screen.getByRole('textbox', { name: /user id/i })
    const passwordInput = screen.getByLabelText(/password/i)
    
    fireEvent.change(userIdInput, { target: { value: 'USER0001' } })
    fireEvent.change(passwordInput, { target: { value: 'user1234' } })
    
    const submitButton = screen.getByRole('button', { name: /sign in/i })
    fireEvent.click(submitButton)
    
    await waitFor(() => {
      expect(localStorage.getItem('userId')).toBe('USER0001')
      expect(localStorage.getItem('userType')).toBe('U')
      expect(mockNavigate).toHaveBeenCalledWith('/menu')
    })
  })

  it('displays error message for invalid credentials', async () => {
    renderWithProviders(<LoginForm />)
    
    const userIdInput = screen.getByRole('textbox', { name: /user id/i })
    const passwordInput = screen.getByLabelText(/password/i)
    
    fireEvent.change(userIdInput, { target: { value: 'INVALID1' } })
    fireEvent.change(passwordInput, { target: { value: 'invalid1' } })
    
    const submitButton = screen.getByRole('button', { name: /sign in/i })
    fireEvent.click(submitButton)
    
    await waitFor(() => {
      expect(screen.getByText('Invalid credentials')).toBeInTheDocument()
    })
  })
})
