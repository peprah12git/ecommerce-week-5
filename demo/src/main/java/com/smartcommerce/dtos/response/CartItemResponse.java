package com.smartcommerce.dtos.response;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cart item information returned by the API")
public class CartItemResponse {

    @JsonProperty("cartItemId")
    @Schema(description = "Unique cart item identifier", example = "1")
    private int cartItemId;

    @JsonProperty("userId")
    @Schema(description = "User ID who owns this cart item", example = "1")
    private int userId;

    @JsonProperty("productId")
    @Schema(description = "Product ID", example = "5")
    private int productId;

    @JsonProperty("productName")
    @Schema(description = "Product name", example = "Wireless Headphones")
    private String productName;

    @JsonProperty("productPrice")
    @Schema(description = "Product unit price", example = "49.99")
    private BigDecimal productPrice;

    @JsonProperty("productDescription")
    @Schema(description = "Product description", example = "High-quality wireless headphones")
    private String productDescription;

    @JsonProperty("quantity")
    @Schema(description = "Quantity in cart", example = "2")
    private int quantity;

    @JsonProperty("subtotal")
    @Schema(description = "Subtotal (quantity * price)", example = "99.98")
    private BigDecimal subtotal;

    @JsonProperty("addedAt")
    @Schema(description = "Timestamp when item was added to cart")
    private Timestamp addedAt;

    @JsonProperty("updatedAt")
    @Schema(description = "Timestamp when item was last updated")
    private Timestamp updatedAt;

    public CartItemResponse() {}

    // Getters and Setters
    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getProductPrice() { return productPrice; }
    public void setProductPrice(BigDecimal productPrice) { this.productPrice = productPrice; }

    public String getProductDescription() { return productDescription; }
    public void setProductDescription(String productDescription) { this.productDescription = productDescription; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

    public Timestamp getAddedAt() { return addedAt; }
    public void setAddedAt(Timestamp addedAt) { this.addedAt = addedAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
