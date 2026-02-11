package com.smartcommerce.service.imp;

import com.smartcommerce.dao.interfaces.OrderDaoInterface;
import com.smartcommerce.dao.interfaces.OrderItemDaoInterface;
import com.smartcommerce.dao.interfaces.ProductDaoInterface;
import com.smartcommerce.exception.BusinessException;
import com.smartcommerce.exception.ResourceNotFoundException;
import com.smartcommerce.model.Order;
import com.smartcommerce.model.OrderItem;
import com.smartcommerce.model.Product;
import com.smartcommerce.service.serviceInterface.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service layer for OrderItem entity
 * Handles business logic, validation, and orchestration of order item operations
 */
@Service
@Transactional
public class OrderItemServiceImp implements OrderItemService {

    private final OrderItemDaoInterface orderItemDao;
    private final OrderDaoInterface orderDao;
    private final ProductDaoInterface productDao;

    @Autowired
    public OrderItemServiceImp(OrderItemDaoInterface orderItemDao,
                               OrderDaoInterface orderDao,
                               ProductDaoInterface productDao) {
        this.orderItemDao = orderItemDao;
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    @Override
    public OrderItem addOrderItem(OrderItem orderItem) {
        // Validate order exists
        Order order = orderDao.getOrderById(orderItem.getOrderId());
        if (order == null) {
            throw new ResourceNotFoundException("Order", "id", orderItem.getOrderId());
        }

        // Validate product exists
        Product product = productDao.getProductById(orderItem.getProductId());
        if (product == null) {
            throw new ResourceNotFoundException("Product", "id", orderItem.getProductId());
        }

        // Set unit price from product if not provided
        if (orderItem.getUnitPrice() == null) {
            orderItem.setUnitPrice(product.getPrice());
        }

        // Set product name for convenience
        orderItem.setProductName(product.getProductName());

        // Add order item
        boolean success = orderItemDao.addOrderItem(orderItem);
        if (!success) {
            throw new BusinessException("Failed to add order item");
        }

        return orderItem;
    }

    @Override
    public OrderItem addOrderItem(int orderId, int productId, int quantity, BigDecimal unitPrice) {
        OrderItem orderItem = new OrderItem(orderId, productId, quantity, unitPrice);
        return addOrderItem(orderItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        // Validate order exists
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        return orderItemDao.getOrderItemsByOrderId(orderId);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItem getOrderItemById(int orderItemId) {
        // Get all order items and find by ID (since DAO doesn't have getById)
        // This is a workaround - ideally we'd add getOrderItemById to DAO
        List<OrderItem> allItems = getAllOrderItems();
        return allItems.stream()
                .filter(item -> item.getOrderItemId() == orderItemId)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("OrderItem", "id", orderItemId));
    }

    @Override
    public OrderItem updateOrderItemQuantity(int orderItemId, int quantity) {
        if (quantity < 1) {
            throw new BusinessException("Quantity must be at least 1");
        }

        // Get order item
        OrderItem orderItem = getOrderItemById(orderItemId);

        // Update quantity
        orderItem.setQuantity(quantity);

        // Recalculate subtotal
        orderItem.setSubtotal(orderItem.getUnitPrice().multiply(new BigDecimal(quantity)));

        // Note: This requires updating the DAO to support updates
        // For now, we'll delete and recreate
        orderItemDao.deleteOrderItem(orderItemId);
        boolean success = orderItemDao.addOrderItem(orderItem);
        if (!success) {
            throw new BusinessException("Failed to update order item quantity");
        }

        return orderItem;
    }

    @Override
    public void deleteOrderItem(int orderItemId) {
        // Verify order item exists
        getOrderItemById(orderItemId);

        boolean deleted = orderItemDao.deleteOrderItem(orderItemId);
        if (!deleted) {
            throw new BusinessException("Failed to delete order item");
        }
    }

    @Override
    public void deleteOrderItemsByOrderId(int orderId) {
        // Validate order exists
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        // Get all items for the order and delete them
        List<OrderItem> items = orderItemDao.getOrderItemsByOrderId(orderId);
        for (OrderItem item : items) {
            orderItemDao.deleteOrderItem(item.getOrderItemId());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calculateSubtotal(int orderItemId) {
        OrderItem orderItem = getOrderItemById(orderItemId);
        return orderItem.getUnitPrice().multiply(new BigDecimal(orderItem.getQuantity()));
    }

    /**
     * Helper method to get all order items (for finding by ID)
     */
    private List<OrderItem> getAllOrderItems() {
        // Get all orders and collect their items
        List<Order> allOrders = orderDao.getAllOrders();
        return allOrders.stream()
                .flatMap(order -> orderItemDao.getOrderItemsByOrderId(order.getOrderId()).stream())
                .toList();
    }
}
