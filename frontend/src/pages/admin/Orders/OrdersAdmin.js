import React, { useState, useEffect } from 'react';
import { ClipboardList, Eye, XCircle, CheckCircle, Truck, Package } from 'lucide-react';
import OrderService from '../../../services/orderService';
import Modal from '../../../components/Modal/Modal';
import Loading from '../../../components/Loading/Loading';
import { useApp } from '../../../context/AppContext';
import './OrdersAdmin.css';

const OrdersAdmin = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [selectedOrder, setSelectedOrder] = useState(null);
  const [detailsModal, setDetailsModal] = useState(false);
  const [statusModal, setStatusModal] = useState({ open: false, order: null });
  const [updating, setUpdating] = useState(false);
  const { showNotification } = useApp();

  const statusOptions = [
    { value: 'pending', label: 'Pending', color: '#f59e0b' },
    { value: 'confirmed', label: 'Confirmed', color: '#3b82f6' },
    { value: 'processing', label: 'Processing', color: '#8b5cf6' },
    { value: 'shipped', label: 'Shipped', color: '#10b981' },
    { value: 'delivered', label: 'Delivered', color: '#059669' },
    { value: 'cancelled', label: 'Cancelled', color: '#ef4444' },
  ];

  useEffect(() => {
    fetchOrders();
  }, []);

  const fetchOrders = async () => {
    setLoading(true);
    try {
      const data = await OrderService.getAllOrders();
      setOrders(data);
    } catch (error) {
      console.error('Failed to fetch orders:', error);
      showNotification('Failed to load orders', 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleViewDetails = async (order) => {
    try {
      const fullOrder = await OrderService.getOrderById(order.orderId);
      setSelectedOrder(fullOrder);
      setDetailsModal(true);
    } catch (error) {
      showNotification('Failed to load order details', 'error');
    }
  };

  const handleUpdateStatus = async (newStatus) => {
    if (!statusModal.order) return;

    setUpdating(true);
    try {
      await OrderService.updateOrderStatus(statusModal.order.orderId, newStatus);
      showNotification('Order status updated', 'success');
      setStatusModal({ open: false, order: null });
      fetchOrders();
    } catch (error) {
      showNotification('Failed to update status', 'error');
    } finally {
      setUpdating(false);
    }
  };

  const handleCancelOrder = async (orderId) => {
    setUpdating(true);
    try {
      await OrderService.cancelOrder(orderId);
      showNotification('Order cancelled', 'success');
      fetchOrders();
    } catch (error) {
      showNotification(error.response?.data?.message || 'Failed to cancel order', 'error');
    } finally {
      setUpdating(false);
    }
  };

  const formatPrice = (price) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const getStatusColor = (status) => {
    const option = statusOptions.find(s => s.value === status);
    return option ? option.color : '#666';
  };

  return (
    <div className="orders-admin">
      <div className="page-header">
        <h1>Orders</h1>
        <div className="stats-row">
          <div className="stat-badge">
            <span className="stat-label">Total Orders</span>
            <span className="stat-value">{orders.length}</span>
          </div>
        </div>
      </div>

      <div className="card">
        <div className="card-body">
          {loading ? (
            <Loading text="Loading orders..." />
          ) : orders.length === 0 ? (
            <div className="empty-state">
              <ClipboardList size={48} />
              <h3>No orders yet</h3>
              <p>Orders will appear here when customers make purchases</p>
            </div>
          ) : (
            <div className="table-container">
              <table className="data-table">
                <thead>
                  <tr>
                    <th>Order ID</th>
                    <th>Customer</th>
                    <th>Date</th>
                    <th>Items</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {orders.map((order) => (
                    <tr key={order.orderId}>
                      <td>
                        <span className="order-id">#{order.orderId}</span>
                      </td>
                      <td>{order.userName || `User #${order.userId}`}</td>
                      <td>{formatDate(order.orderDate)}</td>
                      <td>{order.itemCount || order.items?.length || '-'}</td>
                      <td className="price">{formatPrice(order.totalAmount)}</td>
                      <td>
                        <span 
                          className="status-badge"
                          style={{ backgroundColor: getStatusColor(order.status) }}
                        >
                          {order.status}
                        </span>
                      </td>
                      <td>
                        <div className="action-buttons">
                          <button
                            className="btn-icon"
                            onClick={() => handleViewDetails(order)}
                            title="View Details"
                          >
                            <Eye size={16} />
                          </button>
                          <button
                            className="btn-icon"
                            onClick={() => setStatusModal({ open: true, order })}
                            title="Update Status"
                            disabled={order.status === 'cancelled' || order.status === 'delivered'}
                          >
                            <Truck size={16} />
                          </button>
                          {order.status !== 'cancelled' && order.status !== 'delivered' && (
                            <button
                              className="btn-icon danger"
                              onClick={() => handleCancelOrder(order.orderId)}
                              title="Cancel Order"
                              disabled={updating}
                            >
                              <XCircle size={16} />
                            </button>
                          )}
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>

      {/* Order Details Modal */}
      <Modal
        isOpen={detailsModal}
        onClose={() => setDetailsModal(false)}
        title={`Order #${selectedOrder?.orderId}`}
      >
        {selectedOrder && (
          <div className="order-details">
            <div className="detail-row">
              <span className="label">Customer:</span>
              <span>{selectedOrder.userName || `User #${selectedOrder.userId}`}</span>
            </div>
            <div className="detail-row">
              <span className="label">Date:</span>
              <span>{formatDate(selectedOrder.orderDate)}</span>
            </div>
            <div className="detail-row">
              <span className="label">Status:</span>
              <span 
                className="status-badge"
                style={{ backgroundColor: getStatusColor(selectedOrder.status) }}
              >
                {selectedOrder.status}
              </span>
            </div>
            
            <h4>Items</h4>
            <div className="order-items">
              {selectedOrder.items?.map((item) => (
                <div key={item.orderItemId} className="order-item">
                  <div className="item-info">
                    <Package size={20} />
                    <span>{item.productName || `Product #${item.productId}`}</span>
                  </div>
                  <div className="item-qty">x{item.quantity}</div>
                  <div className="item-price">{formatPrice(item.subtotal)}</div>
                </div>
              ))}
            </div>

            <div className="order-total">
              <span>Total:</span>
              <span>{formatPrice(selectedOrder.totalAmount)}</span>
            </div>
          </div>
        )}
      </Modal>

      {/* Update Status Modal */}
      <Modal
        isOpen={statusModal.open}
        onClose={() => setStatusModal({ open: false, order: null })}
        title="Update Order Status"
      >
        <div className="status-options">
          {statusOptions
            .filter(s => s.value !== 'cancelled')
            .map((status) => (
              <button
                key={status.value}
                className={`status-option ${statusModal.order?.status === status.value ? 'current' : ''}`}
                style={{ borderColor: status.color }}
                onClick={() => handleUpdateStatus(status.value)}
                disabled={updating || statusModal.order?.status === status.value}
              >
                <CheckCircle size={18} style={{ color: status.color }} />
                <span>{status.label}</span>
              </button>
            ))}
        </div>
      </Modal>
    </div>
  );
};

export default OrdersAdmin;
