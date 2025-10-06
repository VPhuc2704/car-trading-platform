package org.cartradingplatform.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteDTO {
    private Long favoriteId;
    private PostDTO post;
}
