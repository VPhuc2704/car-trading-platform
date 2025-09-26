package org.cartradingplatform.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.entity.PostPaymentsEntity;
import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.enums.PaymentStatus;
import org.cartradingplatform.model.enums.PostStatus;
import org.cartradingplatform.repository.PostPaymentsRepository;
import org.cartradingplatform.repository.PostsRepository;
import org.cartradingplatform.service.VNPayService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

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
