package com.smartcommerce.dao.interfaces;

import com.smartcommerce.model.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public interface OrderItemDaoInterface {
    /**
     * Adds a new order item to the database
     * @param item The order item object to be added
     * @return true if the order item was successfully added, false otherwise
     */
    boolean addOrderItem(OrderItem item);

    /**
     * Adds a new order item to the database with individual parameters
     * @param orderId The ID of the order
     * @param productId The ID of the product
     * @param quantity The quantity of the product
     * @param unitPrice The unit price of the product
     * @return true if the order item was successfully added, false otherwise
     */
    boolean addOrderItem(int orderId, int productId, int quantity, BigDecimal unitPrice);

    /**
     * Retrieves all order items for a specific order
     * @param orderId The ID of the order
     * @return List of order items for the specified order
     */
    List<OrderItem> getOrderItemsByOrderId(int orderId);

    /**
     * Deletes an order item from the database
     * @param id The ID of the order item to be deleted
     * @return true if the order item was successfully deleted, false otherwise
     */
    boolean deleteOrderItem(int id);
}