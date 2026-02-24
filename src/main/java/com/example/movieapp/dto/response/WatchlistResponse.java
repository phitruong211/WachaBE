package com.example.movieapp.dto.response;

import com.example.movieapp.model.WatchlistStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class WatchlistResponse {
    private Long id;
    private Long userId;
    private String username;
    private Long tmdbId;
    private WatchlistStatus status;
    private LocalDateTime addedAt;
}

