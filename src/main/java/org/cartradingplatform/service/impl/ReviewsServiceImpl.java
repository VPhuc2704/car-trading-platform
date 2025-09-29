package org.cartradingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.PageResponse;
import org.cartradingplatform.model.dto.requests.ReviewsRequestDTO;
import org.cartradingplatform.model.dto.response.ReviewResponseDTO;
import org.cartradingplatform.model.entity.ReviewsEntity;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.mapper.ReviewsMapper;
import org.cartradingplatform.repository.ReviewsRepository;
import org.cartradingplatform.repository.UserRepository;
import org.cartradingplatform.service.ReviewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewsServiceImpl implements ReviewsService {
    private final ReviewsRepository reviewsRepository;
    private final UserRepository userRepository;

    @Override
    public ReviewResponseDTO createReview(Long reviewerId, ReviewsRequestDTO dto) {
        UsersEntity reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found"));
        UsersEntity reviewed = userRepository.findById(dto.getReviewedId())
                .orElseThrow(() -> new RuntimeException("Reviewed user not found"));

        ReviewsEntity review = ReviewsEntity.builder()
                .rating(dto.getRating())
                .comment(dto.getComment())
                .reviewer(reviewer)
                .reviewed(reviewed)
                .build();

        reviewsRepository.save(review);

        return ReviewsMapper.toDto(review);
    }

    @Override
    public PageResponse<ReviewResponseDTO> getReviewsByReviewedId(Long reviewedId, Pageable pageable) {

        Page<ReviewsEntity> reviewPage = reviewsRepository.findAllByReviewed_Id(reviewedId, pageable);

        List<ReviewResponseDTO> listReviews = reviewPage.getContent()
                .stream()
                .map(ReviewsMapper::toDto)
                .collect(Collectors.toList());

        return new PageResponse<>(
                listReviews,
                reviewPage.getNumber(),
                reviewPage.getSize(),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewPage.isFirst(),
                reviewPage.isLast()
        );
    }

    @Override
    @Transactional
    public ReviewResponseDTO updateReview(Long reviewId, Long reviewerId, ReviewsRequestDTO dto) {

        ReviewsEntity review = reviewsRepository.findByIdAndReviewer_Id(reviewId, reviewerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá hoặc bạn không có quyền sửa"));

        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        ReviewsEntity updated = reviewsRepository.save(review);

        return ReviewsMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long reviewerId) {
        ReviewsEntity review = reviewsRepository.findByIdAndReviewer_Id(reviewId, reviewerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đánh giá hoặc bạn không có quyền xóa"));

        reviewsRepository.delete(review);
    }

}
