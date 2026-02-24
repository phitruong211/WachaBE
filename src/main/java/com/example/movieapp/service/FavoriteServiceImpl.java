package com.example.movieapp.service;

import com.example.movieapp.dto.response.FavoriteResponse;
import com.example.movieapp.exception.ResourceNotFoundException;
import com.example.movieapp.model.Favorite;
import com.example.movieapp.model.User;
import com.example.movieapp.repository.FavoriteRepository;
import com.example.movieapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;

    @Override
    public FavoriteResponse addFavorite(Long userId, Long tmdbId) {
        if (favoriteRepository.existsByUserIdAndTmdbId(userId, tmdbId)) {
            throw new IllegalArgumentException("Movie already in favorites");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Favorite fav = Favorite.builder().user(user).tmdbId(tmdbId).build();
        Favorite saved = favoriteRepository.save(fav);
        return toResponse(saved);
    }

    @Override
    public void removeFavorite(Long userId, Long tmdbId) {
        Favorite fav = favoriteRepository.findByUserIdAndTmdbId(userId, tmdbId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
        favoriteRepository.delete(fav);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FavoriteResponse> getFavoritesByUserId(Long userId) {
        return favoriteRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFavorite(Long userId, Long tmdbId) {
        return favoriteRepository.existsByUserIdAndTmdbId(userId, tmdbId);
    }

    private FavoriteResponse toResponse(Favorite f) {
        return FavoriteResponse.builder()
                .id(f.getId())
                .userId(f.getUser().getId())
                .username(f.getUser().getUsername())
                .tmdbId(f.getTmdbId())
                .createdAt(f.getCreatedAt())
                .build();
    }
}

