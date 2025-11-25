import { describe, it, expect, vi } from 'vitest'
import { renderHook } from '@testing-library/react'
import { useKeyboardShortcuts } from '../../src/hooks/useKeyboardShortcuts'

describe('useKeyboardShortcuts', () => {
  it('calls onEscape when Escape key is pressed', () => {
    const onEscape = vi.fn()
    renderHook(() => useKeyboardShortcuts({ onEscape }))

    const event = new KeyboardEvent('keydown', { key: 'Escape' })
    document.dispatchEvent(event)

    expect(onEscape).toHaveBeenCalledTimes(1)
  })

  it('calls onAltEnter when Alt+Enter is pressed', () => {
    const onAltEnter = vi.fn()
    renderHook(() => useKeyboardShortcuts({ onAltEnter }))

    const event = new KeyboardEvent('keydown', { key: 'Enter', altKey: true })
    document.dispatchEvent(event)

    expect(onAltEnter).toHaveBeenCalledTimes(1)
  })

  it('calls onAltN when Alt+N is pressed', () => {
    const onAltN = vi.fn()
    renderHook(() => useKeyboardShortcuts({ onAltN }))

    const event = new KeyboardEvent('keydown', { key: 'n', altKey: true })
    document.dispatchEvent(event)

    expect(onAltN).toHaveBeenCalledTimes(1)
  })

  it('calls onAltS when Alt+S is pressed', () => {
    const onAltS = vi.fn()
    renderHook(() => useKeyboardShortcuts({ onAltS }))

    const event = new KeyboardEvent('keydown', { key: 's', altKey: true })
    document.dispatchEvent(event)

    expect(onAltS).toHaveBeenCalledTimes(1)
  })

  it('does not call handlers when keys are pressed without modifiers', () => {
    const onAltEnter = vi.fn()
    const onAltN = vi.fn()
    const onAltS = vi.fn()
    renderHook(() => useKeyboardShortcuts({ onAltEnter, onAltN, onAltS }))

    document.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }))
    document.dispatchEvent(new KeyboardEvent('keydown', { key: 'n' }))
    document.dispatchEvent(new KeyboardEvent('keydown', { key: 's' }))

    expect(onAltEnter).not.toHaveBeenCalled()
    expect(onAltN).not.toHaveBeenCalled()
    expect(onAltS).not.toHaveBeenCalled()
  })

  it('cleans up event listeners on unmount', () => {
    const removeEventListenerSpy = vi.spyOn(document, 'removeEventListener')
    const { unmount } = renderHook(() => useKeyboardShortcuts({}))

    unmount()

    expect(removeEventListenerSpy).toHaveBeenCalledWith('keydown', expect.any(Function))
  })
})