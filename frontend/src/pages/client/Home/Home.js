import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight, Package, Tag, Users, TrendingUp } from 'lucide-react';
import ProductCard from '../../../components/ProductCard/ProductCard';
import Loading from '../../../components/Loading/Loading';
import ProductService from '../../../services/productService';
import { useApp } from '../../../context/AppContext';
import './Home.css';

const Home = () => {
  const [featuredProducts, setFeaturedProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const { categories } = useApp();

  useEffect(() => {
    fetchFeaturedProducts();
  }, []);

  const fetchFeaturedProducts = async () => {
    try {
      const response = await ProductService.getProducts({ size: 8 });
      setFeaturedProducts(response.content || []);
    } catch (error) {
      console.error('Failed to fetch products:', error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="home">
      {/* Hero Section */}
      <section className="hero">
        <div className="container">
          <div className="hero-content">
            <h1>Welcome to SmartCommerce</h1>
            <p>Discover amazing products at unbeatable prices. Shop the latest trends and enjoy fast delivery.</p>
            <div className="hero-actions">
              <Link to="/products" className="btn btn-primary btn-lg">
                Shop Now <ArrowRight size={20} />
              </Link>
              <Link to="/categories" className="btn btn-outline btn-lg">
                Browse Categories
              </Link>
            </div>
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="stats">
        <div className="container">
          <div className="stats-grid">
            <div className="stat-card">
              <Package size={32} />
              <div>
                <h3>{featuredProducts.length}+</h3>
                <p>Products</p>
              </div>
            </div>
            <div className="stat-card">
              <Tag size={32} />
              <div>
                <h3>{categories.length}</h3>
                <p>Categories</p>
              </div>
            </div>
            <div className="stat-card">
              <Users size={32} />
              <div>
                <h3>1000+</h3>
                <p>Happy Customers</p>
              </div>
            </div>
            <div className="stat-card">
              <TrendingUp size={32} />
              <div>
                <h3>24/7</h3>
                <p>Support</p>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Categories Section */}
      <section className="categories-section">
        <div className="container">
          <div className="section-header">
            <h2>Shop by Category</h2>
            <Link to="/categories" className="view-all">
              View All <ArrowRight size={18} />
            </Link>
          </div>
          <div className="categories-grid">
            {categories.slice(0, 6).map((category) => (
              <Link
                key={category.categoryId}
                to={`/products?category=${category.categoryName}`}
                className="category-card"
              >
                <div className="category-icon">
                  <Tag size={24} />
                </div>
                <h3>{category.categoryName}</h3>
                <p>{category.description}</p>
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* Featured Products Section */}
      <section className="featured-section">
        <div className="container">
          <div className="section-header">
            <h2>Featured Products</h2>
            <Link to="/products" className="view-all">
              View All <ArrowRight size={18} />
            </Link>
          </div>
          {loading ? (
            <Loading text="Loading products..." />
          ) : (
            <div className="products-grid">
              {featuredProducts.map((product) => (
                <ProductCard key={product.productId} product={product} />
              ))}
            </div>
          )}
        </div>
      </section>
    </div>
  );
};

export default Home;
