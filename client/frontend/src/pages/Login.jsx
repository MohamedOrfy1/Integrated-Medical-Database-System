import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import '../styles/Login.css';
import AuthService from '../services/AuthService';

const Login = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    role: 'DOC' // Default role
  });
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      setIsLoading(true);
      const response = await AuthService.login(formData.username, formData.password, formData.role);
      if (response.accessToken) {
        // Redirect based on role
        if (formData.role === 'DOC') {
          navigate('/doctor-dashboard');
        } else if (formData.role === 'EMP') {
          navigate('/receptionist');
        } else {
          navigate('/home');
        }
      }
    } catch (err) {
      console.error('Login error:', err);
      setError('Invalid username or password');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="login-container">
      <div className="login-card">
        <h1>Login</h1>
        {error && <div className="error-message">{error}</div>}
        <form onSubmit={handleSubmit}>
          <div className="form-group">
            <input
              type="text"
              id="username"
              name="username"
              value={formData.username}
              onChange={handleChange}
              required
              placeholder=" "
              autoComplete="username"
            />
            <label htmlFor="username" style={{backgroundColor: "transparent" , fontSize: "19px"}}>Username</label>
          </div>

          <div className="form-group">
            <input
              type={showPassword ? "text" : "password"}
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              placeholder=" "
              autoComplete="current-password"
            />
            <label htmlFor="password" style={{backgroundColor: "transparent" , fontSize: "19px"}}>Password</label>
            <button
              type="button"
              className="password-toggle"
              onClick={() => setShowPassword(!showPassword)}
            >
              {showPassword ? "👁️" : "👁️‍🗨️"}
            </button>
          </div>

          <div className="role-selection">
            <label>
              <input
                type="radio"
                name="role"
                value="DOC"
                checked={formData.role === 'DOC'}
                onChange={handleChange}
              />
              Doctor
            </label>
            <label>
              <input
                type="radio"
                name="role"
                value="EMP"
                checked={formData.role === 'EMP'}
                onChange={handleChange}
              />
              Employee
            </label>
          </div>

          {/* <div className="forgot-password">
            <Link to="/forgot-password">Forgot Password?</Link>
          </div> */}

          <button 
            type="submit" 
            className={`login-button ${isLoading ? 'loading' : ''}`}
            disabled={isLoading}
          >
            {isLoading ? 'Logging in...' : 'Login'}
          </button>
        </form>

        {/* <div className="signup-link">
          Don't have an account? <Link to="/signup">Sign Up</Link>
        </div> */}
      </div>
    </div>
  );
};

export default Login;
