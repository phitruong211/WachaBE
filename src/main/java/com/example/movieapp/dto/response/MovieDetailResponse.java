package com.example.movieapp.dto.response;

import com.example.movieapp.integration.tmdb.dto.TmdbMovieDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MovieDetailResponse {
    // from TMDB
    private Long tmdbId;
    private String title;
    private String overview;
    private Integer runtime;
    private String posterUrl;
    private String backdropUrl;
    private String releaseDate;
    private Double voteAverage;
    private Integer voteCount;
    private String originalLanguage;
    private String status;
    private String tagline;
    private List<String> genres;
    private List<String> trailerUrls;

    // user interaction (null when not authenticated)
    private Boolean isFavorite;
    private Integer userRating;
    private WatchingInfo watchingProgress;
    private Boolean isInWatchlist;

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class WatchingInfo {
        private Integer progressTime;
        private Double progressPercentage;
        private String status;
    }
}

