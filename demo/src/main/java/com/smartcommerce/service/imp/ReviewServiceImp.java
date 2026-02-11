package com.smartcommerce.service.imp;

import com.smartcommerce.dao.interfaces.ReviewDaoInterface;
import com.smartcommerce.dtos.request.UpdateReviewDTO;
import com.smartcommerce.exception.BusinessException;
import com.smartcommerce.exception.ResourceNotFoundException;
import com.smartcommerce.model.Review;
import com.smartcommerce.service.serviceInterface.ReviewService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ReviewServiceImp implements ReviewService {

    private ReviewDaoInterface reviewDao;

    @Override
    public Review createReview(Review review) {
        if (review.getRating() < 1 || review.getRating() > 5) {
            throw new BusinessException("Rating must be between 1 and 5");
        }
        if (review.getComment() == null || review.getComment().trim().isEmpty()) {
            throw new BusinessException("Review comment is required");
        }

        boolean success = reviewDao.addReview(review);
        if (!success) {
            throw new BusinessException("Failed to create review");
        }
        return review;
    }

    @Override
    @Transactional(readOnly = true)
    public Review getReviewById(int reviewId) {
        Review review = reviewDao.getReviewById(reviewId);
        if (review == null) {
            throw new ResourceNotFoundException("Review", "id", reviewId);
        }
        return review;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByProductId(int productId) {
        return reviewDao.getReviewsByProductId(productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getReviewsByUserId(int userId) {
        return reviewDao.getReviewsByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Review> getAllReviews() {
        return reviewDao.getAllReviews();
    }

    @Override
    public Review updateReview(int reviewId, UpdateReviewDTO dto) {
        Review existing = reviewDao.getReviewById(reviewId);
        if (existing == null) {
            throw new ResourceNotFoundException("Review", "id", reviewId);
        }

        existing.setRating(dto.rating());
        existing.setComment(dto.comment());

        boolean success = reviewDao.updateReview(existing);
        if (!success) {
            throw new BusinessException("Failed to update review");
        }

        return reviewDao.getReviewById(reviewId);
    }


    @Override
    public void deleteReview(int reviewId) {
        Review review = reviewDao.getReviewById(reviewId);
        if (review == null) {
            throw new ResourceNotFoundException("Review", "id", reviewId);
        }

        boolean success = reviewDao.deleteReview(reviewId);
        if (!success) {
            throw new BusinessException("Failed to delete review");
        }
    }
}
