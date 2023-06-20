package ru.yandex.practicum.filmorate.mapper;

import ru.yandex.practicum.filmorate.dto.ReviewDTO;
import ru.yandex.practicum.filmorate.model.Review;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewMapper {

    public static ReviewDTO reviewToDTO(Review review) {
        if (review == null) {
            return null;
        }

        return ReviewDTO.builder()
                .id(review.getId())
                .content(review.getContent())
                .filmId(review.getFilmId())
                .userId(review.getUserId())
                .useful(review.getUseful())
                .isPositive(review.isPositive())
                .build();
    }

    public static Review dtoToReview(ReviewDTO reviewDTO) {
        if (reviewDTO == null) {
            return null;
        }

        return Review.builder()
                .id(reviewDTO.getId())
                .content(reviewDTO.getContent())
                .filmId(reviewDTO.getFilmId())
                .userId(reviewDTO.getUserId())
                .useful(reviewDTO.getUseful())
                .isPositive(reviewDTO.isPositive())
                .build();
    }

    public static List<ReviewDTO> listUsersToListDto(Collection<Review> reviews) {
        return reviews.stream().map(ReviewMapper::reviewToDTO).collect(Collectors.toList());
    }

}
