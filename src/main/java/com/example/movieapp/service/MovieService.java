package com.example.movieapp.service;

import com.example.movieapp.dto.response.MovieDetailResponse;
import com.example.movieapp.integration.tmdb.TmdbService;
import com.example.movieapp.integration.tmdb.dto.TmdbMovieDetail;
import com.example.movieapp.integration.tmdb.dto.TmdbMovieListResponse;
import com.example.movieapp.model.Rating;
import com.example.movieapp.model.Watching;
import com.example.movieapp.repository.FavoriteRepository;
import com.example.movieapp.repository.RatingRepository;
import com.example.movieapp.repository.WatchingRepository;
import com.example.movieapp.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final TmdbService tmdbService;
    private final FavoriteRepository favoriteRepository;
    private final RatingRepository ratingRepository;
    private final WatchingRepository watchingRepository;
    private final WatchlistRepository watchlistRepository;

    public TmdbMovieListResponse getPopular(int page, String language) {
        return tmdbService.getPopular(page, language);
    }

    public TmdbMovieListResponse getTopRated(int page, String language) {
        return tmdbService.getTopRated(page, language);
    }

    public TmdbMovieListResponse getUpcoming(int page, String language) {
        return tmdbService.getUpcoming(page, language);
    }

    public TmdbMovieListResponse searchMovies(String query, int page, String language) {
        return tmdbService.searchMovies(query, page, language);
    }

    public MovieDetailResponse getMovieDetail(Long tmdbId, Long userId, String language) {
        TmdbMovieDetail detail = tmdbService.getMovieDetail(tmdbId, language);
        List<String> trailers = tmdbService.getTrailerUrls(tmdbId, language);

        List<String> genres = detail.getGenres() != null
                ? detail.getGenres().stream().map(TmdbMovieDetail.Genre::getName).toList()
                : List.of();

        MovieDetailResponse.MovieDetailResponseBuilder builder = MovieDetailResponse.builder()
                .tmdbId(detail.getId())
                .title(detail.getTitle())
                .overview(detail.getOverview())
                .runtime(detail.getRuntime())
                .posterUrl(detail.getPosterPath())
                .backdropUrl(detail.getBackdropPath())
                .releaseDate(detail.getReleaseDate())
                .voteAverage(detail.getVoteAverage())
                .voteCount(detail.getVoteCount())
                .originalLanguage(detail.getOriginalLanguage())
                .status(detail.getStatus())
                .tagline(detail.getTagline())
                .genres(genres)
                .trailerUrls(trailers);

        if (userId != null) {
            builder.isFavorite(favoriteRepository.existsByUserIdAndTmdbId(userId, tmdbId));
            builder.isInWatchlist(watchlistRepository.existsByUserIdAndTmdbId(userId, tmdbId));

            ratingRepository.findByUserIdAndTmdbId(userId, tmdbId)
                    .map(Rating::getRatingValue)
                    .ifPresent(builder::userRating);

            watchingRepository.findByUserIdAndTmdbId(userId, tmdbId)
                    .map(w -> MovieDetailResponse.WatchingInfo.builder()
                            .progressTime(w.getProgressTime())
                            .progressPercentage(w.getProgressPercentage())
                            .status(w.getStatus().name())
                            .build())
                    .ifPresent(builder::watchingProgress);
        }

        return builder.build();
    }
}

