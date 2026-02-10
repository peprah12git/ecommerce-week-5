package com.smartcommerce.dtos.request;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Order item details for creating an order")
public record OrderItemDTO(

        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be a positive number")
        @Schema(description = "ID of the product to order", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer productId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Schema(description = "Quantity of the product", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer quantity,

        @Schema(description = "Unit price (optional, defaults to product price)", example = "29.99")
        BigDecimal unitPrice
) {
}
