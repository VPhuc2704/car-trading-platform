package org.cartradingplatform.model.enums;

import lombok.Getter;

@Getter
public enum PostStatus {
    PENDING("PENDING"),    // chờ duyệt sau khi thanh toán
    APPROVED("APPROVED"),  // admin duyệt
    REJECTED("REJECTED"),  // admin từ chối
    BLOCKED("BLOCKED"),    // bị khóa
    DRAFT("DRAFT");        // bản nháp, seller chưa thanh toán hoặc thanh toán thất bại

    private final String status;

    PostStatus(String status) {
        this.status = status;
    }
}
