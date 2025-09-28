package org.cartradingplatform.repository;

import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.enums.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostsRepository extends JpaRepository<PostsEntity, Long> {
    Page<PostsEntity> findByIsDeletedFalseAndStatus(PostStatus status, Pageable pageable);
    List<PostsEntity> findBySellerIdAndIsDeletedFalse(Long sellerId);
    Page<PostsEntity> findByStatusAndIsDeletedFalse(PostStatus status, Pageable pageable);
    Page<PostsEntity> findByIsDeletedFalseAndStatusNotIn(List<PostStatus> statuses, Pageable pageable);
}

