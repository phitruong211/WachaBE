package com.example.movieapp.service;

import com.example.movieapp.dto.request.RatingRequest;
import com.example.movieapp.dto.response.RatingResponse;
import com.example.movieapp.exception.ResourceNotFoundException;
import com.example.movieapp.model.Rating;
import com.example.movieapp.model.User;
import com.example.movieapp.repository.RatingRepository;
import com.example.movieapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    @Override
    public RatingResponse createRating(Long userId, RatingRequest request) {
        if (ratingRepository.existsByUserIdAndTmdbId(userId, request.getTmdbId())) {
            throw new IllegalArgumentException("Already rated this movie");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Rating rating = Rating.builder()
                .user(user)
                .tmdbId(request.getTmdbId())
                .ratingValue(request.getRatingValue())
                .reviewText(request.getReviewText())
                .build();
        return toResponse(ratingRepository.save(rating));
    }

    @Override
    public RatingResponse updateRating(Long userId, RatingRequest request) {
        Rating rating = ratingRepository.findByUserIdAndTmdbId(userId, request.getTmdbId())
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));
        rating.setRatingValue(request.getRatingValue());
        rating.setReviewText(request.getReviewText());
        return toResponse(ratingRepository.save(rating));
    }

    @Override
    public void deleteRating(Long userId, Long tmdbId) {
        Rating rating = ratingRepository.findByUserIdAndTmdbId(userId, tmdbId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));
        ratingRepository.delete(rating);
    }

    @Override @Transactional(readOnly = true)
    public RatingResponse getRatingByUserAndMovie(Long userId, Long tmdbId) {
        Rating rating = ratingRepository.findByUserIdAndTmdbId(userId, tmdbId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));
        return toResponse(rating);
    }

    @Override @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsByUserId(Long userId) {
        return ratingRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsByTmdbId(Long tmdbId) {
        return ratingRepository.findByTmdbId(tmdbId).stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public Double getAverageRating(Long tmdbId) {
        return ratingRepository.getAverageRatingByTmdbId(tmdbId);
    }

    private RatingResponse toResponse(Rating r) {
        return RatingResponse.builder()
                .id(r.getId())
                .userId(r.getUser().getId())
                .username(r.getUser().getUsername())
                .tmdbId(r.getTmdbId())
                .ratingValue(r.getRatingValue())
                .reviewText(r.getReviewText())
                .ratedAt(r.getRatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}

