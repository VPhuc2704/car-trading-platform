package org.cartradingplatform.model.mapper;

import org.cartradingplatform.model.dto.FavoriteDTO;
import org.cartradingplatform.model.entity.FavoriteEntity;

public class FavoriteMapper {
    public static FavoriteDTO toDTO(FavoriteEntity entity) {
        if (entity == null) return null;
        return FavoriteDTO.builder()
                .favoriteId(entity.getId())
                .post(PostMapper.toDTO(entity.getPosts()))
                .build();
    }
}
