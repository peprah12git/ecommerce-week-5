package com.smartcommerce.dao.interfaces;

import com.smartcommerce.model.Inventory;
import java.util.List;

/**
 * Interface for Inventory Data Access Object operations
 */
public interface InventoryDaoInterface {

    /**
     * Updates the inventory quantity for a specific product
     * @param productId Product ID to update
     * @param quantity New quantity available
     * @return true if inventory was successfully updated, false otherwise
     */
    boolean updateInventory(int productId, int quantity);

    /**
     * Retrieves inventory information for a specific product
     * @param productId Product ID to search for
     * @return Inventory object if found, null otherwise
     */
    Inventory getInventoryByProductId(int productId);

    /**
     * Retrieves all inventory records from the database
     * @return List of all inventory items ordered by quantity available (ascending)
     */
    List<Inventory> getAllInventory();

    /**
     * Retrieves inventory items that are below the specified stock threshold
     * @param threshold Minimum quantity threshold
     * @return List of inventory items with quantity below threshold, ordered by quantity (ascending)
     */
    List<Inventory> getLowStockItems(int threshold);
}
