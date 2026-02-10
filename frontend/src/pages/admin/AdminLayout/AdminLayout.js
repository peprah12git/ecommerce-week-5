import React from 'react';
import { Link, Outlet, useLocation } from 'react-router-dom';
import {
  LayoutDashboard,
  Package,
  Tag,
  Users,
  Settings,
  ArrowLeft,
  Menu,
  ClipboardList,
  Warehouse,
} from 'lucide-react';
import './AdminLayout.css';

const AdminLayout = () => {
  const location = useLocation();

  const navItems = [
    { path: '/admin', icon: LayoutDashboard, label: 'Dashboard', exact: true },
    { path: '/admin/products', icon: Package, label: 'Products' },
    { path: '/admin/categories', icon: Tag, label: 'Categories' },
    { path: '/admin/orders', icon: ClipboardList, label: 'Orders' },
    { path: '/admin/inventory', icon: Warehouse, label: 'Inventory' },
  ];

  const isActive = (path, exact = false) => {
    if (exact) {
      return location.pathname === path;
    }
    return location.pathname.startsWith(path);
  };

  return (
    <div className="admin-layout">
      <aside className="admin-sidebar">
        <div className="sidebar-header">
          <Link to="/admin" className="admin-logo">
            <Package size={24} />
            <span>Admin Panel</span>
          </Link>
        </div>

        <nav className="sidebar-nav">
          {navItems.map((item) => (
            <Link
              key={item.path}
              to={item.path}
              className={`nav-item ${isActive(item.path, item.exact) ? 'active' : ''}`}
            >
              <item.icon size={20} />
              <span>{item.label}</span>
            </Link>
          ))}
        </nav>

        <div className="sidebar-footer">
          <Link to="/" className="back-to-store">
            <ArrowLeft size={18} />
            <span>Back to Store</span>
          </Link>
        </div>
      </aside>

      <main className="admin-main">
        <header className="admin-header">
          <button className="mobile-menu-btn">
            <Menu size={24} />
          </button>
          <div className="admin-header-title">
            {navItems.find((item) => isActive(item.path, item.exact))?.label || 'Admin'}
          </div>
        </header>

        <div className="admin-content">
          <Outlet />
        </div>
      </main>
    </div>
  );
};

export default AdminLayout;
