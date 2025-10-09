package org.cartradingplatform.model.dto.response;

import lombok.Data;
import org.cartradingplatform.model.dto.PostDTO;

@Data
public class PostAndInfoSellerDTO {
    private PostDTO post;
    private SellerInforPublicDTO sellerInfo;
}
