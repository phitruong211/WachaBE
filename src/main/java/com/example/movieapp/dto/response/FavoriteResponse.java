package com.example.movieapp.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class FavoriteResponse {
    private Long id;
    private Long userId;
    private String username;
    private Long tmdbId;
    private LocalDateTime createdAt;
}

