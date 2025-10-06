package org.cartradingplatform.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.config.VNPayConfig;
import org.cartradingplatform.exceptions.ReportException;
import org.cartradingplatform.exceptions.UsersException;
import org.cartradingplatform.model.entity.PostPaymentsEntity;
import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.enums.PaymentStatus;
import org.cartradingplatform.model.enums.PostStatus;
import org.cartradingplatform.repository.PostPaymentsRepository;
import org.cartradingplatform.repository.PostsRepository;
import org.cartradingplatform.service.VNPayService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VNPayServiceImpl implements VNPayService {
    private final VNPayConfig vnPayConfig;
    private final PostPaymentsRepository postPaymentsRepository;
    private final PostsRepository postsRepository;

    @Override
    public String createPayment(String totalAmount, Long paymentId) throws UnsupportedEncodingException {

        PostPaymentsEntity payment = postPaymentsRepository.findById(paymentId)
                .orElseThrow(() -> new UsersException("Payment not found"));

        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "other";

        long amount = 0;
        try {
            amount = Long.parseLong(totalAmount) * 100;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Số tiền không hợp lệ");
        }
//        String bankCode = "VNBANK";
//        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_TxnRef = String.valueOf(paymentId);
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");

//        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán phí đăng bài:" +  payment.getPost().getPostId());
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", vnPayConfig.vnp_IpnUrl());
//        vnp_Params.put("vnp_IpnUrl", vnPayConfig.vnp_IpnUrl());
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

//        vnp_Params.put("vnp_IpnUrl", VNPayConfig.vnp_IpnUrl);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                hashData.append(fieldName).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append('&');
                hashData.append('&');
            }
        }

        if (query.length() > 0) query.setLength(query.length() - 1);
        if (hashData.length() > 0) hashData.setLength(hashData.length() - 1);

        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        return VNPayConfig.vnp_PayUrl + "?" + query;
    }

    @Override
    public ResponseEntity<String> handlePaymentReturn(HttpServletRequest request) {
        String responseCode = request.getParameter("vnp_ResponseCode");
        String paymentIdStr = request.getParameter("vnp_TxnRef");

        Long paymentId;
        try {
            paymentId = Long.parseLong(paymentIdStr);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Mã payment không hợp lệ.");
        }

        PostPaymentsEntity payment = postPaymentsRepository.findById(paymentId)
                .orElseThrow(() -> new ReportException("Payment not found"));

        PostsEntity post = payment.getPost();

        if ("00".equals(responseCode)) {
            // Thanh toán thành công
            payment.setStatus(PaymentStatus.PAID);
            post.setStatus(PostStatus.PENDING); // rõ nghĩa hơn
        } else {
            // Thanh toán thất bại
            payment.setStatus(PaymentStatus.FAILED);
            post.setStatus(PostStatus.DRAFT); // để seller sửa và thanh toán lại
        }

        postPaymentsRepository.save(payment);
        postsRepository.save(post); // Lưu lại trạng thái bài viết

        String redirectUrl =  vnPayConfig.vnp_ReturnUrl() + "/?status=" + ("00".equals(responseCode) ? "success" : "failed");

        return ResponseEntity.status(302)
                .header("Location", redirectUrl)
                .build();
    }
}
