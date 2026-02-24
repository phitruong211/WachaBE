package com.example.movieapp.integration.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbImageResponse {
    private List<Image> backdrops;
    private List<Image> posters;

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Image {
        @JsonProperty("file_path")
        private String filePath;
        private Integer width;
        private Integer height;
        @JsonProperty("vote_average")
        private Double voteAverage;
    }
}

