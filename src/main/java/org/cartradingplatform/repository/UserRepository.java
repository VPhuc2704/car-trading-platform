package org.cartradingplatform.repository;

import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.enums.RoleName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<UsersEntity,Long> {
    Optional<UsersEntity> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT u FROM UsersEntity u " +
            "WHERE (:email IS NULL OR u.email LIKE %:email%) " +
            "AND (:isActive IS NULL OR u.isActive = :isActive) " +
            "AND ( " +
            "    (:roleName IS NULL AND u.roleName IN (org.cartradingplatform.model.enums.RoleName.BUYER, org.cartradingplatform.model.enums.RoleName.SELLER)) " +
            "    OR (:roleName IS NOT NULL AND u.roleName = :roleName) " +
            ")")
    Page<UsersEntity> findAllWithFilters(@Param("email") String email,
                                         @Param("roleName") RoleName roleName,
                                         @Param("isActive") Boolean isActive,
                                         Pageable pageable);

}
