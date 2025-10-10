package org.cartradingplatform.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.response.ReportResponseDTO;
import org.cartradingplatform.model.enums.ReportStatus;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.ReportService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {
    private final ReportService reportService;


    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ReportResponseDTO>>> getAllReports(HttpServletRequest request) {
        List<ReportResponseDTO> reports = reportService.getAllReports();

        ApiResponse<List<ReportResponseDTO>> response = new ApiResponse<>(
                "Danh sách báo cáo người dùng",
                HttpStatus.OK.value(),
                reports,
                request.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }

    // Admin xử lý báo cáo
    @PutMapping("/{id}/handle")
    public ResponseEntity<ApiResponse<ReportResponseDTO>> handleReport(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam ReportStatus status,
            HttpServletRequest request
    ) {
        Long adminId = userDetails.getUser().getId();
        ReportResponseDTO result = reportService.handleReport(id, adminId, status);

        ApiResponse<ReportResponseDTO> response = new ApiResponse<>(
                "Xử lý báo cáo thành công",
                HttpStatus.OK.value(),
                result,
                request.getRequestURI()
        );

        return ResponseEntity.ok(response);
    }

}
