package org.cartradingplatform.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReportResponseDTO {
    private Long id;
    private Long reporterId;
    private String reporterName;
    private Long reportedUserId;
    private String reportedUserName;
    private String reason;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime handledAt;
    private Long handledBy;
    private String handledByName;
}
