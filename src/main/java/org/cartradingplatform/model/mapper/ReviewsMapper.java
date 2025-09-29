package org.cartradingplatform.model.mapper;

import org.cartradingplatform.model.dto.response.ReviewResponseDTO;
import org.cartradingplatform.model.entity.ReviewsEntity;

public class ReviewsMapper {
    public static ReviewResponseDTO toDto(ReviewsEntity entity){
        if (entity == null) return null;
        return ReviewResponseDTO.builder()
                .id(entity.getId())
                .reviewerId(entity.getReviewer().getId())
                .reviewedId(entity.getReviewed().getId())
                .rating(entity.getRating())
                .comment(entity.getComment())
                .createdAt(entity.getCreatedAt())
                .build();
    }

}
