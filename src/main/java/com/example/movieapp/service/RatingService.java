package com.example.movieapp.service;

import com.example.movieapp.dto.request.RatingRequest;
import com.example.movieapp.dto.response.RatingResponse;
import java.util.List;

public interface RatingService {
    RatingResponse createRating(Long userId, RatingRequest request);
    RatingResponse updateRating(Long userId, RatingRequest request);
    void deleteRating(Long userId, Long tmdbId);
    RatingResponse getRatingByUserAndMovie(Long userId, Long tmdbId);
    List<RatingResponse> getRatingsByUserId(Long userId);
    List<RatingResponse> getRatingsByTmdbId(Long tmdbId);
    Double getAverageRating(Long tmdbId);
}

