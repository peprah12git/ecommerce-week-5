package com.smartcommerce.dao.interfaces;

import com.smartcommerce.model.CartItem;
import java.math.BigDecimal;
import java.util.List;

public interface CartItemDaoInterface {
    /**
     * Add item to cart or update quantity if already exists
     * @param cartItem the cart item to add
     * @return true if operation successful, false otherwise
     */
    boolean addToCart(CartItem cartItem);

    /**
     * Get all cart items for a specific user
     * @param userId the user ID
     * @return list of cart items
     */
    List<CartItem> getCartItemsByUserId(int userId);

    /**
     * Get cart items with product details (joined query)
     * @param userId the user ID
     * @return list of cart items with product information
     */
    List<CartItem> getCartItemsWithDetails(int userId);

    /**
     * Get a specific cart item
     * @param userId the user ID
     * @param productId the product ID
     * @return the cart item or null if not found
     */
    CartItem getCartItem(int userId, int productId);

    /**
     * Update the quantity of a cart item
     * @param userId the user ID
     * @param productId the product ID
     * @param quantity the new quantity
     * @return true if update successful, false otherwise
     */
    boolean updateQuantity(int userId, int productId, int quantity);

    /**
     * Remove a specific item from cart
     * @param userId the user ID
     * @param productId the product ID
     * @return true if removal successful, false otherwise
     */
    boolean removeFromCart(int userId, int productId);

    /**
     * Clear all items from user's cart
     * @param userId the user ID
     * @return true if clear successful, false otherwise
     */
    boolean clearCart(int userId);

    /**
     * Get the total number of items in user's cart
     * @param userId the user ID
     * @return count of cart items
     */
    int getCartItemCount(int userId);

    /**
     * Calculate the total value of user's cart
     * @param userId the user ID
     * @return total cart value
     */
    BigDecimal getCartTotal(int userId);
}
