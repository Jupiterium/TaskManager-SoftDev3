import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen, waitFor } from '@testing-library/react'
import { AppProvider, useAppContext } from '../src/AppProvider'
import axios from 'axios'

vi.mock('axios')
const mockedAxios = vi.mocked(axios)

const TestComponent = () => {
  const { state, api } = useAppContext()
  return (
    <div>
      <div data-testid="task-lists-count">{state.taskLists.length}</div>
      <button onClick={() => api.fetchTaskLists()}>Fetch</button>
    </div>
  )
}

describe('AppProvider', () => {
  beforeEach(() => {
    vi.clearAllMocks()
    mockedAxios.get.mockResolvedValue({ data: [] })
  })

  it('provides initial state', () => {
    render(
      <AppProvider>
        <TestComponent />
      </AppProvider>
    )
    
    expect(screen.getByTestId('task-lists-count')).toHaveTextContent('0')
  })

  it('throws error when useAppContext is used outside provider', () => {
    const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {})
    
    expect(() => render(<TestComponent />)).toThrow(
      'useAppContext must be used within an AppProvider'
    )
    
    consoleSpy.mockRestore()
  })

  it('fetches task lists on mount', async () => {
    mockedAxios.get.mockResolvedValue({ 
      data: [{ id: '1', title: 'Test List' }] 
    })

    render(
      <AppProvider>
        <TestComponent />
      </AppProvider>
    )

    await waitFor(() => {
      expect(mockedAxios.get).toHaveBeenCalledWith('/api/task-lists', {
        headers: { 'Content-Type': 'application/json' }
      })
    })
  })
})