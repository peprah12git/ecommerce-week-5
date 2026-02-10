package com.smartcommerce.dtos.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateProductDTO(
        @NotBlank(message = "Product name is required")
        @Size(max = 255, message = "Product name cannot exceed 255 characters")
        String productName,

        String description,

        @NotNull(message = "Price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
        BigDecimal price,

        @NotNull(message = "Category ID is required")
        Integer categoryId,

        Integer quantityAvailable
) {
}