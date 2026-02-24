package com.example.movieapp.integration.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbMovieDetail {
    private Long id;
    private String title;
    private String overview;
    private Integer runtime;
    private Double popularity;
    @JsonProperty("poster_path")
    private String posterPath;
    @JsonProperty("backdrop_path")
    private String backdropPath;
    @JsonProperty("release_date")
    private String releaseDate;
    @JsonProperty("vote_average")
    private Double voteAverage;
    @JsonProperty("vote_count")
    private Integer voteCount;
    @JsonProperty("original_language")
    private String originalLanguage;
    private String status;
    private String tagline;
    private Long budget;
    private Long revenue;
    private String homepage;
    @JsonProperty("imdb_id")
    private String imdbId;
    private List<Genre> genres;
    @JsonProperty("production_companies")
    private List<ProductionCompany> productionCompanies;

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Genre {
        private Integer id;
        private String name;
    }

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductionCompany {
        private Integer id;
        private String name;
        @JsonProperty("logo_path")
        private String logoPath;
    }
}

