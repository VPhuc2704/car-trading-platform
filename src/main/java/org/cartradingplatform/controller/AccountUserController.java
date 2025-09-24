package org.cartradingplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.response.ProfileDTO;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.AccountUserService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountUserController {
    private  final AccountUserService accountUserService;

    @GetMapping("/account/me")
    public ResponseEntity<ApiResponse<ProfileDTO>> getProfileMe( @AuthenticationPrincipal CustomUserDetails currentUser,
                                                                 HttpServletRequest request) {

        Long currentUserId = currentUser.getUser().getId();
        ProfileDTO dto = accountUserService.getUserProfile(currentUserId);

        return ResponseEntity.ok(
                new ApiResponse<>("Profile retrieved successfully",
                        HttpStatus.OK.value(), dto, request.getRequestURI())
        );
    }

    @PostMapping("/account/me")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal CustomUserDetails currentUser,
                                           HttpServletRequest request){
        Long currentUserId = currentUser.getUser().getId();
        ProfileDTO updated = accountUserService.updateUserProfile(currentUserId, updateRequest);

        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Profile updated successfully",
                        HttpStatus.OK.value(),
                        updated,
                        request.getRequestURI()
                )
        );

    }



}
