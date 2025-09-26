package org.cartradingplatform.model.dto.requests;

import lombok.Data;

@Data
public class ResetPassword {
    private String password;
    private String newPassword;
}
