package org.cartradingplatform.service;

import org.cartradingplatform.model.entity.UsersEntity;

public interface BlacklistedTokensService {
    void blacklistToken(String token, UsersEntity user);
    boolean isBlacklisted(String token);
}
