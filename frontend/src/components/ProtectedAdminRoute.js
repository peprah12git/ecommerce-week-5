import { Navigate } from 'react-router-dom';

const ProtectedAdminRoute = ({ children }) => {
  const adminUser = localStorage.getItem('adminUser');
  
  if (!adminUser) {
    return <Navigate to="/admin/login" replace />;
  }
  
  try {
    const user = JSON.parse(adminUser);
    if (user.role !== 'ADMIN') {
      localStorage.removeItem('adminUser');
      localStorage.removeItem('adminToken');
      return <Navigate to="/admin/login" replace />;
    }
  } catch (error) {
    localStorage.removeItem('adminUser');
    localStorage.removeItem('adminToken');
    return <Navigate to="/admin/login" replace />;
  }
  
  return children;
};

export default ProtectedAdminRoute;
