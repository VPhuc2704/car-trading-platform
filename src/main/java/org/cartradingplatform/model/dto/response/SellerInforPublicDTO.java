package org.cartradingplatform.model.dto.response;

import lombok.Data;

@Data
public class SellerInforPublicDTO {
    private Long sellerId;
    private String sellerName;
    private String sellerEmail;
    private String sellerPhone;
}
