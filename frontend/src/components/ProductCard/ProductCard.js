import React from 'react';
import { Link } from 'react-router-dom';
import './ProductCard.css';

const ProductCard = ({ product }) => {
  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  return (
    <Link to={`/products/${product.productId}`} className="product-card">
      <div className="product-image">
        <img
          src={`https://picsum.photos/seed/${product.productId}/300/300`}
          alt={product.productName}
        />
        {product.quantityAvailable === 0 && (
          <span className="out-of-stock-badge">Out of Stock</span>
        )}
      </div>
      <div className="product-info">
        <span className="product-category">{product.categoryName}</span>
        <h3 className="product-name">{product.productName}</h3>
        <p className="product-description">{product.description}</p>
        <div className="product-footer">
          <span className="product-price">{formatPrice(product.price)}</span>
          <span className={`product-stock ${product.quantityAvailable > 0 ? 'in-stock' : 'no-stock'}`}>
            {product.quantityAvailable > 0 ? `${product.quantityAvailable} in stock` : 'Out of stock'}
          </span>
        </div>
      </div>
    </Link>
  );
};

export default ProductCard;
