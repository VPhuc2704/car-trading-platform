package org.cartradingplatform.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponseDTO {
        private Long id;
        private Integer rating;
        private String comment;
        private Long reviewerId;
        private Long reviewedId;
        private LocalDateTime createdAt;
}
