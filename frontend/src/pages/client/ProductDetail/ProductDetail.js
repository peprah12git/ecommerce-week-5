import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import { ArrowLeft, Package, Tag, ShoppingCart, Heart, Share2 } from 'lucide-react';
import Loading from '../../../components/Loading/Loading';
import ProductService from '../../../services/productService';
import CartService from '../../../services/cartService';
import { useApp } from '../../../context/AppContext';
import './ProductDetail.css';

const ProductDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { showNotification, user } = useApp();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [quantity, setQuantity] = useState(1);
  const [addingToCart, setAddingToCart] = useState(false);

  const userId = user?.userId || user?.user_id;

  useEffect(() => {
    fetchProduct();
  }, [id]);

  const fetchProduct = async () => {
    try {
      const data = await ProductService.getProductById(id);
      setProduct(data);
    } catch (error) {
      setError('Product not found');
    } finally {
      setLoading(false);
    }
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  const handleAddToCart = async () => {
    if (!user) {
      showNotification('Please sign in to add items to cart', 'error');
      navigate('/login');
      return;
    }
    setAddingToCart(true);
    try {
      await CartService.addToCart(userId, product.productId, quantity);
      showNotification(`Added ${quantity} ${product.productName} to cart!`, 'success');
    } catch (error) {
      console.error('Failed to add to cart:', error);
      showNotification(error.response?.data?.message || 'Failed to add item to cart', 'error');
    } finally {
      setAddingToCart(false);
    }
  };

  if (loading) {
    return <Loading text="Loading product..." />;
  }

  if (error || !product) {
    return (
      <div className="container">
        <div className="not-found">
          <Package size={64} />
          <h2>Product Not Found</h2>
          <p>The product you're looking for doesn't exist or has been removed.</p>
          <button className="btn btn-primary" onClick={() => navigate('/products')}>
            Browse Products
          </button>
        </div>
      </div>
    );
  }

  return (
    <div className="product-detail-page">
      <div className="container">
        <button className="back-btn" onClick={() => navigate(-1)}>
          <ArrowLeft size={20} />
          Back to Products
        </button>

        <div className="product-detail">
          <div className="product-image-section">
            <img
              src={`https://picsum.photos/seed/${product.productId}/600/600`}
              alt={product.productName}
            />
            {product.quantityAvailable === 0 && (
              <div className="out-of-stock-overlay">Out of Stock</div>
            )}
          </div>

          <div className="product-info-section">
            <Link to={`/products?category=${product.categoryName}`} className="product-category-link">
              <Tag size={16} />
              {product.categoryName}
            </Link>

            <h1>{product.productName}</h1>

            <div className="price-section">
              <span className="current-price">{formatPrice(product.price)}</span>
            </div>

            <p className="product-description-full">{product.description}</p>

            <div className="stock-info">
              <span className={`stock-status ${product.quantityAvailable > 0 ? 'in-stock' : 'out-of-stock'}`}>
                {product.quantityAvailable > 0 ? (
                  <>
                    <span className="stock-dot" />
                    {product.quantityAvailable} in stock
                  </>
                ) : (
                  'Out of Stock'
                )}
              </span>
            </div>

            {product.quantityAvailable > 0 && (
              <div className="quantity-section">
                <label>Quantity:</label>
                <div className="quantity-controls">
                  <button
                    onClick={() => setQuantity(Math.max(1, quantity - 1))}
                    disabled={quantity <= 1}
                  >
                    -
                  </button>
                  <span>{quantity}</span>
                  <button
                    onClick={() => setQuantity(Math.min(product.quantityAvailable, quantity + 1))}
                    disabled={quantity >= product.quantityAvailable}
                  >
                    +
                  </button>
                </div>
              </div>
            )}

            <div className="action-buttons">
              <button
                className="btn btn-primary btn-lg add-to-cart"
                disabled={product.quantityAvailable === 0 || addingToCart}
                onClick={handleAddToCart}
              >
                <ShoppingCart size={20} />
                {addingToCart ? 'Adding...' : 'Add to Cart'}
              </button>
              <button className="btn btn-outline icon-btn">
                <Heart size={20} />
              </button>
              <button className="btn btn-outline icon-btn">
                <Share2 size={20} />
              </button>
            </div>

            <div className="product-meta">
              <div className="meta-item">
                <span className="meta-label">Product ID:</span>
                <span className="meta-value">#{product.productId}</span>
              </div>
              <div className="meta-item">
                <span className="meta-label">Category:</span>
                <span className="meta-value">{product.categoryName}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;
