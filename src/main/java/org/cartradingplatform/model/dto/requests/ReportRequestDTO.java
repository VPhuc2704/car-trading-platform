package org.cartradingplatform.model.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ReportRequestDTO {
    private Long reportedUserId;
    @Email(message = "Email người bị báo cáo không hợp lệ")
    private String reportedUserEmail;
    @NotBlank(message = "Lý do báo cáo không được để trống")
    private String reason;
    @NotBlank(message = "Mô tả chi tiết không được để trống")
    private String description;
}