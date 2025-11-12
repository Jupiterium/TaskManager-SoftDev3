import { describe, it, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import TaskListScreen from '../../src/components/TaskListsScreen'

// Mock the AppProvider
vi.mock('../../src/AppProvider', () => ({
  useAppContext: () => ({
    state: {
      taskLists: [
        { id: '1', title: 'Test List', count: 5, progress: 0.6 }
      ]
    },
    api: {
      fetchTaskLists: vi.fn()
    }
  })
}))

const renderWithRouter = (component: React.ReactElement) => {
  return render(
    <BrowserRouter>
      {component}
    </BrowserRouter>
  )
}

describe('TaskListScreen', () => {
  it('renders task lists screen with title', () => {
    renderWithRouter(<TaskListScreen />)
    expect(screen.getByText('My Task Lists')).toBeInTheDocument()
  })

  it('renders create new task list button', () => {
    renderWithRouter(<TaskListScreen />)
    expect(screen.getByRole('button', { name: /create new task list/i })).toBeInTheDocument()
  })

  it('displays task list with correct information', () => {
    renderWithRouter(<TaskListScreen />)
    expect(screen.getByText('Test List')).toBeInTheDocument()
    expect(screen.getByText('5 tasks')).toBeInTheDocument()
  })
})