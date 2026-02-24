package com.example.movieapp.integration.tmdb;

import com.example.movieapp.integration.tmdb.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TmdbService {

    private final TmdbClient tmdbClient;
    private final TmdbConfig config;

    public TmdbMovieListResponse getPopular(int page, String language) {
        String lang = language != null ? language : config.getDefaultLanguage();
        TmdbMovieListResponse res = tmdbClient.getPopular(page, lang);
        buildImageUrls(res.getResults());
        return res;
    }

    public TmdbMovieListResponse getTopRated(int page, String language) {
        String lang = language != null ? language : config.getDefaultLanguage();
        TmdbMovieListResponse res = tmdbClient.getTopRated(page, lang);
        buildImageUrls(res.getResults());
        return res;
    }

    public TmdbMovieListResponse getUpcoming(int page, String language) {
        String lang = language != null ? language : config.getDefaultLanguage();
        TmdbMovieListResponse res = tmdbClient.getUpcoming(page, lang);
        buildImageUrls(res.getResults());
        return res;
    }

    public TmdbMovieDetail getMovieDetail(Long tmdbId, String language) {
        String lang = language != null ? language : config.getDefaultLanguage();
        TmdbMovieDetail detail = tmdbClient.getMovieDetail(tmdbId, lang);
        detail.setPosterPath(buildFullUrl(detail.getPosterPath(), config.getPosterSize()));
        detail.setBackdropPath(buildFullUrl(detail.getBackdropPath(), config.getBackdropSize()));
        return detail;
    }

    public List<String> getTrailerUrls(Long tmdbId, String language) {
        String lang = language != null ? language : config.getDefaultLanguage();
        TmdbVideoResponse res = tmdbClient.getMovieVideos(tmdbId, lang);
        if (res == null || res.getResults() == null) return Collections.emptyList();
        return res.getResults().stream()
                .filter(v -> "YouTube".equalsIgnoreCase(v.getSite()) && "Trailer".equalsIgnoreCase(v.getType()))
                .map(v -> "https://www.youtube.com/watch?v=" + v.getKey())
                .toList();
    }

    public TmdbImageResponse getMovieImages(Long tmdbId) {
        TmdbImageResponse res = tmdbClient.getMovieImages(tmdbId);
        if (res != null && res.getPosters() != null) {
            res.getPosters().forEach(img ->
                    img.setFilePath(buildFullUrl(img.getFilePath(), config.getPosterSize())));
        }
        if (res != null && res.getBackdrops() != null) {
            res.getBackdrops().forEach(img ->
                    img.setFilePath(buildFullUrl(img.getFilePath(), config.getBackdropSize())));
        }
        return res;
    }

    public TmdbMovieListResponse searchMovies(String query, int page, String language) {
        String lang = language != null ? language : config.getDefaultLanguage();
        TmdbMovieListResponse res = tmdbClient.searchMovies(query, page, lang);
        buildImageUrls(res.getResults());
        return res;
    }

    // --- helpers ---

    private void buildImageUrls(List<TmdbMovieSummary> movies) {
        if (movies == null) return;
        movies.forEach(m -> {
            m.setPosterPath(buildFullUrl(m.getPosterPath(), config.getPosterSize()));
            m.setBackdropPath(buildFullUrl(m.getBackdropPath(), config.getBackdropSize()));
        });
    }

    private String buildFullUrl(String filePath, String size) {
        if (filePath == null || filePath.isBlank()) return null;
        if (filePath.startsWith("http")) return filePath;
        return config.getImageBaseUrl() + size + filePath;
    }
}

