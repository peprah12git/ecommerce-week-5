package com.smartcommerce.dao.interfaces;

import com.smartcommerce.model.Product;

import java.util.List;

public interface ProductDaoInterface {
    /**
     * Retrieves all products for a specific category
     *
     * @param category The name of the category
     * @return List of products in the specified category, ordered by name
     */
    List<Product> getProductsByCategory(String category);

    /**
     * Adds a new product to the database
     *
     * @param product The product object to be added
     * @return true if the product was successfully added, false otherwise
     */
    boolean addProduct(Product product);

    /**
     * Retrieves all products from the database
     * Uses caching to improve performance
     *
     * @return List of all products, ordered by product ID (most recent first)
     */
    List<Product> getAllProducts();

    /**
     * Retrieves a specific product by its ID
     *
     * @param id The ID of the product
     * @return The product object if found, null otherwise
     */
    Product getProductById(int id);

    /**
     * Updates an existing product in the database
     *
     * @param product The product object with updated information
     * @return true if the product was successfully updated, false otherwise
     */
    boolean updateProduct(Product product);

    /**
     * Deletes a product from the database
     *
     * @param id The ID of the product to be deleted
     * @return true if the product was successfully deleted, false otherwise
     */
    boolean deleteProduct(int id);

    /**
     * Searches for products by name or description
     *
     * @param term The search term to match against product name or description
     * @return List of products matching the search term
     */
    List<Product> searchProducts(String term);

    /**
     * Invalidates the product cache, forcing a fresh database query on next access
     */
    void invalidateCache();

    /**
     * Retrieves cache statistics
     *
     * @return String containing cache hits, misses, and hit rate
     */
    static String getCacheStats() {
        return "";
    }
}