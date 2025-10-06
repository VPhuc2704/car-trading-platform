package org.cartradingplatform.service;

import org.cartradingplatform.model.dto.requests.ReportRequestDTO;
import org.cartradingplatform.model.dto.response.ReportResponseDTO;
import org.cartradingplatform.model.entity.ReportEntity;
import org.cartradingplatform.model.enums.ReportStatus;

import java.util.List;

public interface ReportService {
    ReportResponseDTO createReport(Long reporterId, ReportRequestDTO dto);
    List<ReportResponseDTO> getReportsByReporter(Long reporterId);
    List<ReportResponseDTO> getReportsByReportedUser(Long reportedUserId);
    List<ReportResponseDTO> getAllPendingReports();
    ReportResponseDTO  handleReport(Long reportId, Long adminId, ReportStatus status);
}
