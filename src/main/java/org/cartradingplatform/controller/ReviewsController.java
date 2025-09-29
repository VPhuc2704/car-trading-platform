package org.cartradingplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.PageResponse;
import org.cartradingplatform.model.dto.requests.ReviewsRequestDTO;
import org.cartradingplatform.model.dto.response.ReviewResponseDTO;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.ReviewsService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ReviewsController {

    private final ReviewsService reviewsService;

    @PostMapping("/api/reviews")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(@RequestBody ReviewsRequestDTO dto,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails,
                                                          HttpServletRequest request) {
        Long reviewerId = userDetails.getUser().getId(); // lấy từ token

        return ResponseEntity.ok( new  ApiResponse<>(
                "Đánh giá thanh cong ",
                HttpStatus.CREATED.value(),
                reviewsService.createReview(reviewerId, dto),
                request.getRequestURI())
        );

    }

    @PutMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> updateReview(@PathVariable Long reviewId,
                                                                       @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                       @RequestBody ReviewsRequestDTO dto,
                                                                       HttpServletRequest request) {
        return ResponseEntity.ok( new  ApiResponse<>(
                "Cập nhật thành công ",
                HttpStatus.OK.value(),
                reviewsService.updateReview(reviewId, userDetails.getUser().getId(), dto),
                request.getRequestURI())
        );
    }

    @DeleteMapping("/api/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<String>> deleteReview( @AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @PathVariable Long reviewId,
                                                             HttpServletRequest request) { // lấy từ token trong thực tế
        reviewsService.deleteReview(reviewId, userDetails.getUser().getId());

        return ResponseEntity.ok( new  ApiResponse<>(
                "Xóa đánh giá thành công",
                HttpStatus.NO_CONTENT.value(),
                null,
                request.getRequestURI())
        );
    }

    @GetMapping("/api/reviews/seller/{sellerId}")
    public ResponseEntity<ApiResponse<PageResponse<ReviewResponseDTO>>> getSellerReviews(@PathVariable Long sellerId,
                                                                                         @RequestParam(defaultValue = "0") int page,
                                                                                         @RequestParam(defaultValue = "10") int size,
                                                                                         HttpServletRequest request) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return ResponseEntity.ok(new ApiResponse<>(
                "Danh sách đánh giá",
                HttpStatus.OK.value(),
                reviewsService.getReviewsByReviewedId(sellerId, pageable),
                request.getRequestURI())
        );
    }

}
