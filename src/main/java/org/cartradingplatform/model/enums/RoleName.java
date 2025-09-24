package org.cartradingplatform.model.enums;

import lombok.Getter;

@Getter
public enum RoleName {
    ADMIN("ADMIN"),
    BUYER("BUYER"),
    SELLER("SELLER");
    private final String nameCode;

    RoleName(String nameCode) {
        this.nameCode = nameCode;
    }
}
