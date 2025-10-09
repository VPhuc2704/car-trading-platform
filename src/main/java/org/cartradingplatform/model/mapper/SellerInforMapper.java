package org.cartradingplatform.model.mapper;

import org.cartradingplatform.model.dto.response.SellerInforPublicDTO;
import org.cartradingplatform.model.entity.UsersEntity;

public class SellerInforMapper {
    public static SellerInforPublicDTO toPublicDTO(UsersEntity user) {
        if (user == null) return null;

        SellerInforPublicDTO dto = new SellerInforPublicDTO();
        dto.setSellerId(user.getId());
        dto.setSellerName(user.getFullName());
        dto.setSellerEmail(user.getEmail());
        dto.setSellerPhone(user.getNumberPhone());
        return dto;
    }
}
