import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Plus, Edit, Trash2, Search, Tag } from 'lucide-react';
import CategoryService from '../../../services/categoryService';
import Modal from '../../../components/Modal/Modal';
import Loading from '../../../components/Loading/Loading';
import { useApp } from '../../../context/AppContext';
import './CategoriesAdmin.css';

const CategoriesAdmin = () => {
  const navigate = useNavigate();
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deleteModal, setDeleteModal] = useState({ open: false, category: null });
  const [deleting, setDeleting] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const { showNotification, refreshCategories } = useApp();

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const data = await CategoryService.getAllCategories();
      setCategories(data);
    } catch (error) {
      console.error('Failed to fetch categories:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteModal.category) return;

    setDeleting(true);
    try {
      await CategoryService.deleteCategory(deleteModal.category.categoryId);
      showNotification('Category deleted successfully', 'success');
      setDeleteModal({ open: false, category: null });
      fetchCategories();
      refreshCategories();
    } catch (error) {
      showNotification('Failed to delete category', 'error');
    } finally {
      setDeleting(false);
    }
  };

  const filteredCategories = categories.filter((cat) =>
    cat.categoryName.toLowerCase().includes(searchTerm.toLowerCase())
  );

  return (
    <div className="categories-admin">
      <div className="page-header">
        <h1>Categories</h1>
        <Link to="/admin/categories/new" className="btn btn-primary">
          <Plus size={18} />
          Add Category
        </Link>
      </div>

      <div className="card">
        <div className="card-header">
          <div className="search-box">
            <Search size={18} />
            <input
              type="text"
              placeholder="Search categories..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>
        </div>
        <div className="card-body">
          {loading ? (
            <Loading text="Loading categories..." />
          ) : filteredCategories.length === 0 ? (
            <div className="empty-state">
              <Tag size={48} />
              <h3>No categories found</h3>
              <p>Get started by adding your first category</p>
              <Link to="/admin/categories/new" className="btn btn-primary">
                Add Category
              </Link>
            </div>
          ) : (
            <table className="table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Category Name</th>
                  <th>Description</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredCategories.map((category) => (
                  <tr key={category.categoryId}>
                    <td>#{category.categoryId}</td>
                    <td>
                      <div className="category-name-cell">
                        <Tag size={18} />
                        <span>{category.categoryName}</span>
                      </div>
                    </td>
                    <td>{category.description || '-'}</td>
                    <td>
                      <div className="action-buttons">
                        <button
                          className="btn btn-outline btn-sm"
                          onClick={() => navigate(`/admin/categories/${category.categoryId}`)}
                        >
                          <Edit size={16} />
                        </button>
                        <button
                          className="btn btn-danger btn-sm"
                          onClick={() => setDeleteModal({ open: true, category })}
                        >
                          <Trash2 size={16} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </div>
      </div>

      <Modal
        isOpen={deleteModal.open}
        onClose={() => setDeleteModal({ open: false, category: null })}
        title="Delete Category"
        footer={
          <>
            <button
              className="btn btn-outline"
              onClick={() => setDeleteModal({ open: false, category: null })}
            >
              Cancel
            </button>
            <button className="btn btn-danger" onClick={handleDelete} disabled={deleting}>
              {deleting ? 'Deleting...' : 'Delete'}
            </button>
          </>
        }
      >
        <p>
          Are you sure you want to delete <strong>{deleteModal.category?.categoryName}</strong>?
          This action cannot be undone.
        </p>
      </Modal>
    </div>
  );
};

export default CategoriesAdmin;
