package org.cartradingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.exceptions.PostException;
import org.cartradingplatform.exceptions.UsersException;
import org.cartradingplatform.model.dto.FavoriteDTO;
import org.cartradingplatform.model.entity.FavoriteEntity;
import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.mapper.FavoriteMapper;
import org.cartradingplatform.repository.FavoriteRepository;
import org.cartradingplatform.repository.PostsRepository;
import org.cartradingplatform.repository.UserRepository;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.FavoriteService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final PostsRepository postsRepository;

    @Override
    public ApiResponse<FavoriteDTO> addFavorite(Long userId, Long postId) {
        UsersEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsersException("User not found"));
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found"));

        FavoriteEntity favorite = favoriteRepository.findByUsersAndPosts(user, post)
                .orElseGet(() -> favoriteRepository.save(
                        FavoriteEntity.builder()
                                .users(user)
                                .posts(post)
                                .build()
                ));

        return new ApiResponse<>("Added to favorites successfully", 200,
                FavoriteMapper.toDTO(favorite), "/api/favorites/add");
    }

    @Override
    public ApiResponse<Void> removeFavorite(Long userId, Long postId) {
        UsersEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsersException("User not found"));
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found"));

        favoriteRepository.findByUsersAndPosts(user, post)
                .ifPresent(favoriteRepository::delete);

        return new ApiResponse<>("Removed from favorites successfully", 200, null, "/api/favorites/remove");
    }

    @Override
    public ApiResponse<Boolean> isFavorite(Long userId, Long postId) {
        UsersEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsersException("User not found"));
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostException("Post not found"));

        boolean exists = favoriteRepository.findByUsersAndPosts(user, post).isPresent();
        return new ApiResponse<>("Checked favorite status", 200, exists, "/api/favorites/check");
    }

    @Override
    public ApiResponse<List<FavoriteDTO>> getFavoritesByUser(Long userId) {
        UsersEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UsersException("User not found"));

        List<FavoriteDTO> favorites = favoriteRepository.findByUsers(user)
                .stream()
                .map(FavoriteMapper::toDTO)
                .collect(Collectors.toList());

        return new ApiResponse<>("Fetched user favorites successfully", 200, favorites, "/api/favorites");
    }
}
