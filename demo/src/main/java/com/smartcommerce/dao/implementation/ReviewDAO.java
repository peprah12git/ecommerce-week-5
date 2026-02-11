package com.smartcommerce.dao.implementation;

import com.smartcommerce.dao.interfaces.ReviewDaoInterface;
import com.smartcommerce.model.Review;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReviewDAO implements ReviewDaoInterface {

    private DataSource dataSource;

    public ReviewDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public boolean addReview(Review review) {
        String sql = "INSERT INTO Reviews (user_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, review.getUserId());
            pstmt.setInt(2, review.getProductId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error adding review: " + e.getMessage());
        }
        return false;
    }

    @Override
    public Review getReviewById(int reviewId) {
        String sql = "SELECT * FROM Reviews WHERE review_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return extractReview(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching review: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Review> getReviewsByProductId(int productId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews WHERE product_id = ? ORDER BY review_date DESC";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(extractReview(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reviews by product: " + e.getMessage());
        }
        return reviews;
    }

    @Override
    public List<Review> getReviewsByUserId(int userId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews WHERE user_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                reviews.add(extractReview(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching reviews by user: " + e.getMessage());
        }
        return reviews;
    }

    @Override
    public List<Review> getAllReviews() {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM Reviews ORDER BY review_date DESC";
        try (Connection connection = dataSource.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                reviews.add(extractReview(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all reviews: " + e.getMessage());
        }
        return reviews;
    }

    @Override
    public boolean updateReview(Review review) {
        String sql = "UPDATE Reviews SET rating = ?, comment = ? WHERE review_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, review.getRating());
            pstmt.setString(2, review.getComment());
            pstmt.setInt(3, review.getReviewId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating review: " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM Reviews WHERE review_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reviewId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting review: " + e.getMessage());
        }
        return false;
    }

    private Review extractReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setReviewId(rs.getInt("review_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setProductId(rs.getInt("product_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setReviewDate(rs.getTimestamp("review_date"));
        return review;
    }

    @Override
    public double getAverageRating(int productId) {
        String sql = "SELECT AVG(rating) as avg_rating FROM Reviews WHERE product_id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            System.err.println("Error calculating average rating: " + e.getMessage());
        }
        return 0.0;
    }
}
