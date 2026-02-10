package com.smartcommerce.service.imp;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smartcommerce.dao.interfaces.CartItemDaoInterface;
import com.smartcommerce.dao.interfaces.ProductDaoInterface;
import com.smartcommerce.dao.interfaces.UserDaoInterface;
import com.smartcommerce.exception.BusinessException;
import com.smartcommerce.exception.ResourceNotFoundException;
import com.smartcommerce.model.CartItem;
import com.smartcommerce.model.Product;
import com.smartcommerce.model.User;
import com.smartcommerce.service.serviceInterface.CartItemService;

/**
 * Service layer for CartItem entity
 * Handles business logic, validation, and orchestration of cart operations
 */
@Service
@Transactional
public class CartItemServiceImp implements CartItemService {

    private final CartItemDaoInterface cartItemDao;
    private final UserDaoInterface userDao;
    private final ProductDaoInterface productDao;

    @Autowired
    public CartItemServiceImp(CartItemDaoInterface cartItemDao,
                               UserDaoInterface userDao,
                               ProductDaoInterface productDao) {
        this.cartItemDao = cartItemDao;
        this.userDao = userDao;
        this.productDao = productDao;
    }

    /**
     * Adds an item to the user's cart or updates quantity if already exists
     */
    @Override
    public CartItem addToCart(int userId, int productId, int quantity) {
        // Validate user exists
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        // Validate product exists
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }

        // Validate quantity
        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero");
        }

        // Check stock availability
        CartItem existingItem = cartItemDao.getCartItem(userId, productId);
        int totalQuantity = quantity + (existingItem != null ? existingItem.getQuantity() : 0);
        
        if (product.getQuantityAvailable() < totalQuantity) {
            throw new BusinessException("Insufficient stock for product: " + product.getProductName() +
                    ". Available: " + product.getQuantityAvailable() + ", Requested: " + totalQuantity);
        }

        // Create cart item and add to cart
        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(quantity);

        boolean success = cartItemDao.addToCart(cartItem);
        if (!success) {
            throw new BusinessException("Failed to add item to cart");
        }

        // Return the updated cart item with details
        return cartItemDao.getCartItem(userId, productId);
    }

    /**
     * Retrieves all cart items for a user (basic info)
     */
    @Override
    public List<CartItem> getCartItemsByUserId(int userId) {
        // Validate user exists
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        return cartItemDao.getCartItemsByUserId(userId);
    }

    /**
     * Retrieves all cart items for a user with product details
     */
    @Override
    public List<CartItem> getCartItemsWithDetails(int userId) {
        // Validate user exists
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        return cartItemDao.getCartItemsWithDetails(userId);
    }

    /**
     * Gets a specific cart item
     */
    @Override
    public CartItem getCartItem(int userId, int productId) {
        CartItem cartItem = cartItemDao.getCartItem(userId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "userId=" + userId + ", productId", productId);
        }
        return cartItem;
    }

    /**
     * Updates the quantity of a cart item
     */
    @Override
    public CartItem updateQuantity(int userId, int productId, int quantity) {
        // Validate cart item exists
        CartItem cartItem = cartItemDao.getCartItem(userId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "userId=" + userId + ", productId", productId);
        }

        // Validate quantity
        if (quantity <= 0) {
            throw new BusinessException("Quantity must be greater than zero. Use removeFromCart to delete items.");
        }

        // Check stock availability
        Product product = productDao.getProductById(productId);
        if (product == null) {
            throw new ResourceNotFoundException("Product", "id", productId);
        }

        if (product.getQuantityAvailable() < quantity) {
            throw new BusinessException("Insufficient stock for product: " + product.getProductName() +
                    ". Available: " + product.getQuantityAvailable() + ", Requested: " + quantity);
        }

        // Update quantity
        boolean success = cartItemDao.updateQuantity(userId, productId, quantity);
        if (!success) {
            throw new BusinessException("Failed to update cart item quantity");
        }

        // Return updated cart item
        return cartItemDao.getCartItem(userId, productId);
    }

    /**
     * Removes a specific item from the cart
     */
    @Override
    public void removeFromCart(int userId, int productId) {
        // Validate cart item exists
        CartItem cartItem = cartItemDao.getCartItem(userId, productId);
        if (cartItem == null) {
            throw new ResourceNotFoundException("CartItem", "userId=" + userId + ", productId", productId);
        }

        boolean success = cartItemDao.removeFromCart(userId, productId);
        if (!success) {
            throw new BusinessException("Failed to remove item from cart");
        }
    }

    /**
     * Clears all items from a user's cart
     */
    @Override
    public void clearCart(int userId) {
        // Validate user exists
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        // Clear the cart (will return false if cart is already empty, which is okay)
        cartItemDao.clearCart(userId);
    }

    /**
     * Gets the count of items in a user's cart
     */
    @Override
    public int getCartItemCount(int userId) {
        // Validate user exists
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        return cartItemDao.getCartItemCount(userId);
    }

    /**
     * Calculates the total value of a user's cart
     */
    @Override
    public BigDecimal getCartTotal(int userId) {
        // Validate user exists
        User user = userDao.getUserById(userId);
        if (user == null) {
            throw new ResourceNotFoundException("User", "id", userId);
        }

        return cartItemDao.getCartTotal(userId);
    }
}
