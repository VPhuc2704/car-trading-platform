package org.cartradingplatform.model.dto.requests;

import lombok.Data;
import org.cartradingplatform.model.enums.RoleName;

import java.time.LocalDate;

@Data
public class UpdateUserRequestDTO {
    private Long id;
    private String email;
    private String fullName;
    private String numberPhone;
    private RoleName roleName;
    private Boolean isActive;
}
