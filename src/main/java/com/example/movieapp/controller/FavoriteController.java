package com.example.movieapp.controller;

import com.example.movieapp.dto.request.FavoriteRequest;
import com.example.movieapp.dto.response.FavoriteResponse;
import com.example.movieapp.security.CustomUserDetails;
import com.example.movieapp.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Favorites", description = "User favorite movies")
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "Add favorite")
    @PostMapping
    public ResponseEntity<FavoriteResponse> add(
            @Valid @RequestBody FavoriteRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteService.addFavorite(user.getUser().getId(), request.getTmdbId()));
    }

    @Operation(summary = "Remove favorite")
    @DeleteMapping("/{tmdbId}")
    public ResponseEntity<Void> remove(
            @PathVariable Long tmdbId,
            @AuthenticationPrincipal CustomUserDetails user) {
        favoriteService.removeFavorite(user.getUser().getId(), tmdbId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "My favorites")
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> myFavorites(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(favoriteService.getFavoritesByUserId(user.getUser().getId()));
    }

    @Operation(summary = "Check favorite")
    @GetMapping("/check/{tmdbId}")
    public ResponseEntity<Boolean> check(
            @PathVariable Long tmdbId,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(favoriteService.isFavorite(user.getUser().getId(), tmdbId));
    }
}

