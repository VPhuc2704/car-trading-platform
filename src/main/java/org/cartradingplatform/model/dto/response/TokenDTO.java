package org.cartradingplatform.model.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@Slf4j
public class TokenDTO {
        private String type;
        private String token;
}
