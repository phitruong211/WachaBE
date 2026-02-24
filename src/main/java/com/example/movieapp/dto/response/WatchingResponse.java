package com.example.movieapp.dto.response;

import com.example.movieapp.model.WatchingStatus;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class WatchingResponse {
    private Long id;
    private Long userId;
    private String username;
    private Long tmdbId;
    private Integer progressTime;
    private Double progressPercentage;
    private WatchingStatus status;
    private LocalDateTime startedAt;
    private LocalDateTime lastWatchedAt;
    private LocalDateTime completedAt;
}

