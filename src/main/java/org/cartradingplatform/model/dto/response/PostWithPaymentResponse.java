package org.cartradingplatform.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.cartradingplatform.model.dto.PostDTO;

@Data
@AllArgsConstructor
public class PostWithPaymentResponse {
    private PostDTO post;
    private String vnpayUrl;
}
