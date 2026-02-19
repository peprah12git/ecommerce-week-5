import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Mail, Lock, LogIn, Eye, EyeOff, Shield, User } from 'lucide-react';
import UserService from '../../services/userService';
import { useApp } from '../../context/AppContext';
import './UnifiedLogin.css';

const UnifiedLogin = () => {
  const navigate = useNavigate();
  const { showNotification, setUser } = useApp();
  const [loginType, setLoginType] = useState('client');
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [showPassword, setShowPassword] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }));
    }
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.email.trim()) {
      newErrors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(formData.email)) {
      newErrors.email = 'Invalid email format';
    }
    if (!formData.password) {
      newErrors.password = 'Password is required';
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    try {
      if (loginType === 'admin') {
        const response = await fetch('http://localhost:8080/api/admin/login', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ email: formData.email, password: formData.password })
        });

        if (!response.ok) {
          const error = await response.json();
          throw new Error(error.message || 'Invalid admin credentials');
        }

        const data = await response.json();
        
        if (data.user.role !== 'ADMIN') {
          throw new Error('Access denied. Admin privileges required.');
        }
        
        localStorage.setItem('adminToken', data.token);
        localStorage.setItem('adminUser', JSON.stringify(data.user));
        setUser(data.user);
        showNotification('Admin login successful', 'success');
        navigate('/admin');
      } else {
        const response = await UserService.login(formData.email, formData.password);
        localStorage.setItem('token', response.token);
        localStorage.setItem('user', JSON.stringify(response.user));
        setUser(response.user);
        showNotification(`Welcome back, ${response.user.name}!`, 'success');
        navigate('/home');
      }
    } catch (error) {
      const message = error.response?.data?.message || error.message || 'Login failed';
      setErrors({ submit: message });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="unified-login-page">
      <div className="container">
        <div className="unified-login-card">
          <div className="login-header">
            <LogIn size={48} />
            <h1>Welcome to SmartCommerce</h1>
            <p>Sign in to continue</p>
          </div>

          <div className="login-type-selector">
            <button
              type="button"
              className={`type-btn ${loginType === 'client' ? 'active' : ''}`}
              onClick={() => setLoginType('client')}
            >
              <User size={20} />
              Client Login
            </button>
            <button
              type="button"
              className={`type-btn ${loginType === 'admin' ? 'active' : ''}`}
              onClick={() => setLoginType('admin')}
            >
              <Shield size={20} />
              Admin Login
            </button>
          </div>

          <form onSubmit={handleSubmit} className="login-form">
            <div className="form-group">
              <label htmlFor="email">
                <Mail size={16} />
                Email Address
              </label>
              <input
                type="email"
                id="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                placeholder={loginType === 'admin' ? 'admin@test.com' : 'Enter your email'}
                className={errors.email ? 'error' : ''}
                disabled={loading}
              />
              {errors.email && <span className="error-message">{errors.email}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="password">
                <Lock size={16} />
                Password
              </label>
              <div className="password-input">
                <input
                  type={showPassword ? 'text' : 'password'}
                  id="password"
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  placeholder="Enter your password"
                  className={errors.password ? 'error' : ''}
                  disabled={loading}
                />
                <button
                  type="button"
                  className="password-toggle"
                  onClick={() => setShowPassword(!showPassword)}
                  disabled={loading}
                >
                  {showPassword ? <EyeOff size={16} /> : <Eye size={16} />}
                </button>
              </div>
              {errors.password && <span className="error-message">{errors.password}</span>}
            </div>

            {errors.submit && (
              <div className="error-message submit-error">
                {errors.submit}
              </div>
            )}

            <button 
              type="submit" 
              className="btn btn-primary btn-block"
              disabled={loading}
            >
              {loading ? 'Signing In...' : 'Sign In'}
            </button>
          </form>

          {loginType === 'client' && (
            <div className="login-footer">
              <p className="register-link">
                Don't have an account? <Link to="/register">Sign up</Link>
              </p>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default UnifiedLogin;
