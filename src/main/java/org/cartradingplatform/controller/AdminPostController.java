package org.cartradingplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.PageResponse;
import org.cartradingplatform.model.dto.PostDTO;

import org.cartradingplatform.model.enums.PostStatus;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.PostService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminPostController {

    private final PostService postService;

    @GetMapping("/admin/posts")
    public ResponseEntity<ApiResponse<PageResponse<PostDTO>>> getAllPosts(
            @RequestParam(required = false) PostStatus status,
            Pageable pageable,
            HttpServletRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Danh sách bài đăng",
                HttpStatus.OK.value(),
                postService.getAllPosts(status, pageable),
                request.getRequestURI()
        ));
    }

    @GetMapping("/admin/posts/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> getPostAdminById(@PathVariable Long id,
                                                                 HttpServletRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Chi tiết bài đăng",
                HttpStatus.OK.value(),
                postService.getPostById(id),
                request.getRequestURI()
        ));
    }

    @PatchMapping("/admin/posts/{id}/status")
    public ResponseEntity<ApiResponse<PostDTO>> updatePostStatus(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                 @PathVariable Long id,
                                                                 @RequestParam PostStatus status,
                                                                 HttpServletRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(
                "Cập nhật trạng thái thành công",
                HttpStatus.OK.value(),
                postService.updatePostStatus(id, status, currentUser),
                request.getRequestURI()
        ));
    }

    @DeleteMapping("/admin/posts/{id}")
    public ResponseEntity<ApiResponse<String>> adminDeletePost(@PathVariable Long id,
                                                               HttpServletRequest request) {
        postService.deletePost(id);
        return ResponseEntity.ok(new ApiResponse<>(
                "Xóa bài đăng thành công",
                HttpStatus.NO_CONTENT.value(),
                null,
                request.getRequestURI()
        ));
    }
}
