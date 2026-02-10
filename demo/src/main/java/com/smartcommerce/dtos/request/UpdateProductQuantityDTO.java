package com.smartcommerce.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request body for updating product stock quantity")
public record UpdateProductQuantityDTO(

        @NotNull(message = "Quantity is required")
        @Min(value = 0, message = "Quantity cannot be negative")
        @Schema(description = "New stock quantity", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
        Integer quantity
) {
}