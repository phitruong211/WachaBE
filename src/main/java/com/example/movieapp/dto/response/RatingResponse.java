package com.example.movieapp.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class RatingResponse {
    private Long id;
    private Long userId;
    private String username;
    private Long tmdbId;
    private Integer ratingValue;
    private String reviewText;
    private LocalDateTime ratedAt;
    private LocalDateTime updatedAt;
}

