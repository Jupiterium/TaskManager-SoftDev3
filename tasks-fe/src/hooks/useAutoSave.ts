export const useAutoSave = (key: string) => {
  const getSavedData = () => {
    try {
      const saved = localStorage.getItem(`autosave_${key}`);
      return saved ? JSON.parse(saved) : null;
    } catch {
      return null;
    }
  };

  const clearSavedData = () => {
    try {
      localStorage.removeItem(`autosave_${key}`);
    } catch {
      // Ignore errors
    }
  };

  return { getSavedData, clearSavedData };
};