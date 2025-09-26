package org.cartradingplatform.model.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PostPaymentDTO {
    private Long paymentId;
    private Long postId;
    private Long sellerId;
    private BigDecimal amount;
    private String paymentMethod;
    private String status;
    private CarDetailDTO carDetail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
