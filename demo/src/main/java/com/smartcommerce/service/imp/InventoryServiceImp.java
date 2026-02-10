package com.smartcommerce.service.imp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartcommerce.dao.interfaces.InventoryDaoInterface;
import com.smartcommerce.model.Inventory;
import com.smartcommerce.service.serviceInterface.InventoryServiceInterface;

/**
 * Service layer for Inventory entity
 * Handles business logic, validation, and caching of inventory operations
 */
@Service
public class InventoryServiceImp implements InventoryServiceInterface {
    
    private final InventoryDaoInterface inventoryDAO;
    private List<Inventory> inventoryCache;
    private long lastCacheUpdate;
    private static final long CACHE_VALIDITY = 120000; // 2 minutes (inventory changes frequently)

    @Autowired
    public InventoryServiceImp(InventoryDaoInterface inventoryDAO) {
        this.inventoryDAO = inventoryDAO;
        this.inventoryCache = new ArrayList<>();
        this.lastCacheUpdate = 0;
    }

    @Override
    public boolean updateInventory(int productId, int quantity) {
        boolean success = inventoryDAO.updateInventory(productId, quantity);
        if (success) {
            invalidateCache();
        }
        return success;
    }

    @Override
    public Inventory getInventoryByProductId(int productId) {
        return inventoryDAO.getInventoryByProductId(productId);
    }

    @Override
    public List<Inventory> getAllInventory() {
        long now = System.currentTimeMillis();

        if (!inventoryCache.isEmpty() && (now - lastCacheUpdate) < CACHE_VALIDITY) {
            System.out.println("✓ Inventory from cache");
            return new ArrayList<>(inventoryCache);
        }

        System.out.println("✗ Fetching inventory from database");
        inventoryCache = inventoryDAO.getAllInventory();
        lastCacheUpdate = now;
        return new ArrayList<>(inventoryCache);
    }

    @Override
    public List<Inventory> getLowStockItems(int threshold) {
        return inventoryDAO.getLowStockItems(threshold);
    }

    /**
     * Business logic: Check if product is in stock
     */
    @Override
    public boolean isInStock(int productId) {
        Inventory inv = getInventoryByProductId(productId);
        return inv != null && inv.getQuantityAvailable() > 0;
    }

    /**
     * Business logic: Check if sufficient quantity available
     */
    @Override
    public boolean hasEnoughStock(int productId, int requestedQuantity) {
        Inventory inv = getInventoryByProductId(productId);
        return inv != null && inv.getQuantityAvailable() >= requestedQuantity;
    }

    /**
     * Business logic: Reduce stock (for order processing)
     */
    @Override
    public boolean reduceStock(int productId, int quantity) {
        Inventory inv = getInventoryByProductId(productId);
        if (inv != null && inv.getQuantityAvailable() >= quantity) {
            int newQuantity = inv.getQuantityAvailable() - quantity;
            return updateInventory(productId, newQuantity);
        }
        return false;
    }

    /**
     * Business logic: Restock
     */
    @Override
    public boolean addStock(int productId, int quantity) {
        Inventory inv = getInventoryByProductId(productId);
        if (inv != null) {
            int newQuantity = inv.getQuantityAvailable() + quantity;
            return updateInventory(productId, newQuantity);
        }
        return false;
    }

    /**
     * Filtering: Out of stock items
     */
    @Override
    public List<Inventory> getOutOfStockItems() {
        return getAllInventory().stream()
                .filter(i -> i.getQuantityAvailable() == 0)
                .collect(Collectors.toList());
    }

    /**
     * Sorting by quantity
     */
    @Override
    public List<Inventory> sortByQuantity(boolean ascending) {
        List<Inventory> items = getAllInventory();
        if (ascending) {
            items.sort(Comparator.comparing(Inventory::getQuantityAvailable));
        } else {
            items.sort(Comparator.comparing(Inventory::getQuantityAvailable).reversed());
        }
        return items;
    }

    /**
     * Update stock for a product (alias for updateInventory)
     */
    @Override
    public boolean updateStock(int productId, int quantity) {
        return updateInventory(productId, quantity);
    }

    private void invalidateCache() {
        inventoryCache.clear();
        lastCacheUpdate = 0;
    }
}