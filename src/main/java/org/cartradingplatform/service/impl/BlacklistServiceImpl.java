package org.cartradingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.entity.BlacklistedTokens;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.repository.BlacklistedTokensRepository;
import org.cartradingplatform.service.BlacklistedTokensService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BlacklistServiceImpl implements BlacklistedTokensService {
    private final BlacklistedTokensRepository blacklistedTokensRepository;

    @Override
    public void blacklistToken(String token, UsersEntity user) {
        if (!blacklistedTokensRepository.existsByToken(token)) {
            BlacklistedTokens blacklisted = new BlacklistedTokens();
            blacklisted.setToken(token);
            blacklisted.setUser(user);
            blacklisted.setRevoked(true);
            blacklistedTokensRepository.save(blacklisted);
        }
    }

    @Override
    public boolean isBlacklisted(String token) {
        return blacklistedTokensRepository.existsByToken(token);
    }
}
