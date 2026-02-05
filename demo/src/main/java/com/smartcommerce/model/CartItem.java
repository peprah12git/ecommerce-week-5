package com.smartcommerce.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CartItem {
    private int productId;
    private int cartItemId;
    private String productName;
    private int userId;
    private BigDecimal productPrice;
    private int quantity;
    private String productDescription;
    private Timestamp addedAt;
    private Timestamp updatedAt;

    public CartItem(){;
    }
    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public int getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(int cartItemId) {
        this.cartItemId = cartItemId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Timestamp getAddedAt() {
        return addedAt;
    }

    public void setAddedAt(Timestamp addedAt) {
        this.addedAt = addedAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getSubtotal() {
        if (productPrice == null) {
            return BigDecimal.ZERO;
        }
        return productPrice.multiply(BigDecimal.valueOf(quantity));
    }

}
// why BigDecimal: it is used instead of double or float for monetary calculations, it provides exact precision without rounding errors
