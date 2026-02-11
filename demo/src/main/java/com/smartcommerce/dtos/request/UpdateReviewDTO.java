package com.smartcommerce.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body for updating a review")
public record UpdateReviewDTO(

        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        @Schema(description = "Product rating (1-5)", example = "4", requiredMode = Schema.RequiredMode.REQUIRED)
        int rating,

        @NotBlank(message = "Comment is required")
        @Schema(description = "Review comment", example = "Great product!", requiredMode = Schema.RequiredMode.REQUIRED)
        String comment
) {
}
