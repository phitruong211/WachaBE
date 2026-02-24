package com.example.movieapp.controller;

import com.example.movieapp.dto.response.MovieDetailResponse;
import com.example.movieapp.integration.tmdb.TmdbService;
import com.example.movieapp.integration.tmdb.dto.TmdbImageResponse;
import com.example.movieapp.integration.tmdb.dto.TmdbMovieListResponse;
import com.example.movieapp.security.CustomUserDetails;
import com.example.movieapp.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Movies", description = "Movie browsing (data from TMDB)")
@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    private final TmdbService tmdbService;

    @Operation(summary = "Popular movies")
    @GetMapping("/popular")
    public ResponseEntity<TmdbMovieListResponse> getPopular(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String language) {
        return ResponseEntity.ok(movieService.getPopular(page, language));
    }

    @Operation(summary = "Top rated movies")
    @GetMapping("/top-rated")
    public ResponseEntity<TmdbMovieListResponse> getTopRated(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String language) {
        return ResponseEntity.ok(movieService.getTopRated(page, language));
    }

    @Operation(summary = "Upcoming movies")
    @GetMapping("/upcoming")
    public ResponseEntity<TmdbMovieListResponse> getUpcoming(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String language) {
        return ResponseEntity.ok(movieService.getUpcoming(page, language));
    }

    @Operation(summary = "Search movies")
    @GetMapping("/search")
    public ResponseEntity<TmdbMovieListResponse> searchMovies(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String language) {
        return ResponseEntity.ok(movieService.searchMovies(query, page, language));
    }

    @Operation(summary = "Movie detail + user interaction")
    @GetMapping("/{tmdbId}")
    public ResponseEntity<MovieDetailResponse> getMovieDetail(
            @PathVariable Long tmdbId,
            @RequestParam(required = false) String language,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails != null ? userDetails.getUser().getId() : null;
        return ResponseEntity.ok(movieService.getMovieDetail(tmdbId, userId, language));
    }

    @Operation(summary = "Movie trailer URLs")
    @GetMapping("/{tmdbId}/trailers")
    public ResponseEntity<List<String>> getTrailers(
            @PathVariable Long tmdbId,
            @RequestParam(required = false) String language) {
        return ResponseEntity.ok(tmdbService.getTrailerUrls(tmdbId, language));
    }

    @Operation(summary = "Movie images (posters + backdrops)")
    @GetMapping("/{tmdbId}/images")
    public ResponseEntity<TmdbImageResponse> getImages(@PathVariable Long tmdbId) {
        return ResponseEntity.ok(tmdbService.getMovieImages(tmdbId));
    }
}

