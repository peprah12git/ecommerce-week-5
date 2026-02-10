package com.smartcommerce.dtos.response;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Order information returned by the API")
public class OrderResponse {

    @JsonProperty("orderId")
    @Schema(description = "Unique order identifier", example = "1")
    private int orderId;

    @JsonProperty("userId")
    @Schema(description = "User ID who placed the order", example = "1")
    private int userId;

    @JsonProperty("userName")
    @Schema(description = "Name of the user", example = "John Doe")
    private String userName;

    @JsonProperty("orderDate")
    @Schema(description = "Date and time when order was placed")
    private Timestamp orderDate;

    @JsonProperty("status")
    @Schema(description = "Current order status", example = "pending",
            allowableValues = {"pending", "confirmed", "processing", "shipped", "delivered", "cancelled"})
    private String status;

    @JsonProperty("totalAmount")
    @Schema(description = "Total order amount", example = "149.97")
    private BigDecimal totalAmount;

    @JsonProperty("items")
    @Schema(description = "List of items in the order")
    private List<OrderItemResponse> items;

    @JsonProperty("itemCount")
    @Schema(description = "Number of items in the order", example = "3")
    private int itemCount;

    public OrderResponse() {}

    public OrderResponse(int orderId, int userId, String userName, Timestamp orderDate,
                         String status, BigDecimal totalAmount, List<OrderItemResponse> items) {
        this.orderId = orderId;
        this.userId = userId;
        this.userName = userName;
        this.orderDate = orderDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.items = items;
        this.itemCount = items != null ? items.size() : 0;
    }

    // Getters and Setters
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public List<OrderItemResponse> getItems() { return items; }
    public void setItems(List<OrderItemResponse> items) { 
        this.items = items;
        this.itemCount = items != null ? items.size() : 0;
    }

    public int getItemCount() { return itemCount; }
    public void setItemCount(int itemCount) { this.itemCount = itemCount; }
}
