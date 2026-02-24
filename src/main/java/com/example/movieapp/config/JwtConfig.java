package com.example.movieapp.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * JWT Configuration Properties
 * Chứa các thông tin cấu hình JWT như secret key, salt, expiration time
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfig {

    /**
     * Secret key dùng để ký JWT token
     */
    private String secret;

    /**
     * Salt dùng để tăng cường bảo mật cho secret key
     */
    private String salt;

    /**
     * Thời gian hết hạn của access token (milliseconds)
     */
    private Long expiration;

    /**
     * Thời gian hết hạn của refresh token (milliseconds)
     */
    private Long refreshExpiration;

    /**
     * Issuer của token
     */
    private String issuer = "wacha-cinema";
}

