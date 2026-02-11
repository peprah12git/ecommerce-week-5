package com.smartcommerce.dtos.response;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Cart summary with all items and totals")
public class CartResponse {

    @JsonProperty("userId")
    @Schema(description = "User ID who owns this cart", example = "1")
    private int userId;

    @JsonProperty("items")
    @Schema(description = "List of items in the cart")
    private List<CartItemResponse> items;

    @JsonProperty("itemCount")
    @Schema(description = "Total number of items in the cart", example = "3")
    private int itemCount;

    @JsonProperty("totalAmount")
    @Schema(description = "Total value of all items in the cart", example = "149.97")
    private BigDecimal totalAmount;

    public CartResponse() {}

    public CartResponse(int userId, List<CartItemResponse> items, int itemCount, BigDecimal totalAmount) {
        this.userId = userId;
        this.items = items;
        this.itemCount = itemCount;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public List<CartItemResponse> getItems() { return items; }
    public void setItems(List<CartItemResponse> items) { this.items = items; }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
}
