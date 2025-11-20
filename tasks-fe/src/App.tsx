import React from "react";
import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AppProvider, useAppContext } from "./AppProvider";
import TaskLists from "./components/TaskListsScreen";
import CreateUpdateTaskListScreen from "./components/CreateUpdateTaskListScreen";
import TaskListScreen from "./components/TasksScreen";
import CreateUpdateTaskScreen from "./components/CreateUpdateTaskScreen";
import NotificationPanel from "./components/NotificationPanel";
import OfflineBanner from "./components/OfflineBanner";
import BackOnlineBanner from "./components/BackOnlineBanner";
import { NextUIProvider } from "@nextui-org/react";

const AppContent: React.FC = () => {
  const { isOnline, showBackOnline } = useAppContext();
  
  return (
    <BrowserRouter>
      <OfflineBanner isVisible={!isOnline} />
      <BackOnlineBanner isVisible={showBackOnline} />
      <div className="fixed top-4 right-4 z-50">
        <NotificationPanel />
      </div>
      <Routes>
        <Route path="/" element={<TaskLists />} />
        <Route path="/new-task-list" element={<CreateUpdateTaskListScreen />} />
        <Route
          path="/edit-task-list/:listId"
          element={<CreateUpdateTaskListScreen />}
        />
        <Route path="/task-lists/:listId" element={<TaskListScreen />} />
        <Route
          path="/task-lists/:listId/new-task"
          element={<CreateUpdateTaskScreen />}
        />
        <Route
          path="/task-lists/:listId/edit-task/:taskId"
          element={<CreateUpdateTaskScreen />}
        />
      </Routes>
    </BrowserRouter>
  );
};

function App() {
  return (
    <NextUIProvider>
      <AppProvider>
        <AppContent />
      </AppProvider>
    </NextUIProvider>
  );
}

export default App;
