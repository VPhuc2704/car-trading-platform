package org.cartradingplatform.repository;

import org.cartradingplatform.model.entity.PostsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<PostsEntity, Long> {
    List<PostsEntity> findBySellerId(Long sellerId);
}

