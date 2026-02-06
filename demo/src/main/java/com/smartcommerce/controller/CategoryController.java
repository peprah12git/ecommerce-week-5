package com.smartcommerce.controller;

import com.smartcommerce.dtos.request.CreateCategoryDTO;
import com.smartcommerce.dtos.request.UpdateCategoryDTO;
import com.smartcommerce.model.Category;
import com.smartcommerce.service.imp.CategoryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Category management
 * Handles HTTP requests for category CRUD operations
 * Base URL: /api/categories
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Create a new category
     * POST /api/categories
     */
    @PostMapping
    public ResponseEntity<Category> createCategory(
            @Valid @RequestBody CreateCategoryDTO createCategoryDTO) {

        Category category = new Category(
                createCategoryDTO.categoryName(),
                createCategoryDTO.description()
        );

        if (createCategoryDTO.parentCategoryId() != null) {
            category.setParentCategoryId(createCategoryDTO.parentCategoryId());
        }

        Category createdCategory = categoryService.createCategory(category);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdCategory);
    }

    /**
     * Get all categories
     * GET /api/categories
     */
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Get category by ID
     * GET /api/categories/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Get category by name
     * GET /api/categories/name/{name}
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Category> getCategoryByName(@PathVariable String name) {
        Category category = categoryService.getCategoryByName(name);
        return ResponseEntity.ok(category);
    }

    /**
     * Update category
     * PUT /api/categories/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable int id,
            @Valid @RequestBody UpdateCategoryDTO updateCategoryDTO) {

        Category category = new Category();
        category.setCategoryName(updateCategoryDTO.categoryName());
        category.setDescription(updateCategoryDTO.description());

        if (updateCategoryDTO.parentCategoryId() != null) {
            category.setParentCategoryId(updateCategoryDTO.parentCategoryId());
        }

        Category updatedCategory = categoryService.updateCategory(id, category);

        return ResponseEntity.ok(updatedCategory);
    }

    /**
     * Delete category
     * DELETE /api/categories/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}