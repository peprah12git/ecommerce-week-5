import React from 'react';
import { Link } from 'react-router-dom';
import { ShoppingBag, Mail, Phone, MapPin } from 'lucide-react';
import './Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="container footer-container">
        <div className="footer-section">
          <div className="footer-brand">
            <ShoppingBag size={24} />
            <span>SmartCommerce</span>
          </div>
          <p>Your trusted e-commerce platform for quality products.</p>
        </div>

        <div className="footer-section">
          <h4>Quick Links</h4>
          <Link to="/home">Home</Link>
          <Link to="/products">Products</Link>
          <Link to="/categories">Categories</Link>
          <Link to="/orders">My Orders</Link>
        </div>

        <div className="footer-section">
          <h4>Contact</h4>
          <div className="contact-item">
            <Mail size={16} />
            <span>support@smartcommerce.com</span>
          </div>
          <div className="contact-item">
            <Phone size={16} />
            <span>+1 (555) 123-4567</span>
          </div>
          <div className="contact-item">
            <MapPin size={16} />
            <span>123 Commerce St, City</span>
          </div>
        </div>
      </div>
      <div className="footer-bottom">
        <p>&copy; 2024 SmartCommerce. All rights reserved.</p>
      </div>
    </footer>
  );
};

export default Footer;
