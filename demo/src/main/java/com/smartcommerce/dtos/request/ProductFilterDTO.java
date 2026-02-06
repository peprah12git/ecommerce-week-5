package com.smartcommerce.dtos.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.Min;

/**
 * DTO for product filtering parameters
 */
public record ProductFilterDTO(
        String category,
        
        @Min(value = 0, message = "Minimum price cannot be negative")
        BigDecimal minPrice,
        
        @Min(value = 0, message = "Maximum price cannot be negative")
        BigDecimal maxPrice,
        
        String searchTerm,
        
        Boolean inStock  // true = only in stock, false = only out of stock, null = all
) {
    /**
     * Check if any filter is applied
     */
    public boolean hasFilters() {
        return category != null || minPrice != null || maxPrice != null || 
               searchTerm != null || inStock != null;
    }
}