package com.smartcommerce.dao.interfaces;

import com.models.Category;
import java.util.List;

/**
 * Interface for Category Data Access Object operations
 */
public interface CategoryDaoInterface {

    /**
     * Retrieves all categories from the database
     * @return List of all categories ordered by category_name
     */
    List<Category> getAllCategories();

    /**
     * Adds a new category to the database
     * @param category Category object to be added
     * @return true if category was successfully added, false otherwise
     */
    boolean addCategory(Category category);

    /**
     * Retrieves a category by its ID
     * @param categoryId Category ID to search for
     * @return Category object if found, null otherwise
     */
    Category getCategoryById(int categoryId);

    /**
     * Updates an existing category's information
     * @param category Category object with updated information
     * @return true if category was successfully updated, false otherwise
     */
    boolean updateCategory(Category category);

    /**
     * Deletes a category from the database
     * @param categoryId Category ID to delete
     * @return true if category was successfully deleted, false otherwise
     */
    boolean deleteCategory(int categoryId);
}

