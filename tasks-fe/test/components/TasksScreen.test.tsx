import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import TasksScreen from '../../src/components/TasksScreen'
import { TaskStatus } from '../../src/domain/TaskStatus'
import { TaskPriority } from '../../src/domain/TaskPriority'

const mockNavigate = vi.fn()
const mockApi = {
  getTaskList: vi.fn().mockResolvedValue({}),
  fetchTasks: vi.fn().mockResolvedValue({}),
  updateTask: vi.fn().mockResolvedValue({}),
  deleteTask: vi.fn().mockResolvedValue({}),
  deleteTaskList: vi.fn().mockResolvedValue({})
}

vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useParams: () => ({ listId: '1' })
  }
})

vi.mock('../../src/AppProvider', () => ({
  useAppContext: () => ({
    state: {
      taskLists: [{ id: '1', title: 'Test List', count: 2, progress: 0.5 }],
      tasks: {
        '1': [
          {
            id: '1',
            title: 'Test Task',
            description: 'Test Description',
            priority: TaskPriority.HIGH,
            status: TaskStatus.OPEN,
            dueDate: new Date('2024-12-31')
          }
        ]
      }
    },
    api: mockApi
  })
}))

const renderWithRouter = (component: React.ReactElement) => {
  return render(<BrowserRouter>{component}</BrowserRouter>)
}

describe('TasksScreen', () => {
  it('renders task list title', async () => {
    renderWithRouter(<TasksScreen />)
    expect(await screen.findByText('Test List')).toBeInTheDocument()
  })

  it('renders add task button', async () => {
    renderWithRouter(<TasksScreen />)
    expect(await screen.findByText('Add Task')).toBeInTheDocument()
  })

  it('renders task in table', async () => {
    renderWithRouter(<TasksScreen />)
    expect(await screen.findByText('Test Task')).toBeInTheDocument()
    expect(screen.getByText('HIGH')).toBeInTheDocument()
  })

  it('toggles task status when checkbox is clicked', async () => {
    renderWithRouter(<TasksScreen />)
    const checkbox = await screen.findByRole('checkbox')
    fireEvent.click(checkbox)
    expect(mockApi.updateTask).toHaveBeenCalled()
  })

  it('navigates to add task when add button is clicked', async () => {
    renderWithRouter(<TasksScreen />)
    const addButton = await screen.findByText('Add Task')
    fireEvent.click(addButton)
    expect(mockNavigate).toHaveBeenCalledWith('/task-lists/1/new-task')
  })

  it('deletes task list when delete button is clicked', async () => {
    renderWithRouter(<TasksScreen />)
    const deleteButton = await screen.findByText('Delete TaskList')
    fireEvent.click(deleteButton)
    expect(mockApi.deleteTaskList).toHaveBeenCalledWith('1')
  })
})