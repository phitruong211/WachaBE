package com.example.movieapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Password Encoder Configuration
 * Cấu hình BCrypt encoder với strength tùy chỉnh
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Bean cấu hình PasswordEncoder sử dụng BCrypt
     * Strength = 12 (mức độ mạnh, càng cao càng an toàn nhưng chậm hơn)
     * Salt được tự động tạo bởi BCrypt cho mỗi password
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt tự động tạo salt cho mỗi password, không cần cung cấp thủ công
        // Strength: 10-12 là phù hợp cho production
        return new BCryptPasswordEncoder(12);
    }
}

