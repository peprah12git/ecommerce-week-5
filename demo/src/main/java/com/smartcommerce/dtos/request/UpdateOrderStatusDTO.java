package com.smartcommerce.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Request body for updating order status")
public record UpdateOrderStatusDTO(

        @NotBlank(message = "Status is required")
        @Pattern(regexp = "^(pending|confirmed|processing|shipped|delivered|cancelled)$", 
                 message = "Status must be one of: pending, confirmed, processing, shipped, delivered, cancelled")
        @Schema(description = "New order status", 
                example = "shipped", 
                allowableValues = {"pending", "confirmed", "processing", "shipped", "delivered", "cancelled"},
                requiredMode = Schema.RequiredMode.REQUIRED)
        String status
) {
}
