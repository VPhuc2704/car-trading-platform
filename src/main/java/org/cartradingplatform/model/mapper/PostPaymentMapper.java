package org.cartradingplatform.model.mapper;

import org.cartradingplatform.model.dto.PostPaymentDTO;
import org.cartradingplatform.model.entity.PostPaymentsEntity;

public class PostPaymentMapper {
    public static PostPaymentDTO toDTO(PostPaymentsEntity entity) {
        return PostPaymentDTO.builder()
                .paymentId(entity.getPaymentId())
                .postId(entity.getPost().getPostId())
                .sellerId(entity.getSeller().getId())
                .amount(entity.getAmount())
                .paymentMethod(entity.getPaymentMethod().name())
                .status(entity.getStatus().name())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())

                .build();
    }
}
