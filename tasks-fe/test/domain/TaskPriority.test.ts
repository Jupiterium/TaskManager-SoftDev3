import { describe, it, expect } from 'vitest'
import { TaskPriority, isTaskPriority } from '../../src/domain/TaskPriority'

describe('TaskPriority', () => {
  it('should have correct enum values', () => {
    expect(TaskPriority.HIGH).toBe('HIGH')
    expect(TaskPriority.MEDIUM).toBe('MEDIUM')
    expect(TaskPriority.LOW).toBe('LOW')
  })

  describe('isTaskPriority', () => {
    it('should return true for valid TaskPriority values', () => {
      expect(isTaskPriority('HIGH')).toBe(true)
      expect(isTaskPriority('MEDIUM')).toBe(true)
      expect(isTaskPriority('LOW')).toBe(true)
    })

    it('should return false for invalid values', () => {
      expect(isTaskPriority('INVALID')).toBe(false)
      expect(isTaskPriority(null)).toBe(false)
      expect(isTaskPriority(undefined)).toBe(false)
      expect(isTaskPriority(123)).toBe(false)
    })
  })
})