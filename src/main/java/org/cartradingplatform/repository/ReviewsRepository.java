package org.cartradingplatform.repository;

import org.cartradingplatform.model.entity.ReviewsEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewsRepository extends JpaRepository<ReviewsEntity, Long> {
    Page<ReviewsEntity> findAllByReviewed_Id(Long reviewedId,  Pageable pageable);
    Optional<ReviewsEntity> findByIdAndReviewer_Id(Long id, Long reviewerId);

}
