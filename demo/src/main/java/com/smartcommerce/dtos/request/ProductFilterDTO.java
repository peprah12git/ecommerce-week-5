package com.smartcommerce.dtos.request;

import java.math.BigDecimal;

import com.smartcommerce.validation.ValidPriceRange;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;

/**
 * DTO for product filtering parameters
 */
@ValidPriceRange
@Schema(description = "Product filter criteria for searching and filtering products")
public record ProductFilterDTO(

        @Schema(description = "Filter by category name", example = "Electronics")
        String category,

        @DecimalMin(value = "0.0", message = "Minimum price cannot be negative")
        @Schema(description = "Minimum price filter", example = "10.00")
        BigDecimal minPrice,

        @DecimalMin(value = "0.0", message = "Maximum price cannot be negative")
        @Schema(description = "Maximum price filter", example = "500.00")
        BigDecimal maxPrice,

        @Schema(description = "Search term for product name and description", example = "headphones")
        String searchTerm,

        @Schema(description = "Filter by stock status: true=in stock, false=out of stock, null=all")
        Boolean inStock
) {
    /**
     * Check if any filter is applied
     */
    public boolean hasFilters() {
        return category != null || minPrice != null || maxPrice != null ||
               searchTerm != null || inStock != null;
    }
}