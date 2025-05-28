import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';

const decodeToken = (token) => {
  try {
    // JWT tokens are in format: header.payload.signature
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(c => {
      return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));

    return JSON.parse(jsonPayload);
  } catch (error) {
    console.error('Error decoding token:', error);
    return null;
  }
};

const ProtectedRoute = ({ children, allowedRoles = [] }) => {
  const location = useLocation();
  const token = localStorage.getItem('token');
  console.log('Token:', token);

  // Decode token to get user info
  const decodedToken = token ? decodeToken(token) : null;
  const userRole = decodedToken?.role;
  const username = decodedToken?.sub; // 'sub' is the standard JWT claim for subject (username)

  console.log('ProtectedRoute Check:', {
    path: location.pathname,
    token: token ? 'Present' : 'Missing',
    decodedToken,
    userRole: userRole || 'Not set',
    username: username || 'Not set',
    allowedRoles: allowedRoles
  });

  // If no token, redirect to login
  if (!token) {
    console.log('Access Denied: No token found, redirecting to login');
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // If roles are specified and user's role is not allowed
  if (allowedRoles.length > 0 && !allowedRoles.includes(userRole)) {
    console.log('Access Denied: Invalid role', {
      userRole,
      allowedRoles,
      path: location.pathname
    });
    return <Navigate to="/unauthorized" replace />;
  }

  // If authenticated and authorized, render the protected component
  console.log('Access Granted:', {
    path: location.pathname,
    userRole,
    username,
    allowedRoles
  });
  return children;
};

export default ProtectedRoute; 