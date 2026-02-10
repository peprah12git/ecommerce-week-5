package com.smartcommerce.dao.interfaces;

import com.smartcommerce.model.Order;
import java.util.List;

public interface OrderDaoInterface {
    /**
     * Adds a new order to the database
     * @param order The order object to be added
     * @return true if the order was successfully added, false otherwise
     */
    boolean addOrder(Order order);

    /**
     * Retrieves all orders from the database
     * @return List of all orders, ordered by date (most recent first)
     */
    List<Order> getAllOrders();

    /**
     * Retrieves a specific order by its ID
     * @param id The ID of the order
     * @return The order object if found, null otherwise
     */
    Order getOrderById(int id);

    /**
     * Retrieves all orders for a specific user
     * @param userId The ID of the user
     * @return List of orders for the specified user, ordered by date (most recent first)
     */
    List<Order> getOrdersByUserId(int userId);

    /**
     * Updates the status of an existing order
     * @param orderId The ID of the order to update
     * @param status The new status value
     * @return true if the order status was successfully updated, false otherwise
     */
    boolean updateOrderStatus(int orderId, String status);

    /**
     * Deletes an order from the database
     * @param id The ID of the order to be deleted
     * @return true if the order was successfully deleted, false otherwise
     */
    boolean deleteOrder(int id);
}