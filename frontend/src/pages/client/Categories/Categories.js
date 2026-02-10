import React from 'react';
import { Link } from 'react-router-dom';
import { Tag, ArrowRight } from 'lucide-react';
import Loading from '../../../components/Loading/Loading';
import { useApp } from '../../../context/AppContext';
import './Categories.css';

const Categories = () => {
  const { categories, loading } = useApp();

  if (loading) {
    return <Loading text="Loading categories..." />;
  }

  return (
    <div className="categories-page">
      <div className="container">
        <div className="page-header">
          <h1>Categories</h1>
          <p>Browse products by category</p>
        </div>

        {categories.length === 0 ? (
          <div className="no-categories">
            <Tag size={64} />
            <h2>No Categories</h2>
            <p>Categories will appear here once they're added.</p>
          </div>
        ) : (
          <div className="categories-list">
            {categories.map((category) => (
              <Link
                key={category.categoryId}
                to={`/products?category=${category.categoryName}`}
                className="category-item"
              >
                <div className="category-icon-large">
                  <Tag size={32} />
                </div>
                <div className="category-content">
                  <h2>{category.categoryName}</h2>
                  <p>{category.description || 'Explore products in this category'}</p>
                </div>
                <ArrowRight size={24} className="category-arrow" />
              </Link>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Categories;
