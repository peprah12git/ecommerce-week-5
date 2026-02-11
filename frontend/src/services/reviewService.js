import api from './api';

const ReviewService = {
  createReview: async (reviewData) => {
    const response = await api.post('/reviews', reviewData);
    return response.data;
  },

  getReviewsByProductId: async (productId) => {
    const response = await api.get(`/reviews/product/${productId}`);
    return response.data;
  },

  updateReview: async (reviewId, reviewData) => {
    const response = await api.put(`/reviews/${reviewId}`, reviewData);
    return response.data;
  },

  deleteReview: async (reviewId) => {
    await api.delete(`/reviews/${reviewId}`);
  }
};

export default ReviewService;
