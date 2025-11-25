import { describe, it, expect } from 'vitest'
import TaskList from '../../src/domain/TaskList'

describe('TaskList', () => {
  it('should create a task list with all properties', () => {
    const taskList: TaskList = {
      id: '1',
      title: 'Test List',
      description: 'Test Description',
      count: 5,
      progress: 0.8,
      tasks: []
    }

    expect(taskList.id).toBe('1')
    expect(taskList.title).toBe('Test List')
    expect(taskList.description).toBe('Test Description')
    expect(taskList.count).toBe(5)
    expect(taskList.progress).toBe(0.8)
    expect(taskList.tasks).toEqual([])
  })

  it('should allow undefined values for optional properties', () => {
    const taskList: TaskList = {
      id: undefined,
      title: 'Test List',
      description: undefined,
      count: undefined,
      progress: undefined,
      tasks: undefined
    }

    expect(taskList.id).toBeUndefined()
    expect(taskList.description).toBeUndefined()
    expect(taskList.count).toBeUndefined()
    expect(taskList.progress).toBeUndefined()
    expect(taskList.tasks).toBeUndefined()
  })
})