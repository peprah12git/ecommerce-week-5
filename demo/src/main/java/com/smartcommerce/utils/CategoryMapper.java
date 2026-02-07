package com.smartcommerce.utils;

import com.smartcommerce.dtos.response.CategoryResponse;
import com.smartcommerce.model.Category;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper utility class for Category entity and DTOs
 */
public class CategoryMapper {

    /**
     * Converts Category entity to CategoryResponse DTO
     */
    public static CategoryResponse toCategoryResponse(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponse response = new CategoryResponse();
        response.setCategoryId(category.getCategoryId());
        response.setCategoryName(category.getCategoryName());
        response.setDescription(category.getDescription());
        response.setCreatedAt(category.getCreatedAt());

        return response;
    }

    /**
     * Converts list of Category entities to list of CategoryResponse DTOs
     */
    public static List<CategoryResponse> toCategoryResponseList(List<Category> categories) {
        if (categories == null) {
            return null;
        }

        return categories.stream()
                .map(CategoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }
}