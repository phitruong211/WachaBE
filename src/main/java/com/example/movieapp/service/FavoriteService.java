package com.example.movieapp.service;

import com.example.movieapp.dto.response.FavoriteResponse;
import java.util.List;

public interface FavoriteService {
    FavoriteResponse addFavorite(Long userId, Long tmdbId);
    void removeFavorite(Long userId, Long tmdbId);
    List<FavoriteResponse> getFavoritesByUserId(Long userId);
    boolean isFavorite(Long userId, Long tmdbId);
}

