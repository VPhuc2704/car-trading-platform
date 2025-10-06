package org.cartradingplatform.repository;

import org.cartradingplatform.model.entity.FavoriteEntity;
import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {
    Optional<FavoriteEntity> findByUsersAndPosts(UsersEntity user, PostsEntity post);
    List<FavoriteEntity> findByUsers(UsersEntity user);
}
