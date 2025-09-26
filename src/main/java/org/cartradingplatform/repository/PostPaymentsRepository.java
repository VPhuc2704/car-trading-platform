package org.cartradingplatform.repository;

import org.cartradingplatform.model.entity.PostPaymentsEntity;
import org.cartradingplatform.model.entity.PostsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostPaymentsRepository extends JpaRepository<PostPaymentsEntity, Long> {
    Optional<PostPaymentsEntity> findByPostPostId(Long postId);
}
