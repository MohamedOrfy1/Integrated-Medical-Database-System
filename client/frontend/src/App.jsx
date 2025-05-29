import React, { useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate, useLocation } from 'react-router-dom';
import Navbar from './components/Navbar';
import Login from './pages/Login';
import Home from './pages/Home';
import About from './pages/About';
import Department from './pages/Department';
import Receptionist from './pages/Receptionist';
import Hematology from './pages/Hematology';
import Unauthorized from './pages/Unauthorized';
import ProtectedRoute from './components/ProtectedRoute';
import DoctorDashboard from './pages/DoctorDashboard';
import Research from './pages/Research';
import './App.css';

// Route change logger component
const RouteLogger = () => {
  const location = useLocation();

  useEffect(() => {
    console.log('Route Change:', {
      path: location.pathname,
      state: location.state,
      timestamp: new Date().toISOString()
    });
  }, [location]);

  return null;
};

function App() {
  return (
    <Router>
      <div className="app">
        <Navbar />
        <RouteLogger />
        <main className="main-content">
          <Routes>
            {/* Public routes */}
            <Route path="/login" element={<Login />} />
            <Route path="/unauthorized" element={<Unauthorized />} />

            {/* Protected routes */}
            <Route
              path="/home"
              element={
                <ProtectedRoute>
                  <Home />
                </ProtectedRoute>
              }
            />
            <Route
              path="/about"
              element={
                <ProtectedRoute>
                  <About />
                </ProtectedRoute>
              }
            />
            <Route
              path="/department"
              element={
                <ProtectedRoute>
                  <Department />
                </ProtectedRoute>
              }
            />
            <Route
              path="/hematology"
              element={
                <ProtectedRoute allowedRoles={['DOC']}>
                  <Hematology />
                </ProtectedRoute>
              }
            />
            {/* <Route
              path="/receptionist"
              element={
                <ProtectedRoute allowedRoles={['EMP']}>
                  <Receptionist />
                </ProtectedRoute>
              }
            /> */}
            <Route
              path="/doctor-dashboard"
              element={
                <ProtectedRoute allowedRoles={['DOC']}>
                  <DoctorDashboard />
                </ProtectedRoute>
              }
            />

            {/* Redirect root to login */}
            <Route path="/" element={<Navigate to="/login" replace />} />
            {/* <Route path="*" element={<Navigate to="/unauthorized" replace />} /> */}
            <Route path='/research' element={ <Research />} />
            <Route path='/receptionist' element={ <Receptionist />} />

          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;
