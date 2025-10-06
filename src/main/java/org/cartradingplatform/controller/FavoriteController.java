package org.cartradingplatform.controller;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.FavoriteDTO;
import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.mapper.FavoriteMapper;
import org.cartradingplatform.repository.PostsRepository;
import org.cartradingplatform.repository.UserRepository;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.FavoriteService;
import org.cartradingplatform.service.PostService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    @PostMapping("/add/{postId}")
    public ApiResponse<FavoriteDTO> addFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {
        return favoriteService.addFavorite(userDetails.getUser().getId(), postId);
    }

    @DeleteMapping("/remove/{postId}")
    public ApiResponse<Void> removeFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {
        return favoriteService.removeFavorite(userDetails.getUser().getId(), postId);
    }

    @GetMapping
    public ApiResponse<List<FavoriteDTO>> getFavorites(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return favoriteService.getFavoritesByUser(userDetails.getUser().getId());
    }

    @GetMapping("/check/{postId}")
    public ApiResponse<Boolean> checkFavorite(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long postId) {
        return favoriteService.isFavorite(userDetails.getUser().getId(), postId);
    }
}
