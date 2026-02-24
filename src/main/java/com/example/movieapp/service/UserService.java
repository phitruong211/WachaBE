package com.example.movieapp.service;

import com.example.movieapp.dto.request.UserUpdateRequest;
import com.example.movieapp.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse getUserById(Long id);
    UserResponse getUserByUsername(String username);
    UserResponse getUserByEmail(String email);
    List<UserResponse> getAllUsers();
    UserResponse updateUser(Long id, UserUpdateRequest request);
    void deleteUser(Long id);
    void updateLastLogin(Long id);
}

