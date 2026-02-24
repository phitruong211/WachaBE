package com.example.movieapp.integration.tmdb;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tmdb")
@Getter @Setter
public class TmdbConfig {
    private String apiKey;
    private String baseUrl = "https://api.themoviedb.org/3";
    private String imageBaseUrl = "https://image.tmdb.org/t/p/";
    private String defaultLanguage = "vi-VN";
    private String posterSize = "w500";
    private String backdropSize = "w1280";
}

