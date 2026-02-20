package com.smartcommerce.dao.implementation;

import com.smartcommerce.dao.interfaces.ProductDaoInterface;
import com.smartcommerce.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Data Access Object for Products with in-memory caching
 * All logging is silent - no UI exposure
 */
@Slf4j
@Repository
public class ProductDAO implements ProductDaoInterface {
    private DataSource dataSource;

    public ProductDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private static Map<Integer, Product> productCache = null;
    private static long cacheTimestamp = 0;
    private static final long CACHE_TTL_MS = 300000;
    private static int cacheHits = 0;
    private static int cacheMisses = 0;

    private boolean isCacheValid() {

        if (productCache == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - cacheTimestamp;

        return timeDifference < CACHE_TTL_MS;
    }


    public void invalidateCache() {

        // Step 1: Clear the cached products (romves it from memory)
        if (productCache != null) {
            productCache = null;
        }

        // Step 2: Reset the cache timestamp
        cacheTimestamp = 0;
    }


    public static String getCacheStats() {
        int total = cacheHits + cacheMisses;
        double hitRate = total > 0 ? (cacheHits * 100.0 / total) : 0;
        return String.format("[CACHE] Hits: %d, Misses: %d, Hit Rate: %.1f%%",
                cacheHits, cacheMisses, hitRate);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.category_name, COALESCE(i.quantity_available, 0) as quantity " +
                "FROM Products p " +
                "LEFT JOIN Categories c ON p.category_id = c.category_id " +
                "LEFT JOIN Inventory i ON p.product_id = i.product_id " +
                "WHERE c.category_name = ? ORDER BY p.name";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(extractProduct(rs));
            }
        } catch (SQLException e) {
            // Silent
        }

        return products;
    }

    @Override
    public boolean addProduct(Product product) {
        String sql = "INSERT INTO Products (name, description, price, category_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setInt(4, product.getCategoryId());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    product.setProductId(rs.getInt(1));
                    createInventoryEntry(product.getProductId());
                }
                invalidateCache();
                return true;
            }
        } catch (SQLException e) {
            // Silent
        }
        return false;
    }

    private void createInventoryEntry(int productId) {
        String sql = "INSERT INTO Inventory (product_id, quantity_available) VALUES (?, 0)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Silent
        }
    }

    @Override
    public List<Product> getAllProducts() {
        if (isCacheValid()) {
            cacheHits++;
            return new ArrayList<>(productCache.values());
        }

        cacheMisses++;

        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.category_name, COALESCE(i.quantity_available, 0) as quantity " +
                "FROM Products p " +
                "LEFT JOIN Categories c ON p.category_id = c.category_id " +
                "LEFT JOIN Inventory i ON p.product_id = i.product_id " +
                "ORDER BY p.product_id DESC";

        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(extractProduct(rs));
            }

            productCache = products.stream()
                    .collect(Collectors.toMap(Product::getProductId, p -> p));
            cacheTimestamp = System.currentTimeMillis();

        } catch (SQLException e) {
            // Silent
        }

        return products;
    }

    @Override
    public Product getProductById(int id) {
        if (isCacheValid() && productCache.containsKey(id)) {
            cacheHits++;
            return productCache.get(id);
        }

        cacheMisses++;
        String sql = "SELECT p.*, c.category_name, COALESCE(i.quantity_available, 0) as quantity " +
                "FROM Products p " +
                "LEFT JOIN Categories c ON p.category_id = c.category_id " +
                "LEFT JOIN Inventory i ON p.product_id = i.product_id " +
                "WHERE p.product_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractProduct(rs);
            }
        } catch (SQLException e) {
            // Silent
        }

        return null;
    }

    @Override
    public boolean updateProduct(Product product) {
        String sql = "UPDATE Products SET name = ?, description = ?, price = ? WHERE product_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, product.getProductName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setInt(4, product.getProductId());

            boolean updated = pstmt.executeUpdate() > 0;
            if (updated) {
                invalidateCache();
            }
            return updated;

        } catch (SQLException e) {
            // Silent
        }

        return false;
    }

    @Override
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM Products WHERE product_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            boolean deleted = pstmt.executeUpdate() > 0;
            if (deleted) {
                invalidateCache();
            }
            return deleted;
        } catch (SQLException e) {
            // Silent
        }
        return false;
    }

    public List<Product> searchProducts(String term) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT p.*, c.category_name, COALESCE(i.quantity_available, 0) as quantity " +
                "FROM Products p " +
                "LEFT JOIN Categories c ON p.category_id = c.category_id " +
                "LEFT JOIN Inventory i ON p.product_id = i.product_id " +
                "WHERE p.name LIKE ? OR p.description LIKE ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String pattern = "%" + term + "%";
            pstmt.setString(1, pattern);
            pstmt.setString(2, pattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(extractProduct(rs));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return products;
    }

    private Product extractProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setProductName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setCategoryName(rs.getString("category_name"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setQuantityAvailable(rs.getInt("quantity"));
        return p;
    }
}