package org.cartradingplatform.model.mapper;

import org.cartradingplatform.model.dto.response.ReportResponseDTO;
import org.cartradingplatform.model.entity.ReportEntity;

public class ReportMapper {
    public static ReportResponseDTO toDTO(ReportEntity entity) {
        if (entity == null) return null;

        return ReportResponseDTO.builder()
                .id(entity.getReportId())
                .reporterId(entity.getReporter() != null ? entity.getReporter().getId() : null)
                .reporterName(entity.getReporter() != null ? entity.getReporter().getFullName(): null)
                .reportedUserId(entity.getReportedUser() != null ? entity.getReportedUser().getId() : null)
                .reportedUserName(entity.getReportedUser() != null ? entity.getReportedUser().getFullName() : null)
                .reason(entity.getReason())
                .description(entity.getDescription())
                .status(entity.getStatus().name())
                .createdAt(entity.getCreatedAt())
                .handledAt(entity.getHandledAt())
                .handledBy(entity.getHandledBy() != null ? entity.getHandledBy().getId() : null)
                .handledByName(entity.getHandledBy() != null ? entity.getHandledBy().getFullName() : null)
                .build();
    }
}
