package org.cartradingplatform.model.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {
    VNPAY("VNPAY"),
    COD("COD");

    private final String value;
    PaymentMethod(String value) {
        this.value = value;
    }
}
