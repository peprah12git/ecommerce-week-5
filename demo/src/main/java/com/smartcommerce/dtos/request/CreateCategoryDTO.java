package com.smartcommerce.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for creating a new category")
public record CreateCategoryDTO(

        @NotBlank(message = "Category name is required")
        @Size(min = 2, max = 100, message = "Category name must be between 2 and 100 characters")
        @Schema(description = "Name of the category", example = "Electronics", requiredMode = Schema.RequiredMode.REQUIRED)
        String categoryName,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        @Schema(description = "Category description", example = "Electronic devices and accessories")
        String description,

        @Positive(message = "Parent category ID must be a positive number")
        @Schema(description = "ID of the parent category for subcategories", example = "1")
        Integer parentCategoryId
) {
}