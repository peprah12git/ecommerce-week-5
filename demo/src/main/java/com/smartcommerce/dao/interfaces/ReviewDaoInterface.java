package com.smartcommerce.dao.interfaces;

import com.smartcommerce.model.Review;
import java.util.List;

public interface ReviewDaoInterface {
    /**
     * Adds a new review to the database
     * @param review The review object to be added
     * @return true if the review was successfully added, false otherwise
     */
    boolean addReview(Review review);

    /**
     * Retrieves all reviews for a specific product
     * @param productId The ID of the product
     * @return List of reviews for the specified product, ordered by date (most recent first)
     */
    List<Review> getReviewsByProductId(int productId);

    /**
     * Retrieves all reviews from the database
     * @return List of all reviews, ordered by date (most recent first)
     */
    List<Review> getAllReviews();

    /**
     * Deletes a review from the database
     * @param id The ID of the review to be deleted
     * @return true if the review was successfully deleted, false otherwise
     */
    boolean deleteReview(int id);

    /**
     * Calculates the average rating for a specific product
     * @param productId The ID of the product
     * @return The average rating as a double, or 0.0 if no reviews exist
     */
    double getAverageRating(int productId);
}