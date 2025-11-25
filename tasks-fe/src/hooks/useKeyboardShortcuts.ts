import { useEffect } from 'react';

interface KeyboardShortcuts {
  onEscape?: () => void;
  onAltEnter?: () => void;
  onAltN?: () => void;
  onAltS?: () => void;
}

export const useKeyboardShortcuts = (shortcuts: KeyboardShortcuts) => {
  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === 'Escape' && shortcuts.onEscape) {
        event.preventDefault();
        shortcuts.onEscape();
      }
      
      if (event.key === 'Enter' && event.altKey && shortcuts.onAltEnter) {
        event.preventDefault();
        shortcuts.onAltEnter();
      }
      
      if (event.key === 'n' && event.altKey && shortcuts.onAltN) {
        event.preventDefault();
        shortcuts.onAltN();
      }
      
      if (event.key === 's' && event.altKey && shortcuts.onAltS) {
        event.preventDefault();
        shortcuts.onAltS();
      }
    };

    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [shortcuts]);
};