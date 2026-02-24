package com.example.movieapp.service;

import com.example.movieapp.dto.request.ChangePasswordRequest;
import com.example.movieapp.dto.request.LoginRequest;
import com.example.movieapp.dto.request.RegisterRequest;
import com.example.movieapp.dto.response.AuthResponse;

/**
 * Authentication Service Interface
 * Xử lý các nghiệp vụ xác thực người dùng
 */
public interface AuthService {

    /**
     * Đăng ký tài khoản mới
     * - Kiểm tra username và email đã tồn tại
     * - Mã hóa mật khẩu bằng BCrypt
     * - Tạo JWT tokens
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Đăng nhập
     * - Xác thực username/password
     * - Tạo JWT tokens mới
     */
    AuthResponse login(LoginRequest request);

    /**
     * Làm mới access token bằng refresh token
     */
    AuthResponse refreshToken(String refreshToken);

    /**
     * Đổi mật khẩu
     * - Kiểm tra mật khẩu cũ
     * - Mã hóa mật khẩu mới
     */
    void changePassword(Long userId, ChangePasswordRequest request);

    /**
     * Đăng xuất (invalidate refresh token)
     */
    void logout(String refreshToken);

    /**
     * Xác thực password có đúng với user không
     */
    boolean validatePassword(String rawPassword, String encodedPassword);
}

