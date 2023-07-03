package ru.yandex.practicum.filmorate.review.service;

import ru.yandex.practicum.filmorate.review.model.ReviewDTO;

import java.util.List;

public interface ReviewService {

    ReviewDTO addReview(ReviewDTO review);

    ReviewDTO getReviewById(long reviewId);

    List<ReviewDTO> getAllReviews(Long filmId, Long count);

    ReviewDTO updateReview(ReviewDTO reviewDTO);

    void deleteReviewById(Long filmId);

    void addReviewLikeOrDislike(Long reviewId, Long userId, boolean positive);

    void deleteReviewLikeOrDislike(Long reviewId, Long userId, boolean positive);
}
