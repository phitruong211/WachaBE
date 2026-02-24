package com.example.movieapp.service;

import com.example.movieapp.dto.request.WatchlistRequest;
import com.example.movieapp.dto.response.WatchlistResponse;
import com.example.movieapp.exception.ResourceNotFoundException;
import com.example.movieapp.model.User;
import com.example.movieapp.model.Watchlist;
import com.example.movieapp.model.WatchlistStatus;
import com.example.movieapp.repository.UserRepository;
import com.example.movieapp.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;
    private final UserRepository userRepository;

    @Override
    public WatchlistResponse addToWatchlist(Long userId, WatchlistRequest request) {
        if (watchlistRepository.existsByUserIdAndTmdbId(userId, request.getTmdbId())) {
            throw new IllegalArgumentException("Movie already in watchlist");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        WatchlistStatus status = request.getStatus() != null
                ? WatchlistStatus.valueOf(request.getStatus().toUpperCase())
                : WatchlistStatus.PLANNED;
        Watchlist wl = Watchlist.builder().user(user).tmdbId(request.getTmdbId()).status(status).build();
        return toResponse(watchlistRepository.save(wl));
    }

    @Override
    public WatchlistResponse updateStatus(Long userId, Long tmdbId, String status) {
        Watchlist wl = watchlistRepository.findByUserIdAndTmdbId(userId, tmdbId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist entry not found"));
        wl.setStatus(WatchlistStatus.valueOf(status.toUpperCase()));
        return toResponse(watchlistRepository.save(wl));
    }

    @Override
    public void removeFromWatchlist(Long userId, Long tmdbId) {
        Watchlist wl = watchlistRepository.findByUserIdAndTmdbId(userId, tmdbId)
                .orElseThrow(() -> new ResourceNotFoundException("Watchlist entry not found"));
        watchlistRepository.delete(wl);
    }

    @Override @Transactional(readOnly = true)
    public List<WatchlistResponse> getByUserId(Long userId) {
        return watchlistRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public List<WatchlistResponse> getByUserIdAndStatus(Long userId, WatchlistStatus status) {
        return watchlistRepository.findByUserIdAndStatus(userId, status).stream().map(this::toResponse).toList();
    }

    @Override @Transactional(readOnly = true)
    public boolean isInWatchlist(Long userId, Long tmdbId) {
        return watchlistRepository.existsByUserIdAndTmdbId(userId, tmdbId);
    }

    private WatchlistResponse toResponse(Watchlist wl) {
        return WatchlistResponse.builder()
                .id(wl.getId())
                .userId(wl.getUser().getId())
                .username(wl.getUser().getUsername())
                .tmdbId(wl.getTmdbId())
                .status(wl.getStatus())
                .addedAt(wl.getAddedAt())
                .build();
    }
}

