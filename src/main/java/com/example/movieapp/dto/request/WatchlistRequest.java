package com.example.movieapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class WatchlistRequest {
    @NotNull(message = "TMDB ID is required")
    private Long tmdbId;

    private String status; // PLANNED, WATCHED, SKIPPED
}

