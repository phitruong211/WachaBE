package com.example.movieapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class WatchingRequest {
    @NotNull(message = "TMDB ID is required")
    private Long tmdbId;

    @Min(value = 0, message = "Progress time must be at least 0")
    private Integer progressTime;

    @Min(value = 0) @Max(value = 100)
    private Double progressPercentage;

    private String status; // WATCHING, PAUSED, COMPLETED
}

