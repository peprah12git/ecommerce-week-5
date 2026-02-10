import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Save } from 'lucide-react';
import CategoryService from '../../../services/categoryService';
import { useApp } from '../../../context/AppContext';
import Loading from '../../../components/Loading/Loading';
import './CategoryForm.css';

const CategoryForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const isEditing = Boolean(id);
  const { showNotification, refreshCategories } = useApp();

  const [loading, setLoading] = useState(isEditing);
  const [saving, setSaving] = useState(false);
  const [formData, setFormData] = useState({
    categoryName: '',
    description: '',
  });
  const [errors, setErrors] = useState({});

  useEffect(() => {
    if (isEditing) {
      fetchCategory();
    }
  }, [id]);

  const fetchCategory = async () => {
    try {
      const category = await CategoryService.getCategoryById(id);
      setFormData({
        categoryName: category.categoryName || '',
        description: category.description || '',
      });
    } catch (error) {
      showNotification('Failed to load category', 'error');
      navigate('/admin/categories');
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

    if (!formData.categoryName.trim()) {
      newErrors.categoryName = 'Category name is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!validate()) return;

    setSaving(true);
    try {
      const categoryData = {
        categoryName: formData.categoryName,
        description: formData.description,
      };

      if (isEditing) {
        await CategoryService.updateCategory(id, categoryData);
        showNotification('Category updated successfully', 'success');
      } else {
        await CategoryService.createCategory(categoryData);
        showNotification('Category created successfully', 'success');
      }

      refreshCategories();
      navigate('/admin/categories');
    } catch (error) {
      const message = error.response?.data?.message || 'Failed to save category';
      showNotification(message, 'error');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return <Loading text="Loading category..." />;
  }

  return (
    <div className="category-form-page">
      <button className="back-btn" onClick={() => navigate('/admin/categories')}>
        <ArrowLeft size={18} />
        Back to Categories
      </button>

      <div className="card">
        <div className="card-header">
          <h2>{isEditing ? 'Edit Category' : 'Add New Category'}</h2>
        </div>
        <div className="card-body">
          <form onSubmit={handleSubmit}>
            <div className="form-group">
              <label className="form-label">Category Name *</label>
              <input
                type="text"
                name="categoryName"
                className={`form-input ${errors.categoryName ? 'error' : ''}`}
                value={formData.categoryName}
                onChange={handleChange}
                placeholder="Enter category name"
              />
              {errors.categoryName && (
                <p className="error-message">{errors.categoryName}</p>
              )}
            </div>

            <div className="form-group">
              <label className="form-label">Description</label>
              <textarea
                name="description"
                className="form-input"
                value={formData.description}
                onChange={handleChange}
                placeholder="Enter category description"
                rows={4}
              />
            </div>

            <div className="form-actions">
              <button
                type="button"
                className="btn btn-outline"
                onClick={() => navigate('/admin/categories')}
              >
                Cancel
              </button>
              <button type="submit" className="btn btn-primary" disabled={saving}>
                <Save size={18} />
                {saving ? 'Saving...' : isEditing ? 'Update Category' : 'Create Category'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default CategoryForm;
