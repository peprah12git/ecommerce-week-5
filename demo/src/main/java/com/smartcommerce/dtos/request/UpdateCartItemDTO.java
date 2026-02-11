package com.smartcommerce.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for updating cart item quantity")
public record UpdateCartItemDTO(

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        @Schema(description = "New quantity for the cart item", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer quantity
) {
}
