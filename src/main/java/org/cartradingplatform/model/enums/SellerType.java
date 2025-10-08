package org.cartradingplatform.model.enums;

import lombok.Getter;

@Getter
public enum SellerType {
    INDIVIDUAL("INDIVIDUAL"),
    AGENCY("AGENCY");

    private final String nameType;

    SellerType(String nameType) {
        this.nameType = nameType;
    }

}
