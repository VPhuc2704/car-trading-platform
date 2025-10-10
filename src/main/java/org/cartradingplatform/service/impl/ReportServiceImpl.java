package org.cartradingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.exceptions.ReportException;
import org.cartradingplatform.exceptions.UsersException;
import org.cartradingplatform.model.dto.requests.ReportRequestDTO;
import org.cartradingplatform.model.dto.response.ReportResponseDTO;
import org.cartradingplatform.model.entity.ReportEntity;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.enums.ReportStatus;
import org.cartradingplatform.model.mapper.ReportMapper;
import org.cartradingplatform.repository.ReportRepository;
import org.cartradingplatform.repository.UserRepository;
import org.cartradingplatform.service.ReportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    @Override
    public ReportResponseDTO createReport(Long reporterId, ReportRequestDTO dto) {

        // Lấy người báo cáo từ token (đã truyền từ controller)
        UsersEntity reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new ReportException("Không tìm thấy người báo cáo"));

        // Người bị báo cáo lấy từ DTO
//        UsersEntity reportedUser = userRepository.findById(dto.getReportedUserId())
//                .orElseThrow(() -> new ReportException("Không tìm thấy người bị báo cáo"));


        UsersEntity reportedUser = null;

        if (dto.getReportedUserId() != null) {
            reportedUser = userRepository.findById(dto.getReportedUserId())
                    .orElseThrow(() -> new ReportException("Không tìm thấy người bị báo cáo theo ID"));
        }
        else if (dto.getReportedUserEmail() != null && !dto.getReportedUserEmail().isBlank()) {
            reportedUser = userRepository.findByEmail(dto.getReportedUserEmail())
                    .orElseThrow(() -> new ReportException("Không tìm thấy người bị báo cáo theo email"));
        }
        else if (dto.getReportedUserphone() != null && !dto.getReportedUserphone().isBlank()) {
            reportedUser = userRepository.findByNumberPhone(dto.getReportedUserphone())
                    .orElseThrow(() -> new ReportException("Không tìm thấy người bị báo cáo theo số điện thoại"));
        }
        else {
            throw new ReportException("Thiếu thông tin để xác định người bị báo cáo (cần ID, email hoặc số điện thoại)");
        }

        ReportEntity report = ReportEntity.builder()
                .reporter(reporter)
                .reportedUser(reportedUser)
                .reason(dto.getReason())
                .description(dto.getDescription())
                .status(ReportStatus.PENDING)
                .build();

        ReportEntity saved = reportRepository.save(report);

        return ReportMapper.toDTO(saved);
    }


    @Override
    public List<ReportResponseDTO> getReportsByReporter(Long reporterId) {
        UsersEntity reporter = new UsersEntity();
        reporter.setId(reporterId);
        return reportRepository.findByReporter(reporter)
                .stream()
                .map(ReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponseDTO> getReportsByReportedUser(Long reportedUserId) {
        UsersEntity reported = new UsersEntity();
        reported.setId(reportedUserId);
        return reportRepository.findByReportedUser(reported)
                .stream()
                .map(ReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportResponseDTO> getAllReports() {
        return reportRepository.findAll()
                .stream()
                .map(ReportMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReportResponseDTO  handleReport(Long reportId, Long adminId, ReportStatus status) {
        ReportEntity report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ReportException("Không tìm thấy báo cáo"));

        UsersEntity admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UsersException("Không tìm thấy admin"));

        UsersEntity reportedUser = report.getReportedUser();

        if (report.getStatus() != ReportStatus.PENDING) {
            throw new ReportException("Báo cáo này đã được xử lý trước đó!");
        }



        report.setStatus(status);
        report.setHandledBy(admin);
        report.setHandledAt(LocalDateTime.now());

        switch (status) {
            case SUSPENDED -> {
                // Tạm khóa tài khoản 7 ngày
                reportedUser.setSuspended(true);
                reportedUser.setSuspensionEnd(LocalDateTime.now().plusDays(7));
                userRepository.save(reportedUser);
            }
            case BANNED -> {
                // Cấm vĩnh viễn
                reportedUser.setActive(false);
                reportedUser.setSuspended(true);
                reportedUser.setSuspensionEnd(null);
                userRepository.save(reportedUser);
            }
            case REJECTED -> {

            }
        }

        ReportEntity updated = reportRepository.save(report);
        return ReportMapper.toDTO(updated);
    }
}
