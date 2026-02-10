package com.smartcommerce.dtos.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Order item information returned by the API")
public class OrderItemResponse {

    @JsonProperty("orderItemId")
    @Schema(description = "Unique order item identifier", example = "1")
    private int orderItemId;

    @JsonProperty("orderId")
    @Schema(description = "Order ID this item belongs to", example = "1")
    private int orderId;

    @JsonProperty("productId")
    @Schema(description = "Product ID", example = "5")
    private int productId;

    @JsonProperty("productName")
    @Schema(description = "Product name", example = "Wireless Headphones")
    private String productName;

    @JsonProperty("quantity")
    @Schema(description = "Quantity ordered", example = "2")
    private int quantity;

    @JsonProperty("unitPrice")
    @Schema(description = "Unit price at time of order", example = "49.99")
    private BigDecimal unitPrice;

    @JsonProperty("subtotal")
    @Schema(description = "Subtotal (quantity * unit price)", example = "99.98")
    private BigDecimal subtotal;

    public OrderItemResponse() {}

    public OrderItemResponse(int orderItemId, int orderId, int productId, String productName,
                              int quantity, BigDecimal unitPrice, BigDecimal subtotal) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.subtotal = subtotal;
    }

    // Getters and Setters
    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getSubtotal() { return subtotal; }
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}
