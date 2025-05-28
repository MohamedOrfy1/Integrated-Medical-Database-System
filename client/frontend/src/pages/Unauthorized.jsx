import React from 'react';
import { Link } from 'react-router-dom';
import '../styles/Unauthorized.css';

const Unauthorized = () => {
  return (
    <div className="unauthorized-container">
      <div className="unauthorized-content">
        <h1>401</h1>
        <h2>Unauthorized Access</h2>
        <p>Sorry, you don't have permission to access this page.</p>
        <Link to="/" className="back-button">Back to Home</Link>
      </div>
    </div>
  );
};

export default Unauthorized; 