package com.example.movieapp.repository;

import com.example.movieapp.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByUserId(Long userId);
    List<Rating> findByTmdbId(Long tmdbId);
    Optional<Rating> findByUserIdAndTmdbId(Long userId, Long tmdbId);
    boolean existsByUserIdAndTmdbId(Long userId, Long tmdbId);

    @Query("SELECT AVG(r.ratingValue) FROM Rating r WHERE r.tmdbId = :tmdbId")
    Double getAverageRatingByTmdbId(@Param("tmdbId") Long tmdbId);
}

