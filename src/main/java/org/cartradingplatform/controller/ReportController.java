package org.cartradingplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.requests.ReportRequestDTO;
import org.cartradingplatform.model.dto.response.ReportResponseDTO;
import org.cartradingplatform.model.mapper.ReportMapper;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.ReportService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    // Buyer/Seller gửi báo cáo
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ReportResponseDTO>> createReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ReportRequestDTO request,
            HttpServletRequest httpServletRequest) {

        ReportResponseDTO result = reportService.createReport(userDetails.getUser().getId(), request);

        ApiResponse<ReportResponseDTO> response = new ApiResponse<>(
                "Báo cáo đã được gửi thành công",
                HttpStatus.CREATED.value(),
                result,
                httpServletRequest.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Xem danh sách báo cáo mình đã gửi
    @GetMapping("/my-reports")
    public ResponseEntity<ApiResponse<List<ReportResponseDTO>>> getReportsByReporter(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest httpRequest) {

        Long reporterId = userDetails.getUser().getId();
        List<ReportResponseDTO> reports = reportService.getReportsByReporter(reporterId);

        ApiResponse<List<ReportResponseDTO>> response = new ApiResponse<>(
                "Danh sách báo cáo bạn đã gửi",
                HttpStatus.OK.value(),
                reports,
                httpRequest.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }

    // Xem danh sách báo cáo về mình (nếu là seller bị báo)
    @GetMapping("/about-me")
    public ResponseEntity<ApiResponse<List<ReportResponseDTO>>> getReportsByReportedUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest httpRequest) {

        Long reportedUserId = userDetails.getUser().getId();
        List<ReportResponseDTO> reports = reportService.getReportsByReportedUser(reportedUserId);

        ApiResponse<List<ReportResponseDTO>> response = new ApiResponse<>(
                "Danh sách báo cáo về bạn",
                HttpStatus.OK.value(),
                reports,
                httpRequest.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }
}
