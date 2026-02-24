package com.example.movieapp.integration.tmdb;

import com.example.movieapp.integration.tmdb.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@Slf4j
public class TmdbClient {

    private final RestClient restClient;
    private final TmdbConfig config;

    public TmdbClient(TmdbConfig config) {
        this.config = config;
        this.restClient = RestClient.builder()
                .baseUrl(config.getBaseUrl())
                .build();
    }

    public TmdbMovieListResponse getPopular(int page, String language) {
        return restClient.get()
                .uri(uri -> uri.path("/movie/popular")
                        .queryParam("api_key", config.getApiKey())
                        .queryParam("page", page)
                        .queryParam("language", language)
                        .build())
                .retrieve()
                .body(TmdbMovieListResponse.class);
    }

    public TmdbMovieListResponse getTopRated(int page, String language) {
        return restClient.get()
                .uri(uri -> uri.path("/movie/top_rated")
                        .queryParam("api_key", config.getApiKey())
                        .queryParam("page", page)
                        .queryParam("language", language)
                        .build())
                .retrieve()
                .body(TmdbMovieListResponse.class);
    }

    public TmdbMovieListResponse getUpcoming(int page, String language) {
        return restClient.get()
                .uri(uri -> uri.path("/movie/upcoming")
                        .queryParam("api_key", config.getApiKey())
                        .queryParam("page", page)
                        .queryParam("language", language)
                        .build())
                .retrieve()
                .body(TmdbMovieListResponse.class);
    }

    public TmdbMovieDetail getMovieDetail(Long movieId, String language) {
        return restClient.get()
                .uri(uri -> uri.path("/movie/{movieId}")
                        .queryParam("api_key", config.getApiKey())
                        .queryParam("language", language)
                        .build(movieId))
                .retrieve()
                .body(TmdbMovieDetail.class);
    }

    public TmdbVideoResponse getMovieVideos(Long movieId, String language) {
        return restClient.get()
                .uri(uri -> uri.path("/movie/{movieId}/videos")
                        .queryParam("api_key", config.getApiKey())
                        .queryParam("language", language)
                        .build(movieId))
                .retrieve()
                .body(TmdbVideoResponse.class);
    }

    public TmdbImageResponse getMovieImages(Long movieId) {
        return restClient.get()
                .uri(uri -> uri.path("/movie/{movieId}/images")
                        .queryParam("api_key", config.getApiKey())
                        .build(movieId))
                .retrieve()
                .body(TmdbImageResponse.class);
    }

    public TmdbMovieListResponse searchMovies(String query, int page, String language) {
        return restClient.get()
                .uri(uri -> uri.path("/search/movie")
                        .queryParam("api_key", config.getApiKey())
                        .queryParam("query", query)
                        .queryParam("page", page)
                        .queryParam("language", language)
                        .build())
                .retrieve()
                .body(TmdbMovieListResponse.class);
    }
}

