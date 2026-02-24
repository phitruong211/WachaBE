package com.example.movieapp.repository;

import com.example.movieapp.model.Watching;
import com.example.movieapp.model.WatchingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchingRepository extends JpaRepository<Watching, Long> {
    List<Watching> findByUserId(Long userId);
    List<Watching> findByUserIdAndStatus(Long userId, WatchingStatus status);
    Optional<Watching> findByUserIdAndTmdbId(Long userId, Long tmdbId);
    boolean existsByUserIdAndTmdbId(Long userId, Long tmdbId);
}

