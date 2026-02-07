package com.smartcommerce.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for updating an existing category")
public record UpdateCategoryDTO(

        @NotBlank(message = "Category name is required")
        @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
        @Schema(description = "Updated category name", example = "Home Appliances", requiredMode = Schema.RequiredMode.REQUIRED)
        String categoryName,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        @Schema(description = "Updated category description", example = "Large and small home appliances")
        String description
) {
}