package com.smartcommerce.service.serviceInterface;

import java.math.BigDecimal;
import java.util.List;

import com.smartcommerce.exception.BusinessException;
import com.smartcommerce.exception.ResourceNotFoundException;
import com.smartcommerce.model.OrderItem;

/**
 * Service interface for OrderItem entity
 * Defines business operations related to order items
 */
public interface OrderItemService {

    /**
     * Adds an order item to an order
     *
     * @param orderItem OrderItem object to add
     * @return Created order item
     * @throws ResourceNotFoundException if order or product not found
     * @throws BusinessException if order item creation fails
     */
    OrderItem addOrderItem(OrderItem orderItem);

    /**
     * Adds an order item using individual parameters
     *
     * @param orderId Order ID
     * @param productId Product ID
     * @param quantity Quantity
     * @param unitPrice Unit price
     * @return Created order item
     * @throws ResourceNotFoundException if order or product not found
     * @throws BusinessException if order item creation fails
     */
    OrderItem addOrderItem(int orderId, int productId, int quantity, BigDecimal unitPrice);

    /**
     * Retrieves all order items for a specific order
     *
     * @param orderId Order ID
     * @return List of order items
     * @throws ResourceNotFoundException if order not found
     */
    List<OrderItem> getOrderItemsByOrderId(int orderId);

    /**
     * Retrieves a specific order item by ID
     *
     * @param orderItemId Order item ID
     * @return OrderItem object
     * @throws ResourceNotFoundException if order item not found
     */
    OrderItem getOrderItemById(int orderItemId);

    /**
     * Updates the quantity of an order item
     *
     * @param orderItemId Order item ID
     * @param quantity New quantity
     * @return Updated order item
     * @throws ResourceNotFoundException if order item not found
     * @throws BusinessException if update fails
     */
    OrderItem updateOrderItemQuantity(int orderItemId, int quantity);

    /**
     * Deletes an order item
     *
     * @param orderItemId Order item ID
     * @throws ResourceNotFoundException if order item not found
     * @throws BusinessException if deletion fails
     */
    void deleteOrderItem(int orderItemId);

    /**
     * Deletes all order items for an order
     *
     * @param orderId Order ID
     * @throws ResourceNotFoundException if order not found
     */
    void deleteOrderItemsByOrderId(int orderId);

    /**
     * Calculates the subtotal for an order item
     *
     * @param orderItemId Order item ID
     * @return Subtotal amount
     * @throws ResourceNotFoundException if order item not found
     */
    BigDecimal calculateSubtotal(int orderItemId);
}
