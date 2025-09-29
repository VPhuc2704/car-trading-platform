package org.cartradingplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.service.VNPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vnpayment")
@RequiredArgsConstructor
public class VNPayCallbackController {
    private final VNPayService vnPayService;

    @GetMapping("/return")
    public ResponseEntity<String> vnpayReturn(HttpServletRequest request) {
        return vnPayService.handlePaymentReturn(request);
    }
}
