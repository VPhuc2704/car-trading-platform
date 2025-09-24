package org.cartradingplatform.repository;

import org.cartradingplatform.model.entity.BlacklistedTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlacklistedTokensRepository extends JpaRepository<BlacklistedTokens,Long> {
    Optional<BlacklistedTokens> findByToken(String token);
    boolean existsByToken(String token);
}
