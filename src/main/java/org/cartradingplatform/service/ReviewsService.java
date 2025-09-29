package org.cartradingplatform.service;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.PageResponse;
import org.cartradingplatform.model.dto.requests.ReviewsRequestDTO;
import org.cartradingplatform.model.dto.response.ReviewResponseDTO;
import org.cartradingplatform.repository.ReviewsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

public interface ReviewsService {
    ReviewResponseDTO createReview(Long reviewerId, ReviewsRequestDTO dto);
    PageResponse<ReviewResponseDTO> getReviewsByReviewedId(Long reviewedId, Pageable pageable);
    ReviewResponseDTO updateReview(Long reviewId, Long reviewerId, ReviewsRequestDTO dto);
    void deleteReview(Long reviewId, Long reviewerId);
}
