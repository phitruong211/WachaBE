package com.example.movieapp.service;

import com.example.movieapp.dto.request.WatchingRequest;
import com.example.movieapp.dto.response.WatchingResponse;
import com.example.movieapp.exception.ResourceNotFoundException;
import com.example.movieapp.model.User;
import com.example.movieapp.model.Watching;
import com.example.movieapp.model.WatchingStatus;
import com.example.movieapp.repository.UserRepository;
import com.example.movieapp.repository.WatchingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WatchingServiceImpl implements WatchingService {

    private final WatchingRepository watchingRepository;
    private final UserRepository userRepository;

    @Override
    public WatchingResponse startOrUpdate(Long userId, WatchingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Watching watching = watchingRepository.findByUserIdAndTmdbId(userId, request.getTmdbId())
                .orElse(Watching.builder()
                        .user(user)
                        .tmdbId(request.getTmdbId())
                        .progressTime(0)
                        .progressPercentage(0.0)
                        .status(WatchingStatus.WATCHING)
                        .build());

        if (request.getProgressTime() != null) watching.setProgressTime(request.getProgressTime());
        if (request.getProgressPercentage() != null) watching.setProgressPercentage(request.getProgressPercentage());
        if (request.getStatus() != null) {
            WatchingStatus status = WatchingStatus.valueOf(request.getStatus().toUpperCase());
            watching.setStatus(status);
            if (status == WatchingStatus.COMPLETED) watching.setCompletedAt(LocalDateTime.now());
        }
        watching.setLastWatchedAt(LocalDateTime.now());

        return toResponse(watchingRepository.save(watching));
    }

    @Override @Transactional(readOnly = true)
    public WatchingResponse getByUserAndMovie(Long userId, Long tmdbId) {
        Watching w = watchingRepository.findByUserIdAndTmdbId(userId, tmdbId)
                .orElseThrow(() -> new ResourceNotFoundException("Watching not found"));
        return toResponse(w);
    }

    @Override @Transactional(readOnly = true)
    public List<WatchingResponse> getByUserId(Long userId) {
        return watchingRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<WatchingResponse> getByUserIdAndStatus(Long userId, WatchingStatus status) {
        return watchingRepository.findByUserIdAndStatus(userId, status).stream().map(this::toResponse).toList();
    }

    @Override
    public void delete(Long userId, Long tmdbId) {
        Watching w = watchingRepository.findByUserIdAndTmdbId(userId, tmdbId)
                .orElseThrow(() -> new ResourceNotFoundException("Watching not found"));
        watchingRepository.delete(w);
    }

    private WatchingResponse toResponse(Watching w) {
        return WatchingResponse.builder()
                .id(w.getId())
                .userId(w.getUser().getId())
                .username(w.getUser().getUsername())
                .tmdbId(w.getTmdbId())
                .progressTime(w.getProgressTime())
                .progressPercentage(w.getProgressPercentage())
                .status(w.getStatus())
                .startedAt(w.getStartedAt())
                .lastWatchedAt(w.getLastWatchedAt())
                .completedAt(w.getCompletedAt())
                .build();
    }
}

