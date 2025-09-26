package org.cartradingplatform.model.enums;

import lombok.Getter;

@Getter
public enum PostStatus {
    PENDING("PENDING"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    BLOCKED("BLOCKED");

    private final String status;

    PostStatus(String status) {
        this.status = status;
    }
}
