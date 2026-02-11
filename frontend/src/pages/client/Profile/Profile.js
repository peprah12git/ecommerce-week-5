import React, { useState, useEffect } from 'react';
import { User, Mail, Phone, MapPin, Calendar, Edit2 } from 'lucide-react';
import { useApp } from '../../../context/AppContext';
import './Profile.css';

const Profile = () => {
  const { showNotification } = useApp();
  const [user, setUser] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    phone: '',
    address: ''
  });

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      const parsedUser = JSON.parse(userData);
      setUser(parsedUser);
      setFormData({
        name: parsedUser.name || '',
        email: parsedUser.email || '',
        phone: parsedUser.phone || '',
        address: parsedUser.address || ''
      });
    }
  }, []);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const updatedUser = { ...user, ...formData };
    localStorage.setItem('user', JSON.stringify(updatedUser));
    setUser(updatedUser);
    setIsEditing(false);
    showNotification('Profile updated successfully', 'success');
  };

  if (!user) {
    return <div className="container">Please log in to view your profile.</div>;
  }

  return (
    <div className="container profile-page">
      <div className="profile-header">
        <div className="profile-avatar">
          <User size={48} />
        </div>
        <h1>{user.name}</h1>
        <p className="user-role">{user.role || 'Customer'}</p>
      </div>

      <div className="profile-content">
        {!isEditing ? (
          <div className="profile-info">
            <div className="info-item">
              <Mail size={20} />
              <div>
                <label>Email</label>
                <p>{user.email}</p>
              </div>
            </div>
            <div className="info-item">
              <Phone size={20} />
              <div>
                <label>Phone</label>
                <p>{user.phone || 'Not provided'}</p>
              </div>
            </div>
            <div className="info-item">
              <MapPin size={20} />
              <div>
                <label>Address</label>
                <p>{user.address || 'Not provided'}</p>
              </div>
            </div>
            <div className="info-item">
              <Calendar size={20} />
              <div>
                <label>Member Since</label>
                <p>{user.createdAt ? new Date(user.createdAt).toLocaleDateString() : 'N/A'}</p>
              </div>
            </div>
            <button className="btn btn-primary" onClick={() => setIsEditing(true)}>
              <Edit2 size={16} />
              Edit Profile
            </button>
          </div>
        ) : (
          <form className="profile-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label>Name</label>
              <input
                type="text"
                name="name"
                value={formData.name}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label>Email</label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
              />
            </div>
            <div className="form-group">
              <label>Phone</label>
              <input
                type="tel"
                name="phone"
                value={formData.phone}
                onChange={handleChange}
              />
            </div>
            <div className="form-group">
              <label>Address</label>
              <textarea
                name="address"
                value={formData.address}
                onChange={handleChange}
                rows="3"
              />
            </div>
            <div className="form-actions">
              <button type="submit" className="btn btn-primary">Save Changes</button>
              <button type="button" className="btn btn-outline" onClick={() => setIsEditing(false)}>
                Cancel
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default Profile;
