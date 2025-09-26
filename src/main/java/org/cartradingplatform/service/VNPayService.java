package org.cartradingplatform.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;

public interface VNPayService {
    String createPayment(String totalAmount,  Long orderId) throws UnsupportedEncodingException;
    ResponseEntity<String> handlePaymentReturn(HttpServletRequest request);
}
