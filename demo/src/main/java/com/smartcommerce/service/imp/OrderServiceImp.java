package com.smartcommerce.service.imp;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartcommerce.dao.interfaces.OrderDaoInterface;
import com.smartcommerce.dao.interfaces.OrderItemDaoInterface;
import com.smartcommerce.dao.interfaces.ProductDaoInterface;
import com.smartcommerce.dao.interfaces.UserDaoInterface;
import com.smartcommerce.exception.BusinessException;
import com.smartcommerce.exception.ResourceNotFoundException;
import com.smartcommerce.model.Order;
import com.smartcommerce.model.OrderItem;
import com.smartcommerce.model.Product;
import com.smartcommerce.model.User;
import com.smartcommerce.service.serviceInterface.OrderService;

/**
 * Service layer for Order entity
 * Handles business logic, validation, and orchestration of order operations
 */
@Service
@Transactional
public class OrderServiceImp implements OrderService {

    private final OrderDaoInterface orderDao;
    private final OrderItemDaoInterface orderItemDao;
    private final UserDaoInterface userDao;
    private final ProductDaoInterface productDao;

    // Valid order statuses
    private static final List<String> VALID_STATUSES = List.of(
            "pending", "confirmed", "processing", "shipped", "delivered", "cancelled"
    );

    @Autowired
    public OrderServiceImp(OrderDaoInterface orderDao,
                           OrderItemDaoInterface orderItemDao,
                           UserDaoInterface userDao,
                           ProductDaoInterface productDao) {
        this.orderDao = orderDao;
        this.orderItemDao = orderItemDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    @Override
    public Order createOrder(Order order, List<OrderItem> orderItems) {
        // Validate user exists
        User user = userDao.getUserById(order.getUserId());
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", order.getUserId());
        }

        // Validate order items
        if (orderItems == null || orderItems.isEmpty()) {
            throw new BusinessException("Order must contain at least one item");
        }

        // Calculate total and validate products
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItem item : orderItems) {
            Product product = productDao.getProductById(item.getProductId());
            if (product == null) {
                throw new ResourceNotFoundException("Product", "id", item.getProductId());
            }

            // Check stock availability
            if (product.getQuantityAvailable() < item.getQuantity()) {
                throw new BusinessException("Insufficient stock for product: " + product.getProductName() +
                        ". Available: " + product.getQuantityAvailable() + ", Requested: " + item.getQuantity());
            }

            // Set unit price from product if not provided
            if (item.getUnitPrice() == null) {
                item.setUnitPrice(product.getPrice());
            }

            // Calculate subtotal
            BigDecimal subtotal = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(subtotal);
        }

        // Set order total and default status
        order.setTotalAmount(totalAmount);
        if (order.getStatus() == null || order.getStatus().isEmpty()) {
            order.setStatus("pending");
        }

        // Create order
        boolean orderCreated = orderDao.addOrder(order);
        if (!orderCreated) {
            throw new BusinessException("Failed to create order");
        }

        // Add order items
        for (OrderItem item : orderItems) {
            item.setOrderId(order.getOrderId());
            boolean itemCreated = orderItemDao.addOrderItem(item);
            if (!itemCreated) {
                throw new BusinessException("Failed to add order item for product ID: " + item.getProductId());
            }
        }

        // Set order items and return
        order.setOrderItems(orderItems);
        order.setUserName(user.getName());
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders() {
        List<Order> orders = orderDao.getAllOrders();
        // Load order items for each order
        for (Order order : orders) {
            List<OrderItem> items = orderItemDao.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItems(items);
        }
        return orders;
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrderById(int orderId) {
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        // Load order items
        List<OrderItem> items = orderItemDao.getOrderItemsByOrderId(orderId);
        order.setOrderItems(items);

        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrdersByUserId(int userId) {
        // Validate user exists
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        List<Order> orders = orderDao.getOrdersByUserId(userId);
        // Load order items for each order
        for (Order order : orders) {
            List<OrderItem> items = orderItemDao.getOrderItemsByOrderId(order.getOrderId());
            order.setOrderItems(items);
        }
        return orders;
    }

    @Override
    public Order updateOrderStatus(int orderId, String status) {
        // Validate order exists
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        // Validate status
        String normalizedStatus = status.toLowerCase().trim();
        if (!VALID_STATUSES.contains(normalizedStatus)) {
            throw new BusinessException("Invalid order status: " + status +
                    ". Valid statuses are: " + String.join(", ", VALID_STATUSES));
        }

        // Cannot update cancelled orders
        if ("cancelled".equals(order.getStatus())) {
            throw new BusinessException("Cannot update status of a cancelled order");
        }

        // Update status
        boolean updated = orderDao.updateOrderStatus(orderId, normalizedStatus);
        if (!updated) {
            throw new BusinessException("Failed to update order status");
        }

        // Return updated order
        return getOrderById(orderId);
    }

    @Override
    public Order cancelOrder(int orderId) {
        // Validate order exists
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        // Check if order can be cancelled
        String currentStatus = order.getStatus().toLowerCase();
        if ("shipped".equals(currentStatus) || "delivered".equals(currentStatus)) {
            throw new BusinessException("Cannot cancel order that is already " + currentStatus);
        }

        if ("cancelled".equals(currentStatus)) {
            throw new BusinessException("Order is already cancelled");
        }

        // Update status to cancelled
        boolean updated = orderDao.updateOrderStatus(orderId, "cancelled");
        if (!updated) {
            throw new BusinessException("Failed to cancel order");
        }

        // Return updated order
        return getOrderById(orderId);
    }

    @Override
    public void deleteOrder(int orderId) {
        // Validate order exists
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        // Delete order items first (due to foreign key constraint)
        List<OrderItem> items = orderItemDao.getOrderItemsByOrderId(orderId);
        for (OrderItem item : items) {
            orderItemDao.deleteOrderItem(item.getOrderItemId());
        }

        // Delete order
        boolean deleted = orderDao.deleteOrder(orderId);
        if (!deleted) {
            throw new BusinessException("Failed to delete order");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> getOrderItems(int orderId) {
        // Validate order exists
        Order order = orderDao.getOrderById(orderId);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "id", orderId);
        }

        return orderItemDao.getOrderItemsByOrderId(orderId);
    }
}

