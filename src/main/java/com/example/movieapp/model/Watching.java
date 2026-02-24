package com.example.movieapp.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "watching", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "tmdb_id"}))
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Watching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "tmdb_id", nullable = false)
    private Long tmdbId;

    private Integer progressTime;
    private Double progressPercentage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WatchingStatus status;

    private LocalDateTime startedAt;
    private LocalDateTime lastWatchedAt;
    private LocalDateTime completedAt;

    @PrePersist
    public void prePersist() {
        if (status == null) status = WatchingStatus.WATCHING;
        if (startedAt == null) startedAt = LocalDateTime.now();
    }
}

