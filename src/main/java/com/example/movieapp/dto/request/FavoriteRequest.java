package com.example.movieapp.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class FavoriteRequest {
    @NotNull(message = "TMDB ID is required")
    private Long tmdbId;
}

