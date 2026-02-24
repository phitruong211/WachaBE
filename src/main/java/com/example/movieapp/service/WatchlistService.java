package com.example.movieapp.service;

import com.example.movieapp.dto.request.WatchlistRequest;
import com.example.movieapp.dto.response.WatchlistResponse;
import com.example.movieapp.model.WatchlistStatus;
import java.util.List;

public interface WatchlistService {
    WatchlistResponse addToWatchlist(Long userId, WatchlistRequest request);
    WatchlistResponse updateStatus(Long userId, Long tmdbId, String status);
    void removeFromWatchlist(Long userId, Long tmdbId);
    List<WatchlistResponse> getByUserId(Long userId);
    List<WatchlistResponse> getByUserIdAndStatus(Long userId, WatchlistStatus status);
    boolean isInWatchlist(Long userId, Long tmdbId);
}

