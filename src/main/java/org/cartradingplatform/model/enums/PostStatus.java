package org.cartradingplatform.model.enums;

import lombok.Getter;

@Getter
public enum PostStatus {
    PENDING("PENDING"),    // chờ duyệt sau khi thanh toán
    APPROVED("APPROVED"),  // admin duyệt
    REJECTED("REJECTED"),  // admin từ chối
    BLOCKED("BLOCKED"),    // bị khóa
    DRAFT("DRAFT"),        // bản nháp, seller chưa thanh toán hoặc thanh toán thất bại
    HIDDEN("HIDDEN");      // seller ẩn bài

    private final String status;

    PostStatus(String status) {
        this.status = status;
    }

    public boolean canSellerEdit(){
        return this == DRAFT || this == REJECTED;
    }

    // Rule cho seller
    public boolean canSellerTransitionTo(PostStatus target) {
        switch (this) {
            case APPROVED:
                return target == HIDDEN;
            case HIDDEN:
                return target == APPROVED;
            case REJECTED:
                return target == DRAFT;
            default:
                return false;
        }
    }

    // Rule cho admin
    public boolean canAdminTransitionTo(PostStatus target) {
        switch (this) {
            case PENDING:
                return target == APPROVED || target == REJECTED;
            case APPROVED:
                return target == BLOCKED;
            default:
                return false;
        }
    }

}
