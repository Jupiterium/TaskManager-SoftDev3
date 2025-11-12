import { describe, it, expect } from 'vitest'
import { TaskStatus, isTaskStatus } from '../../src/domain/TaskStatus'

describe('TaskStatus', () => {
  it('should have correct enum values', () => {
    expect(TaskStatus.OPEN).toBe('OPEN')
    expect(TaskStatus.CLOSED).toBe('CLOSED')
  })

  describe('isTaskStatus', () => {
    it('should return true for valid TaskStatus values', () => {
      expect(isTaskStatus('OPEN')).toBe(true)
      expect(isTaskStatus('CLOSED')).toBe(true)
    })

    it('should return false for invalid values', () => {
      expect(isTaskStatus('INVALID')).toBe(false)
      expect(isTaskStatus(null)).toBe(false)
      expect(isTaskStatus(undefined)).toBe(false)
      expect(isTaskStatus(123)).toBe(false)
    })
  })
})