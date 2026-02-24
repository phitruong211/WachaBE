package com.example.movieapp.repository;

import com.example.movieapp.model.Watchlist;
import com.example.movieapp.model.WatchlistStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    List<Watchlist> findByUserId(Long userId);
    List<Watchlist> findByUserIdAndStatus(Long userId, WatchlistStatus status);
    Optional<Watchlist> findByUserIdAndTmdbId(Long userId, Long tmdbId);
    boolean existsByUserIdAndTmdbId(Long userId, Long tmdbId);
    void deleteByUserIdAndTmdbId(Long userId, Long tmdbId);
}

