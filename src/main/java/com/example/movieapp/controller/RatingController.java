package com.example.movieapp.controller;

import com.example.movieapp.dto.request.RatingRequest;
import com.example.movieapp.dto.response.RatingResponse;
import com.example.movieapp.security.CustomUserDetails;
import com.example.movieapp.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Ratings", description = "Movie ratings")
@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    @Operation(summary = "Create rating")
    @PostMapping
    public ResponseEntity<RatingResponse> create(
            @Valid @RequestBody RatingRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ratingService.createRating(user.getUser().getId(), request));
    }

    @Operation(summary = "Update rating")
    @PutMapping
    public ResponseEntity<RatingResponse> update(
            @Valid @RequestBody RatingRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(ratingService.updateRating(user.getUser().getId(), request));
    }

    @Operation(summary = "Delete rating")
    @DeleteMapping("/{tmdbId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long tmdbId,
            @AuthenticationPrincipal CustomUserDetails user) {
        ratingService.deleteRating(user.getUser().getId(), tmdbId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "My ratings")
    @GetMapping
    public ResponseEntity<List<RatingResponse>> myRatings(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(ratingService.getRatingsByUserId(user.getUser().getId()));
    }

    @Operation(summary = "Ratings for a movie")
    @GetMapping("/movie/{tmdbId}")
    public ResponseEntity<List<RatingResponse>> byMovie(@PathVariable Long tmdbId) {
        return ResponseEntity.ok(ratingService.getRatingsByTmdbId(tmdbId));
    }

    @Operation(summary = "Average rating for a movie")
    @GetMapping("/movie/{tmdbId}/average")
    public ResponseEntity<Double> average(@PathVariable Long tmdbId) {
        return ResponseEntity.ok(ratingService.getAverageRating(tmdbId));
    }
}

