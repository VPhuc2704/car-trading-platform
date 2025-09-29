package org.cartradingplatform.model.dto.requests;

import jakarta.persistence.*;
import lombok.Data;
import org.cartradingplatform.model.entity.UsersEntity;

@Data
public class ReviewsRequestDTO {
    private Long reviewedId;
    private Integer rating;
    private String comment;
}
