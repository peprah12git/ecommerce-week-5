import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { ShoppingBag, Search, User, Menu } from 'lucide-react';
import './Header.css';

const Header = () => {
  const location = useLocation();
  const isAdmin = location.pathname.startsWith('/admin');

  return (
    <header className="header">
      <div className="container header-container">
        <Link to="/" className="logo">
          <ShoppingBag size={28} />
          <span>SmartCommerce</span>
        </Link>

        <nav className="nav-links">
          <Link to="/" className={location.pathname === '/' ? 'active' : ''}>
            Home
          </Link>
          <Link to="/products" className={location.pathname.startsWith('/products') ? 'active' : ''}>
            Products
          </Link>
          <Link to="/categories" className={location.pathname.startsWith('/categories') ? 'active' : ''}>
            Categories
          </Link>
        </nav>

        <div className="header-actions">
          <Link to="/products" className="search-btn">
            <Search size={20} />
          </Link>
          <Link to="/register" className="user-btn">
            <User size={20} />
          </Link>
          {!isAdmin && (
            <Link to="/admin" className="btn btn-primary btn-sm">
              Admin Panel
            </Link>
          )}
        </div>
      </div>
    </header>
  );
};

export default Header;
