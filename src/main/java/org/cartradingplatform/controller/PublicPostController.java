package org.cartradingplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.PageResponse;
import org.cartradingplatform.model.dto.PostDTO;
import org.cartradingplatform.service.PostService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public/posts")
@RequiredArgsConstructor
public class PublicPostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PostDTO>>> getPublicPosts(@RequestParam(defaultValue = "0") int page,
                                                                             @RequestParam(defaultValue = "10") int size,
                                                                             HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Danh sách bài đăng",
                        HttpStatus.OK.value(),
                        postService.getPublicPosts(pageable),
                        request.getRequestURI()
                )
        );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse<PostDTO>> getPublicPosts(@PathVariable(name = "postId") Long postId,
                                                                             HttpServletRequest request) {
        return ResponseEntity.ok(
                new ApiResponse<>(
                        "Danh sách bài đăng",
                        HttpStatus.OK.value(),
                        postService.getPublicPostById(postId),
                        request.getRequestURI()
                )
        );
    }

    @GetMapping("/search")
    public ApiResponse<List<PostDTO>> searchCars(@RequestParam Map<String, String> params) {
        return postService.searchCars(params);
    }
}
