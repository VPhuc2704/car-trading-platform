package org.cartradingplatform.service;

import org.cartradingplatform.model.dto.PageResponse;
import org.cartradingplatform.model.dto.PostDTO;
import org.cartradingplatform.model.dto.response.PostAndInfoSellerDTO;
import org.cartradingplatform.model.dto.response.PostWithPaymentResponse;
import org.cartradingplatform.model.enums.PaymentMethod;
import org.cartradingplatform.model.enums.PostStatus;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.data.domain.Pageable;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public interface PostService {

    //Seller
    PostWithPaymentResponse createPost(Long sellerId, PostDTO dto) throws UnsupportedEncodingException;
    PostDTO updatePost(Long sellerId, Long postId, PostDTO dto);
    void deletePost(Long sellerId, Long postId);
    List<PostDTO> getMyPosts(Long sellerId);
    PostDTO getPostById(Long sellerId, Long postId);
    String retryPayment(Long sellerId, Long postId, PaymentMethod method) throws UnsupportedEncodingException;

    //ADMIN
    PageResponse<PostDTO> getAllPosts(PostStatus status, Pageable pageable);
    PostDTO getPostById(Long postId);
    void deletePost(Long postId);

    // Cả hai
    PostDTO updatePostStatus(Long postId, PostStatus newStatus, CustomUserDetails currentUser);

    //public
    PageResponse<PostDTO> getPublicPosts(Pageable pageable);
    PostAndInfoSellerDTO getPublicPostById(Long postId);
    ApiResponse<List<PostDTO>> searchCars(Map<String, String> params);

}
