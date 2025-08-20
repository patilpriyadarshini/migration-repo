import React from 'react'
import { render, screen, fireEvent, waitFor } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { describe, it, expect, vi, beforeEach } from 'vitest'
import AccountView from '../AccountView'

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

describe('AccountView', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    localStorage.clear()
  })

  it('renders account search form', () => {
    renderWithProviders(<AccountView />)
    
    expect(screen.getByText('CardDemo')).toBeInTheDocument()
    expect(screen.getByText('Account View')).toBeInTheDocument()
    expect(screen.getByText('Account Search')).toBeInTheDocument()
    expect(screen.getByLabelText(/account id/i)).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /search/i })).toBeInTheDocument()
  })

  it('validates account ID format', async () => {
    renderWithProviders(<AccountView />)
    
    const accountIdInput = screen.getByLabelText(/account id/i)
    fireEvent.change(accountIdInput, { target: { value: '123' } })
    
    const searchButton = screen.getByRole('button', { name: /search/i })
    fireEvent.click(searchButton)
    
    await waitFor(() => {
      expect(screen.getByText('Account ID must be exactly 11 digits')).toBeInTheDocument()
    })
  })

  it('displays account information for valid account ID', async () => {
    renderWithProviders(<AccountView />)
    
    const accountIdInput = screen.getByLabelText(/account id/i)
    const searchButton = screen.getByRole('button', { name: /search/i })
    
    fireEvent.change(accountIdInput, { target: { value: '' } })
    fireEvent.change(accountIdInput, { target: { value: '12345678901' } })
    
    await waitFor(() => {
      expect(accountIdInput).toHaveValue('12345678901')
    })
    
    fireEvent.click(searchButton)
    
    await waitFor(() => {
      expect(screen.getByText('Account Information')).toBeInTheDocument()
      expect(screen.getByText(/Alice/)).toBeInTheDocument()
      expect(screen.getByText(/Johnson/)).toBeInTheDocument()
      expect(screen.getByText(/1500\.00/)).toBeInTheDocument()
      expect(screen.getByText('Active')).toBeInTheDocument()
    }, { timeout: 5000 })
  })

  it('displays error message for invalid account ID', async () => {
    renderWithProviders(<AccountView />)
    
    const accountIdInput = screen.getByLabelText(/account id/i)
    fireEvent.change(accountIdInput, { target: { value: '99999999999' } })
    
    const searchButton = screen.getByRole('button', { name: /search/i })
    fireEvent.click(searchButton)
    
    await waitFor(() => {
      expect(screen.getByText('Account ID NOT found')).toBeInTheDocument()
    })
  })

  it('navigates back to correct menu based on user type', () => {
    localStorage.setItem('userType', 'A')
    renderWithProviders(<AccountView />)
    
    const backButton = screen.getByRole('button', { name: /back to menu/i })
    fireEvent.click(backButton)
    
    expect(mockNavigate).toHaveBeenCalledWith('/admin-menu')
  })

  it('navigates to account update with account data', async () => {
    renderWithProviders(<AccountView />)
    
    const accountIdInput = screen.getByLabelText(/account id/i)
    fireEvent.change(accountIdInput, { target: { value: '12345678901' } })
    
    const searchButton = screen.getByRole('button', { name: /search/i })
    fireEvent.click(searchButton)
    
    await waitFor(() => {
      const updateButton = screen.getByRole('button', { name: /update account/i })
      fireEvent.click(updateButton)
      
      expect(mockNavigate).toHaveBeenCalledWith('/accounts/update', {
        state: { accountId: 12345678901 }
      })
    })
  })
})
