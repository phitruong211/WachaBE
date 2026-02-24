package com.example.movieapp.controller;

import com.example.movieapp.dto.request.WatchingRequest;
import com.example.movieapp.dto.response.WatchingResponse;
import com.example.movieapp.model.WatchingStatus;
import com.example.movieapp.security.CustomUserDetails;
import com.example.movieapp.service.WatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Watching", description = "Watching progress tracking")
@RestController
@RequestMapping("/api/watching")
@RequiredArgsConstructor
public class WatchingController {

    private final WatchingService watchingService;

    @Operation(summary = "Start or update watching progress")
    @PostMapping
    public ResponseEntity<WatchingResponse> startOrUpdate(
            @Valid @RequestBody WatchingRequest request,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(watchingService.startOrUpdate(user.getUser().getId(), request));
    }

    @Operation(summary = "Get watching progress for a movie")
    @GetMapping("/{tmdbId}")
    public ResponseEntity<WatchingResponse> get(
            @PathVariable Long tmdbId,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(watchingService.getByUserAndMovie(user.getUser().getId(), tmdbId));
    }

    @Operation(summary = "My watching history")
    @GetMapping
    public ResponseEntity<List<WatchingResponse>> myHistory(
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(watchingService.getByUserId(user.getUser().getId()));
    }

    @Operation(summary = "My watching by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<WatchingResponse>> byStatus(
            @PathVariable WatchingStatus status,
            @AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(watchingService.getByUserIdAndStatus(user.getUser().getId(), status));
    }

    @Operation(summary = "Delete watching record")
    @DeleteMapping("/{tmdbId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long tmdbId,
            @AuthenticationPrincipal CustomUserDetails user) {
        watchingService.delete(user.getUser().getId(), tmdbId);
        return ResponseEntity.noContent().build();
    }
}

