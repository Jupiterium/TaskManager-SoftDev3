import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import CreateUpdateTaskScreen from '../../src/components/CreateUpdateTaskScreen'

const mockNavigate = vi.fn()
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useParams: () => ({ listId: '1', taskId: undefined })
  }
})

vi.mock('../../src/AppProvider', () => ({
  useAppContext: () => ({
    state: { 
      taskLists: [{ id: '1', title: 'Test List' }],
      tasks: {}
    },
    api: {
      createTask: vi.fn().mockResolvedValue({}),
      updateTask: vi.fn().mockResolvedValue({}),
      getTask: vi.fn().mockResolvedValue({}),
      getTaskList: vi.fn().mockResolvedValue({})
    }
  })
}))

const renderWithRouter = (component: React.ReactElement) => {
  return render(<BrowserRouter>{component}</BrowserRouter>)
}

describe('CreateUpdateTaskScreen', () => {
  it('renders create mode by default', async () => {
    renderWithRouter(<CreateUpdateTaskScreen />)
    expect(await screen.findByRole('heading', { name: /create task/i })).toBeInTheDocument()

    expect(screen.getByRole('button', { name: /create task/i })).toBeInTheDocument()
  })

  it('allows input in title field', async () => {
    renderWithRouter(<CreateUpdateTaskScreen />)
    const titleInput = await screen.findByLabelText(/title/i)
    fireEvent.change(titleInput, { target: { value: 'New Task' } })
    expect(titleInput).toHaveValue('New Task')
  })

  it('allows input in description field', async () => {
    renderWithRouter(<CreateUpdateTaskScreen />)
    const descInput = await screen.findByLabelText(/description/i)
    fireEvent.change(descInput, { target: { value: 'New Description' } })
    expect(descInput).toHaveValue('New Description')
  })

  it('allows priority selection', async () => {
    renderWithRouter(<CreateUpdateTaskScreen />)
    const highPriorityChip = await screen.findByText('HIGH Priority')
    fireEvent.click(highPriorityChip)
    expect(highPriorityChip).toBeInTheDocument()
  })
})