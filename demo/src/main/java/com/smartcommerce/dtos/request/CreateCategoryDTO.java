package com.smartcommerce.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCategoryDTO(
        @NotBlank(message = "Category name is required")
        @Size(max = 100, message = "Category name cannot exceed 100 characters")
        String categoryName,

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        Integer parentCategoryId
) {
}