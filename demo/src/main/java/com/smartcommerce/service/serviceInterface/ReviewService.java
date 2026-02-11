package com.smartcommerce.service.serviceInterface;

import com.smartcommerce.dtos.request.UpdateReviewDTO;
import com.smartcommerce.model.Review;
import java.util.List;

public interface ReviewService {
    Review createReview(Review review);
    Review getReviewById(int reviewId);
    List<Review> getReviewsByProductId(int productId);
    List<Review> getReviewsByUserId(int userId);
    List<Review> getAllReviews();
    Review updateReview(int reviewId, UpdateReviewDTO dto);
    void deleteReview(int reviewId);
}
