package com.example.movieapp.service;

import com.example.movieapp.dto.request.WatchingRequest;
import com.example.movieapp.dto.response.WatchingResponse;
import com.example.movieapp.model.WatchingStatus;
import java.util.List;

public interface WatchingService {
    WatchingResponse startOrUpdate(Long userId, WatchingRequest request);
    WatchingResponse getByUserAndMovie(Long userId, Long tmdbId);
    List<WatchingResponse> getByUserId(Long userId);
    List<WatchingResponse> getByUserIdAndStatus(Long userId, WatchingStatus status);
    void delete(Long userId, Long tmdbId);
}

