package org.cartradingplatform.model.dto.requests;

import lombok.Data;

@Data
public class ReportRequestDTO {
    private Long reportedUserId;
    private String reason;
}