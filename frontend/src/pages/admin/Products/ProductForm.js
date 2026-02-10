import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Save } from 'lucide-react';
import ProductService from '../../../services/productService';
import { useApp } from '../../../context/AppContext';
import Loading from '../../../components/Loading/Loading';
import './ProductForm.css';

const ProductForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = Boolean(id);
  const { categories, showNotification } = useApp();

  const [loading, setLoading] = useState(isEditing);
  const [saving, setSaving] = useState(false);
  const [formData, setFormData] = useState({
    productName: '',
    description: '',
    price: '',
    categoryId: '',
    quantityAvailable: '',
  });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (isEditing) {
      fetchProduct();
    }
  }, [id]);

  const fetchProduct = async () => {
    try {
      const product = await ProductService.getProductById(id);
      setFormData({
        productName: product.productName || '',
        description: product.description || '',
        price: product.price?.toString() || '',
        categoryId: product.categoryId?.toString() || '',
        quantityAvailable: product.quantityAvailable?.toString() || '',
      });
    } catch (error) {
      showNotification('Failed to load product', 'error');
      navigate('/admin/products');
    } finally {
      setLoading(false);
    }
  };

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: '' }));
    }
  };

  const validate = () => {
    const newErrors = {};

    if (!formData.productName.trim()) {
      newErrors.productName = 'Product name is required';
    }

    if (!formData.price || parseFloat(formData.price) <= 0) {
      newErrors.price = 'Valid price is required';
    }

    if (!formData.categoryId) {
      newErrors.categoryId = 'Category is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validate()) return;

    setSaving(true);
    try {
      const productData = {
        productName: formData.productName,
        description: formData.description,
        price: parseFloat(formData.price),
        categoryId: parseInt(formData.categoryId),
        quantityAvailable: formData.quantityAvailable
          ? parseInt(formData.quantityAvailable)
          : 0,
      };

      if (isEditing) {
        await ProductService.updateProduct(id, productData);
        showNotification('Product updated successfully', 'success');
      } else {
        await ProductService.createProduct(productData);
        showNotification('Product created successfully', 'success');
      }

      navigate('/admin/products');
    } catch (error) {
      const message = error.response?.data?.message || 'Failed to save product';
      showNotification(message, 'error');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <Loading text="Loading product..." />;
  }

  return (
    <div className="product-form-page">
      <button className="back-btn" onClick={() => navigate('/admin/products')}>
        <ArrowLeft size={18} />
        Back to Products
      </button>

      <div className="card">
        <div className="card-header">
          <h2>{isEditing ? 'Edit Product' : 'Add New Product'}</h2>
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            <div className="form-grid">
              <div className="form-group">
                <label className="form-label">Product Name *</label>
                <input
                  type="text"
                  name="productName"
                  className={`form-input ${errors.productName ? 'error' : ''}`}
                  value={formData.productName}
                  onChange={handleChange}
                  placeholder="Enter product name"
                />
                {errors.productName && (
                  <p className="error-message">{errors.productName}</p>
                )}
              </div>

              <div className="form-group">
                <label className="form-label">Category *</label>
                <select
                  name="categoryId"
                  className={`form-input ${errors.categoryId ? 'error' : ''}`}
                  value={formData.categoryId}
                  onChange={handleChange}
                >
                  <option value="">Select a category</option>
                  {categories.map((cat) => (
                    <option key={cat.categoryId} value={cat.categoryId}>
                      {cat.categoryName}
                    </option>
                  ))}
                </select>
                {errors.categoryId && (
                  <p className="error-message">{errors.categoryId}</p>
                )}
              </div>

              <div className="form-group">
                <label className="form-label">Price *</label>
                <input
                  type="number"
                  name="price"
                  className={`form-input ${errors.price ? 'error' : ''}`}
                  value={formData.price}
                  onChange={handleChange}
                  placeholder="0.00"
                  step="0.01"
                  min="0"
                />
                {errors.price && <p className="error-message">{errors.price}</p>}
              </div>

              <div className="form-group">
                <label className="form-label">Quantity Available</label>
                <input
                  type="number"
                  name="quantityAvailable"
                  className="form-input"
                  value={formData.quantityAvailable}
                  onChange={handleChange}
                  placeholder="0"
                  min="0"
                />
              </div>
            </div>

            <div className="form-group full-width">
              <label className="form-label">Description</label>
              <textarea
                name="description"
                className="form-input"
                value={formData.description}
                onChange={handleChange}
                placeholder="Enter product description"
                rows={4}
              />
            </div>

            <div className="form-actions">
              <button
                type="button"
                className="btn btn-outline"
                onClick={() => navigate('/admin/products')}
              >
                Cancel
              </button>
              <button type="submit" className="btn btn-primary" disabled={saving}>
                <Save size={18} />
                {saving ? 'Saving...' : isEditing ? 'Update Product' : 'Create Product'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ProductForm;
