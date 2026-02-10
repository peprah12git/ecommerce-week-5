package com.smartcommerce.service.serviceInterface;

import java.math.BigDecimal;
import java.util.List;

import com.smartcommerce.exception.BusinessException;
import com.smartcommerce.exception.ResourceNotFoundException;
import com.smartcommerce.model.CartItem;

/**
 * Service interface for CartItem entity
 * Defines business operations related to shopping cart items
 */
public interface CartItemService {

    /**
     * Adds an item to the user's cart or updates quantity if already exists
     *
     * @param userId the user ID
     * @param productId the product ID to add
     * @param quantity the quantity to add
     * @return the added/updated cart item
     * @throws ResourceNotFoundException if user or product not found
     * @throws BusinessException if operation fails
     */
    CartItem addToCart(int userId, int productId, int quantity);

    /**
     * Retrieves all cart items for a user (basic info)
     *
     * @param userId the user ID
     * @return list of cart items
     * @throws ResourceNotFoundException if user not found
     */
    List<CartItem> getCartItemsByUserId(int userId);

    /**
     * Retrieves all cart items for a user with product details
     *
     * @param userId the user ID
     * @return list of cart items with product information
     * @throws ResourceNotFoundException if user not found
     */
    List<CartItem> getCartItemsWithDetails(int userId);

    /**
     * Gets a specific cart item
     *
     * @param userId the user ID
     * @param productId the product ID
     * @return the cart item
     * @throws ResourceNotFoundException if cart item not found
     */
    CartItem getCartItem(int userId, int productId);

    /**
     * Updates the quantity of a cart item
     *
     * @param userId the user ID
     * @param productId the product ID
     * @param quantity the new quantity
     * @return the updated cart item
     * @throws ResourceNotFoundException if cart item not found
     * @throws BusinessException if quantity is invalid
     */
    CartItem updateQuantity(int userId, int productId, int quantity);

    /**
     * Removes a specific item from the cart
     *
     * @param userId the user ID
     * @param productId the product ID
     * @throws ResourceNotFoundException if cart item not found
     */
    void removeFromCart(int userId, int productId);

    /**
     * Clears all items from a user's cart
     *
     * @param userId the user ID
     * @throws ResourceNotFoundException if user not found
     */
    void clearCart(int userId);

    /**
     * Gets the count of items in a user's cart
     *
     * @param userId the user ID
     * @return count of cart items
     * @throws ResourceNotFoundException if user not found
     */
    int getCartItemCount(int userId);

    /**
     * Calculates the total value of a user's cart
     *
     * @param userId the user ID
     * @return total cart value
     * @throws ResourceNotFoundException if user not found
     */
    BigDecimal getCartTotal(int userId);
}
