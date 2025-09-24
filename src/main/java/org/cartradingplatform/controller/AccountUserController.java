package org.cartradingplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.PageResponse;
import org.cartradingplatform.model.dto.requests.UpdateUserRequestDTO;
import org.cartradingplatform.model.dto.response.ProfileDTO;
import org.cartradingplatform.model.dto.response.UserInfoDTO;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.AccountUserService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/account/{id}")
    public ResponseEntity<ApiResponse<ProfileDTO>> updateUserProfile(
            @PathVariable Long id,
            @RequestBody UpdateUserRequestDTO dto,
            @AuthenticationPrincipal CustomUserDetails currentUser,
            HttpServletRequest request) {

        ProfileDTO updated = accountUserService.updateUserProfile(id, dto, currentUser);

        return ResponseEntity.ok(
                new ApiResponse<>("Cập nhật thành công",
                        HttpStatus.OK.value(), updated, request.getRequestURI())
        );
    }


    @GetMapping("admin/users")
    public ResponseEntity<ApiResponse<PageResponse<ProfileDTO>>> getAllUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) Boolean isActive,
            Pageable pageable,
            HttpServletRequest request) {

        PageResponse<ProfileDTO> result = accountUserService.getAllUsers(email, roleName, isActive, pageable);

        return ResponseEntity.ok(
                new ApiResponse<>("Lấy danh sách user thành công",
                        HttpStatus.OK.value(), result, request.getRequestURI())
        );
    }

    @GetMapping("/admin/users/{id}")
    public ResponseEntity<ApiResponse<ProfileDTO>> getUserById(
            @PathVariable Long id,
            HttpServletRequest request) {

        ProfileDTO user = accountUserService.getUserProfile(id);

        return ResponseEntity.ok(
                new ApiResponse<>("Lấy chi tiết user thành công",
                        HttpStatus.OK.value(), user, request.getRequestURI())
        );
    }

    @PostMapping("/admin/users")
    public ResponseEntity<ApiResponse<ProfileDTO>> createUser(@Valid @RequestBody ProfileDTO dto,
                                                              HttpServletRequest request) {

        ProfileDTO created = accountUserService.createUser(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                new ApiResponse<>("Tạo user thành công",
                        HttpStatus.CREATED.value(), created, request.getRequestURI())
        );
    }



    @PatchMapping("/admin/users/{id}/status")
    public ResponseEntity<ApiResponse<ProfileDTO>> updateUserStatus(@PathVariable Long id,
                                                                    @RequestParam boolean active,
                                                                    HttpServletRequest request) {

        ProfileDTO updated = accountUserService.updateUserStatus(id, active);

        return ResponseEntity.ok(
                new ApiResponse<>("Cập nhật trạng thái user thành công",
                        HttpStatus.OK.value(), updated, request.getRequestURI())
        );
    }


}
