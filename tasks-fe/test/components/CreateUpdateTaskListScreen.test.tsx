import { describe, it, expect, vi } from 'vitest'
import { render, screen, fireEvent } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import CreateUpdateTaskListScreen from '../../src/components/CreateUpdateTaskListScreen'

const mockNavigate = vi.fn()
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useParams: () => ({ listId: undefined })
  }
})

vi.mock('../../src/AppProvider', () => ({
  useAppContext: () => ({
    state: { taskLists: [] },
    api: {
      createTaskList: vi.fn().mockResolvedValue({}),
      updateTaskList: vi.fn().mockResolvedValue({}),
      fetchTaskLists: vi.fn().mockResolvedValue({})
    }
  })
}))

const renderWithRouter = (component: React.ReactElement) => {
  return render(<BrowserRouter>{component}</BrowserRouter>)
}

describe('CreateUpdateTaskListScreen', () => {
  it('renders create mode by default', async () => {
    renderWithRouter(<CreateUpdateTaskListScreen />)
    expect(await screen.findByRole('heading', { name: /new task list/i })).toBeInTheDocument()
    expect(await screen.findByRole('button', { name: /create task list/i })).toBeInTheDocument()
  })

  it('allows input in title field', () => {
    renderWithRouter(<CreateUpdateTaskListScreen />)
    const titleInput = screen.getByLabelText(/title/i)
    fireEvent.change(titleInput, { target: { value: 'New List' } })
    expect(titleInput).toHaveValue('New List')
  })

  it('allows input in description field', () => {
    renderWithRouter(<CreateUpdateTaskListScreen />)
    const descInput = screen.getByLabelText(/description/i)
    fireEvent.change(descInput, { target: { value: 'New Description' } })
    expect(descInput).toHaveValue('New Description')
  })

  it('navigates back when back button is clicked', async () => {
    renderWithRouter(<CreateUpdateTaskListScreen />)
    const backButton = screen.getAllByRole('button')[0] // First button is the back button
    fireEvent.click(backButton)
    expect(mockNavigate).toHaveBeenCalledWith('/')
  })
})