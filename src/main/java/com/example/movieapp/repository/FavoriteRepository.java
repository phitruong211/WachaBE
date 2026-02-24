package com.example.movieapp.repository;

import com.example.movieapp.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    List<Favorite> findByTmdbId(Long tmdbId);
    Optional<Favorite> findByUserIdAndTmdbId(Long userId, Long tmdbId);
    boolean existsByUserIdAndTmdbId(Long userId, Long tmdbId);
    void deleteByUserIdAndTmdbId(Long userId, Long tmdbId);
}

