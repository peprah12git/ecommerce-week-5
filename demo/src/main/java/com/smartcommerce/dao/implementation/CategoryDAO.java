package com.smartcommerce.dao.implementation;

import com.smartcommerce.dao.interfaces.CategoryDaoInterface;
import com.smartcommerce.model.Category;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Category operations
 */
@Repository
public class CategoryDAO implements CategoryDaoInterface {
    private DataSource dataSource;

    // Constructor should accept DataSource as dependency injection
    public CategoryDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Get all categories
     */
    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Categories ORDER BY category_name";

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
                category.setParentCategoryId(rs.getObject("parent_category_id", Integer.class));
                categories.add(category);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching categories: " + e.getMessage());
            e.printStackTrace();
        }
        return categories;
    }

    /**
     * Add new category
     */
    @Override
    public boolean addCategory(Category category) {
        String sql = "INSERT INTO Categories (category_name, description, parent_category_id) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());

            if (category.getParentCategoryId() != null) {
                pstmt.setInt(3, category.getParentCategoryId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    category.setCategoryId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error adding category: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get category by ID
     */
    @Override
    public Category getCategoryById(int categoryId) {
        String sql = "SELECT * FROM Categories WHERE category_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Category category = new Category();
                category.setCategoryId(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                category.setDescription(rs.getString("description"));
                category.setParentCategoryId(rs.getObject("parent_category_id", Integer.class));
                return category;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching category: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Update category
     */
    @Override
    public boolean updateCategory(Category category) {
        String sql = "UPDATE Categories SET category_name = ?, description = ? WHERE category_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, category.getCategoryName());
            pstmt.setString(2, category.getDescription());
            pstmt.setInt(3, category.getCategoryId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating category: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Delete category
     */
    @Override
    public boolean deleteCategory(int categoryId) {
        String sql = "DELETE FROM Categories WHERE category_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting category: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}