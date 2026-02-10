package com.smartcommerce.service.serviceInterface;

import com.smartcommerce.exception.BusinessException;
import com.smartcommerce.exception.DuplicateResourceException;
import com.smartcommerce.exception.ResourceNotFoundException;
import com.smartcommerce.model.Category;

import java.util.List;

/**
 * Service interface for Category entity
 * Defines business operations related to categories
 */
public interface CategoryServiceInterface {

    /**
     * Creates a new category
     *
     * @param category Category object to create
     * @return Created category
     * @throws DuplicateResourceException if category name already exists
     * @throws BusinessException          if category creation fails
     */
    Category createCategory(Category category);

    /**
     * Retrieves all categories
     *
     * @return List of all categories
     */
    List<Category> getAllCategories();

    /**
     * Retrieves a category by ID
     *
     * @param categoryId Category ID
     * @return Category object
     * @throws ResourceNotFoundException if category not found
     */
    Category getCategoryById(int categoryId);

    /**
     * Retrieves a category by name
     *
     * @param categoryName Category name
     * @return Category object
     * @throws ResourceNotFoundException if category not found
     * @throws BusinessException         if name is invalid
     */
    Category getCategoryByName(String categoryName);

    /**
     * Updates an existing category
     *
     * @param categoryId      Category ID to update
     * @param categoryDetails Updated category details
     * @return Updated category
     * @throws ResourceNotFoundException  if category not found
     * @throws DuplicateResourceException if category name already exists
     * @throws BusinessException          if update fails
     */
    Category updateCategory(int categoryId, Category categoryDetails);

    /**
     * Deletes a category
     *
     * @param categoryId Category ID to delete
     * @throws ResourceNotFoundException if category not found
     * @throws BusinessException         if deletion fails
     */
    void deleteCategory(int categoryId);
}
