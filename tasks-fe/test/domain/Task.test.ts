import { describe, it, expect } from 'vitest'
import Task from '../../src/domain/Task'
import { TaskPriority } from '../../src/domain/TaskPriority'
import { TaskStatus } from '../../src/domain/TaskStatus'

describe('Task', () => {
  it('should create a task with required properties', () => {
    const task: Task = {
      id: '1',
      title: 'Test Task',
      description: 'Test Description',
      dueDate: new Date('2024-12-31'),
      priority: TaskPriority.HIGH,
      status: TaskStatus.OPEN
    }

    expect(task.id).toBe('1')
    expect(task.title).toBe('Test Task')
    expect(task.description).toBe('Test Description')
    expect(task.priority).toBe(TaskPriority.HIGH)
    expect(task.status).toBe(TaskStatus.OPEN)
  })

  it('should allow undefined values for optional properties', () => {
    const task: Task = {
      id: undefined,
      title: 'Test Task',
      description: 'Test Description',
      dueDate: undefined,
      priority: TaskPriority.LOW,
      status: undefined
    }

    expect(task.id).toBeUndefined()
    expect(task.dueDate).toBeUndefined()
    expect(task.status).toBeUndefined()
  })
})