package org.cartradingplatform.model.mapper;

import org.cartradingplatform.model.dto.PostDTO;
import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.enums.PostStatus;
import org.cartradingplatform.model.enums.SellerType;

public class PostMapper {
    public static PostDTO toDTO(PostsEntity entity) {
        if (entity == null) return null;
        return PostDTO.builder()
                .postId(entity.getPostId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .location(entity.getLocation())
                .phoneContact(entity.getPhoneContact())
                .sellerType(entity.getSellerType().toString())
                .images(entity.getImages())
                .status(entity.getStatus().name())
                .carDetailDTO(CarDetailMapper.toDTO(entity.getCarDetail()))
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static PostsEntity toEntity(PostDTO dto, UsersEntity seller) {
        if (dto == null) return null;
        return PostsEntity.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .status(PostStatus.DRAFT)
                .location(dto.getLocation())
                .phoneContact(dto.getPhoneContact())
                .sellerType(SellerType.valueOf(dto.getSellerType()))
                .images(dto.getImages())
                .seller(seller)
                .carDetail(CarDetailMapper.toEntity(dto.getCarDetailDTO()))
                .isDeleted(false)
                .build();
    }
}
