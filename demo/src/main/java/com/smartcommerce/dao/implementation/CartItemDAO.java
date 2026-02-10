package com.smartcommerce.dao.implementation;

import com.smartcommerce.dao.interfaces.CartItemDaoInterface;
import com.smartcommerce.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CartItemDAO implements CartItemDaoInterface {
    private final DataSource dataSource;

    @Autowired
    public CartItemDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean addToCart(CartItem cartItem) {
        String sql = "INSERT INTO CartItems (user_id, product_id, quantity) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItem.getUserId());
            stmt.setInt(2, cartItem.getProductId());
            stmt.setInt(3, cartItem.getQuantity());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    @Override
    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM CartItems WHERE user_id = ?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                cartItems.add(mapResultSetToCartItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
        return cartItems;
    }

    @Override
    public List<CartItem> getCartItemsWithDetails(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT ci.*, p.name, p.price, p.description " +
                "FROM CartItems ci " +
                "JOIN Products p ON ci.product_id = p.product_id " +
                "WHERE ci.user_id = ?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CartItem item = mapResultSetToCartItem(rs);
                item.setProductName(rs.getString("name"));
                item.setProductPrice(rs.getBigDecimal("price"));
                item.setProductDescription(rs.getString("description"));
                cartItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
        return cartItems;
    }

    @Override
    public CartItem getCartItem(int userId, int productId) {
        String sql = "SELECT ci.*, p.name, p.price, p.description " +
                "FROM CartItems ci " +
                "JOIN Products p ON ci.product_id = p.product_id " +
                "WHERE ci.user_id = ? AND ci.product_id = ?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CartItem item = mapResultSetToCartItem(rs);
                item.setProductName(rs.getString("name"));
                item.setProductPrice(rs.getBigDecimal("price"));
                item.setProductDescription(rs.getString("description"));
                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
        return null;
    }

    @Override
    public boolean updateQuantity(int userId, int productId, int quantity) {
        String sql = "UPDATE CartItems SET quantity = ? WHERE user_id = ? AND product_id = ?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, userId);
            stmt.setInt(3, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    @Override
    public boolean removeFromCart(int userId, int productId) {
        String sql = "DELETE FROM CartItems WHERE user_id = ? AND product_id = ?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    @Override
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM CartItems WHERE user_id = ?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
    }

    @Override
    public int getCartItemCount(int userId) {
        String sql = "SELECT COUNT(*) FROM CartItems WHERE user_id = ?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
        return 0;
    }

    @Override
    public BigDecimal getCartTotal(int userId) {
        String sql = "SELECT SUM(ci.quantity * p.price) as total " +
                "FROM CartItems ci " +
                "JOIN Products p ON ci.product_id = p.product_id " +
                "WHERE ci.user_id = ?";

        Connection conn = DataSourceUtils.getConnection(dataSource);
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("total");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DataSourceUtils.releaseConnection(conn, dataSource);
        }
        return BigDecimal.ZERO;
    }

    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setCartItemId(rs.getInt("cart_item_id"));
        item.setUserId(rs.getInt("user_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setAddedAt(rs.getTimestamp("added_at"));
        item.setUpdatedAt(rs.getTimestamp("updated_at"));
        return item;
    }
}