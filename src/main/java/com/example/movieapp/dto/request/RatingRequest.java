package com.example.movieapp.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class RatingRequest {
    @NotNull(message = "TMDB ID is required")
    private Long tmdbId;

    @NotNull(message = "Rating value is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 10, message = "Rating must be at most 10")
    private Integer ratingValue;

    @Size(max = 2000, message = "Review text must be at most 2000 characters")
    private String reviewText;
}

