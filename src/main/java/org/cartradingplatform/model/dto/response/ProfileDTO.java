package org.cartradingplatform.model.dto.response;

import lombok.Data;
import org.cartradingplatform.model.enums.RoleName;

import java.time.LocalDate;

@Data
public class ProfileDTO {
    private Long id;
    private String email;
    private String fullName;
    private String numberPhone;
    private RoleName role;
    private Boolean isActive;
}
