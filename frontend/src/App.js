import React from 'react';
import { BrowserRouter as Router, Routes, Route, Outlet } from 'react-router-dom';
import { AppProvider } from './context/AppContext';

// Components
import Header from './components/Header/Header';

// Client Pages
import Home from './pages/client/Home/Home';
import Products from './pages/client/Products/Products';
import ProductDetail from './pages/client/ProductDetail/ProductDetail';
import Categories from './pages/client/Categories/Categories';
import Register from './pages/client/Register/Register';
import Cart from './pages/client/Cart/Cart';

// Admin Pages
import AdminLayout from './pages/admin/AdminLayout/AdminLayout';
import Dashboard from './pages/admin/Dashboard/Dashboard';
import ProductsAdmin from './pages/admin/Products/ProductsAdmin';
import ProductForm from './pages/admin/Products/ProductForm';
import CategoriesAdmin from './pages/admin/Categories/CategoriesAdmin';
import CategoryForm from './pages/admin/Categories/CategoryForm';
import OrdersAdmin from './pages/admin/Orders/OrdersAdmin';
import InventoryAdmin from './pages/admin/Inventory/InventoryAdmin';

// Client Layout wrapper
const ClientLayout = () => (
  <>
    <Header />
    <Outlet />
  </>
);

function App() {
  return (
    <AppProvider>
      <Router>
        <Routes>
          {/* Client Routes */}
          <Route element={<ClientLayout />}>
            <Route path="/" element={<Home />} />
            <Route path="/products" element={<Products />} />
            <Route path="/products/:id" element={<ProductDetail />} />
            <Route path="/categories" element={<Categories />} />
            <Route path="/register" element={<Register />} />
            <Route path="/cart" element={<Cart />} />
          </Route>

          {/* Admin Routes */}
          <Route path="/admin" element={<AdminLayout />}>
            <Route index element={<Dashboard />} />
            <Route path="products" element={<ProductsAdmin />} />
            <Route path="products/new" element={<ProductForm />} />
            <Route path="products/:id" element={<ProductForm />} />
            <Route path="categories" element={<CategoriesAdmin />} />
            <Route path="categories/new" element={<CategoryForm />} />
            <Route path="categories/:id" element={<CategoryForm />} />
            <Route path="orders" element={<OrdersAdmin />} />
            <Route path="inventory" element={<InventoryAdmin />} />
          </Route>
        </Routes>
      </Router>
    </AppProvider>
  );
}

export default App;
