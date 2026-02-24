package com.example.movieapp.controller;

import com.example.movieapp.dto.request.WatchlistRequest;
import com.example.movieapp.dto.response.WatchlistResponse;
import com.example.movieapp.model.WatchlistStatus;
import com.example.movieapp.security.CustomUserDetails;
import com.example.movieapp.service.WatchlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Watchlist", description = "User watchlist management")
@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @Operation(summary = "Add to watchlist")
    @PostMapping
    public ResponseEntity<WatchlistResponse> add(
            @Valid @RequestBody WatchlistRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(watchlistService.addToWatchlist(user.getUser().getId(), request));
    }

    @Operation(summary = "Update watchlist status")
    @PutMapping("/{tmdbId}")
    public ResponseEntity<WatchlistResponse> updateStatus(
            @PathVariable Long tmdbId,
            @RequestParam String status,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(watchlistService.updateStatus(user.getUser().getId(), tmdbId, status));
    }

    @Operation(summary = "Remove from watchlist")
    @DeleteMapping("/{tmdbId}")
    public ResponseEntity<Void> remove(
            @PathVariable Long tmdbId,
            @AuthenticationPrincipal CustomUserDetails user) {
        watchlistService.removeFromWatchlist(user.getUser().getId(), tmdbId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "My watchlist")
    @GetMapping
    public ResponseEntity<List<WatchlistResponse>> myWatchlist(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(watchlistService.getByUserId(user.getUser().getId()));
    }

    @Operation(summary = "My watchlist by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<WatchlistResponse>> byStatus(
            @PathVariable WatchlistStatus status,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(watchlistService.getByUserIdAndStatus(user.getUser().getId(), status));
    }

    @Operation(summary = "Check if in watchlist")
    @GetMapping("/check/{tmdbId}")
    public ResponseEntity<Boolean> check(
            @PathVariable Long tmdbId,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(watchlistService.isInWatchlist(user.getUser().getId(), tmdbId));
    }
}

