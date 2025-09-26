package org.cartradingplatform.service;

import org.cartradingplatform.model.dto.PostDTO;
import org.cartradingplatform.model.dto.PostPaymentDTO;
import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.enums.PaymentMethod;

import java.util.List;

public interface SellerPostService {
    PostDTO createPost(Long sellerId, PostDTO dto);
    PostDTO updatePost(Long sellerId, Long postId, PostDTO dto);
    void deletePost(Long sellerId, Long postId);
    List<PostDTO> getMyPosts(Long sellerId);
    PostDTO getPostById(Long sellerId, Long postId);
    PostPaymentDTO payForPost(Long postId, UsersEntity seller, PaymentMethod method);
}
