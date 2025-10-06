package org.cartradingplatform.service;

import org.cartradingplatform.model.dto.FavoriteDTO;
import org.cartradingplatform.model.dto.PostDTO;
import org.cartradingplatform.model.entity.FavoriteEntity;
import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.utils.ApiResponse;

import java.util.List;

public interface FavoriteService {
    ApiResponse<FavoriteDTO> addFavorite(Long userId, Long postId);
    ApiResponse<Void> removeFavorite(Long userId, Long postId);
    ApiResponse<Boolean> isFavorite(Long userId, Long postId);
    ApiResponse<List<FavoriteDTO>> getFavoritesByUser(Long userId);
}
