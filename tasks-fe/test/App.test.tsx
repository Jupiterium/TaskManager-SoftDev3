import { describe, it, expect, vi } from 'vitest'
import { render, screen } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import App from '../src/App'

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual, // Import all actual implementations
    // Override BrowserRouter to just render children (a fragment)
    // This prevents the "Router inside Router" error from App.tsx
    BrowserRouter: ({ children }: { children: React.ReactNode }) => <>{children}</>,
  }
})
// --- END OF MOCK ---

// Mock all components
vi.mock('../src/components/TaskListsScreen', () => ({
  default: () => <div>TaskLists Screen</div>
}))

vi.mock('../src/components/CreateUpdateTaskListScreen', () => ({
  default: () => <div>CreateUpdate TaskList Screen</div>
}))

vi.mock('../src/components/TasksScreen', () => ({
  default: () => <div>Tasks Screen</div>
}))

vi.mock('../src/components/CreateUpdateTaskScreen', () => ({
  default: () => <div>CreateUpdate Task Screen</div>
}))

describe('App', () => {
  it('renders TaskLists screen on root path', () => {
    render(
      <MemoryRouter initialEntries={['/']}>
        <App />
      </MemoryRouter>
    )
    expect(screen.getByText('TaskLists Screen')).toBeInTheDocument()
  })

  it('renders CreateUpdate TaskList screen on /new-task-list', () => {
    render(
      <MemoryRouter initialEntries={['/new-task-list']}>
        <App />
      </MemoryRouter>
    )
    expect(screen.getByText('CreateUpdate TaskList Screen')).toBeInTheDocument()
  })

  it('renders CreateUpdate TaskList screen on /edit-task-list/:listId', () => {
    render(
      <MemoryRouter initialEntries={['/edit-task-list/1']}>
        <App />
      </MemoryRouter>
    )
    expect(screen.getByText('CreateUpdate TaskList Screen')).toBeInTheDocument()
  })

  it('renders Tasks screen on /task-lists/:listId', () => {
    render(
      <MemoryRouter initialEntries={['/task-lists/1']}>
        <App />
      </MemoryRouter>
    )
    expect(screen.getByText('Tasks Screen')).toBeInTheDocument()
  })

  it('renders CreateUpdate Task screen on /task-lists/:listId/new-task', () => {
    render(
      <MemoryRouter initialEntries={['/task-lists/1/new-task']}>
        <App />
      </MemoryRouter>
    )
    expect(screen.getByText('CreateUpdate Task Screen')).toBeInTheDocument()
  })

  it('renders CreateUpdate Task screen on /task-lists/:listId/edit-task/:taskId', () => {
    render(
      <MemoryRouter initialEntries={['/task-lists/1/edit-task/1']}>
        <App />
      </MemoryRouter>
    )
    expect(screen.getByText('CreateUpdate Task Screen')).toBeInTheDocument()
  })
})