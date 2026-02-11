import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ArrowLeft, Package, CheckCircle, Clock, Truck, XCircle } from 'lucide-react';
import OrderService from '../../../services/orderService';
import Loading from '../../../components/Loading/Loading';
import { useApp } from '../../../context/AppContext';
import './OrderDetail.css';

const OrderDetail = () => {
  const { orderId } = useParams();
  const navigate = useNavigate();
  const { showNotification } = useApp();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchOrder();
  }, [orderId]);

  const fetchOrder = async () => {
    try {
      const data = await OrderService.getOrderById(orderId);
      setOrder(data);
    } catch (error) {
      console.error('Failed to fetch order:', error);
      showNotification('Failed to load order details', 'error');
    } finally {
      setLoading(false);
    }
  };

  const getStatusSteps = () => {
    const steps = [
      { key: 'confirmed', label: 'Confirmed', icon: CheckCircle },
      { key: 'processing', label: 'Processing', icon: Clock },
      { key: 'shipped', label: 'Shipped', icon: Truck },
      { key: 'delivered', label: 'Delivered', icon: Package },
    ];

    const statusOrder = ['confirmed', 'processing', 'shipped', 'delivered'];
    const currentIndex = statusOrder.indexOf(order?.status.toLowerCase());

    return steps.map((step, index) => ({
      ...step,
      completed: index <= currentIndex,
      active: index === currentIndex,
    }));
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  if (loading) {
    return (
      <div className="order-detail-page">
        <div className="container">
          <Loading text="Loading order details..." />
        </div>
      </div>
    );
  }

  if (!order) {
    return (
      <div className="order-detail-page">
        <div className="container">
          <div className="not-found">
            <Package size={64} />
            <h2>Order Not Found</h2>
            <button className="btn btn-primary" onClick={() => navigate('/orders')}>
              Back to Orders
            </button>
          </div>
        </div>
      </div>
    );
  }

  const isCancelled = order.status.toLowerCase() === 'cancelled';

  return (
    <div className="order-detail-page">
      <div className="container">
        <button className="back-btn" onClick={() => navigate('/orders')}>
          <ArrowLeft size={20} />
          Back to Orders
        </button>

        <div className="order-detail-card">
          <div className="order-header">
            <div>
              <h1>Order #{order.orderId}</h1>
              <p className="order-date">Placed on {formatDate(order.orderDate)}</p>
            </div>
            <div className={`order-status-badge ${order.status.toLowerCase()}`}>
              {order.status.charAt(0).toUpperCase() + order.status.slice(1)}
            </div>
          </div>

          {!isCancelled && (
            <div className="order-tracking">
              <h2>Order Status</h2>
              <div className="tracking-steps">
                {getStatusSteps().map((step, index) => (
                  <div key={step.key} className={`tracking-step ${step.completed ? 'completed' : ''} ${step.active ? 'active' : ''}`}>
                    <div className="step-icon">
                      <step.icon size={24} />
                    </div>
                    <div className="step-label">{step.label}</div>
                    {index < 3 && <div className="step-line" />}
                  </div>
                ))}
              </div>
            </div>
          )}

          {isCancelled && (
            <div className="cancelled-notice">
              <XCircle size={48} />
              <h3>This order has been cancelled</h3>
            </div>
          )}

          <div className="order-items-section">
            <h2>Order Items</h2>
            <div className="items-list">
              {order.items.map((item) => (
                <div key={item.orderItemId} className="order-item-detail">
                  <div className="item-image">
                    <Package size={48} />
                  </div>
                  <div className="item-info">
                    <h3>{item.productName}</h3>
                    <p>Quantity: {item.quantity}</p>
                    <p className="item-price">{formatPrice(item.unitPrice)} each</p>
                  </div>
                  <div className="item-total">
                    {formatPrice(item.unitPrice * item.quantity)}
                  </div>
                </div>
              ))}
            </div>
          </div>

          <div className="order-summary">
            <h2>Order Summary</h2>
            <div className="summary-row">
              <span>Subtotal</span>
              <span>{formatPrice(order.totalAmount)}</span>
            </div>
            <div className="summary-row">
              <span>Shipping</span>
              <span>Free</span>
            </div>
            <div className="summary-total">
              <span>Total</span>
              <strong>{formatPrice(order.totalAmount)}</strong>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default OrderDetail;
