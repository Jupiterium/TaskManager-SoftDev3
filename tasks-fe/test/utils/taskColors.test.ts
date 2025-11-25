import { describe, it, expect } from 'vitest'
import { getTaskStatusColor, getPriorityColor } from '../../src/utils/taskColors'
import { TaskStatus } from '../../src/domain/TaskStatus'
import { TaskPriority } from '../../src/domain/TaskPriority'

describe('taskColors', () => {
  describe('getTaskStatusColor', () => {
    it('returns green for closed tasks', () => {
      const task = { status: TaskStatus.CLOSED }
      expect(getTaskStatusColor(task)).toBe('bg-green-100 border-green-300')
    })

    it('returns red for overdue tasks', () => {
      const yesterday = new Date()
      yesterday.setDate(yesterday.getDate() - 1)
      const task = { status: TaskStatus.OPEN, dueDate: yesterday }
      expect(getTaskStatusColor(task)).toBe('bg-red-100 border-red-300')
    })

    it('returns yellow for tasks due within 24 hours', () => {
      const tomorrow = new Date()
      tomorrow.setHours(tomorrow.getHours() + 12)
      const task = { status: TaskStatus.OPEN, dueDate: tomorrow }
      expect(getTaskStatusColor(task)).toBe('bg-yellow-100 border-yellow-300')
    })

    it('returns white for normal tasks', () => {
      const task = { status: TaskStatus.OPEN }
      expect(getTaskStatusColor(task)).toBe('bg-white border-gray-200')
    })

    it('returns white for tasks due in more than 24 hours', () => {
      const nextWeek = new Date()
      nextWeek.setDate(nextWeek.getDate() + 7)
      const task = { status: TaskStatus.OPEN, dueDate: nextWeek }
      expect(getTaskStatusColor(task)).toBe('bg-white border-gray-200')
    })
  })

  describe('getPriorityColor', () => {
    it('returns red for high priority', () => {
      expect(getPriorityColor(TaskPriority.HIGH)).toBe('bg-red-500')
    })

    it('returns yellow for medium priority', () => {
      expect(getPriorityColor(TaskPriority.MEDIUM)).toBe('bg-yellow-500')
    })

    it('returns green for low priority', () => {
      expect(getPriorityColor(TaskPriority.LOW)).toBe('bg-green-500')
    })
  })
})