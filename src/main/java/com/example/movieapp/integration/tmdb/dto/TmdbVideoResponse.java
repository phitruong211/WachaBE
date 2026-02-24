package com.example.movieapp.integration.tmdb.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TmdbVideoResponse {
    private List<Video> results;

    @Data @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Video {
        private String key;
        private String name;
        private String site;
        private String type;
        private Boolean official;
        @JsonProperty("published_at")
        private String publishedAt;
    }
}

