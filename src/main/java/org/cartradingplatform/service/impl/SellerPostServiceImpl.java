package org.cartradingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.CarDetailDTO;
import org.cartradingplatform.model.dto.PostDTO;
import org.cartradingplatform.model.dto.PostPaymentDTO;
import org.cartradingplatform.model.entity.CarDetailEntity;
import org.cartradingplatform.model.entity.PostPaymentsEntity;
import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.enums.PaymentMethod;
import org.cartradingplatform.model.enums.PaymentStatus;
import org.cartradingplatform.model.enums.PostStatus;
import org.cartradingplatform.model.mapper.CarDetailMapper;
import org.cartradingplatform.model.mapper.PostMapper;
import org.cartradingplatform.model.mapper.PostPaymentMapper;
import org.cartradingplatform.repository.PostPaymentsRepository;
import org.cartradingplatform.repository.PostsRepository;
import org.cartradingplatform.repository.UserRepository;
import org.cartradingplatform.service.SellerPostService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SellerPostServiceImpl implements SellerPostService {

    private final UserRepository userRepository;
    private final PostsRepository postsRepository;
    private final PostPaymentsRepository paymentsRepository;

    // Tạo bài đăng
    @Override
    public PostDTO createPost(Long sellerId, PostDTO dto) {
        UsersEntity seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        PostsEntity post = PostMapper.toEntity(dto, seller);
        return PostMapper.toDTO(postsRepository.save(post));
    }

    // Lấy danh sách bài đăng của seller
    @Override
    public List<PostDTO> getMyPosts(Long sellerId) {
        return postsRepository.findBySellerId(sellerId)
                .stream().map(PostMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO getPostById(Long sellerId, Long postId) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized to view this post");
        }

        return PostMapper.toDTO(post);
    }

    @Override
    public void deletePost(Long sellerId, Long postId) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized to delete this post");
        }

        postsRepository.delete(post);
    }

    // Cập nhật bài đăng
    @Override
    public PostDTO updatePost(Long sellerId, Long postId, PostDTO dto) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized to update this post");
        }

        if (dto.getTitle() != null) post.setTitle(dto.getTitle());
        if (dto.getDescription() != null) post.setDescription(dto.getDescription());
        if (dto.getPrice() != null) post.setPrice(dto.getPrice());
        if (dto.getLocation() != null) post.setLocation(dto.getLocation());
        if (dto.getImages() != null) post.setImages(new ArrayList<>(dto.getImages()));

        // update car detail
        if (dto.getCarDetailDTO() != null) {
            CarDetailEntity carDetail = post.getCarDetail();
            CarDetailDTO carDTO = dto.getCarDetailDTO();

            if (carDTO.getMake() != null) carDetail.setMake(carDTO.getMake());
            if (carDTO.getModel() != null) carDetail.setModel(carDTO.getModel());
            if (carDTO.getYear() != null) carDetail.setYear(carDTO.getYear());
            if (carDTO.getMileage() != null) carDetail.setMileage(carDTO.getMileage());
            if (carDTO.getFuelType() != null) carDetail.setFuelType(carDTO.getFuelType());
            if (carDTO.getEngineCapacity() != null) carDetail.setEngineCapacity(carDTO.getEngineCapacity());
            if (carDTO.getTransmission() != null) carDetail.setTransmission(carDTO.getTransmission());
            if (carDTO.getColor() != null) carDetail.setColor(carDTO.getColor());
            if (carDTO.getCondition() != null) carDetail.setCondition(carDTO.getCondition());
        }

        return PostMapper.toDTO(postsRepository.save(post));
    }


    // Thanh toán phí đăng bài
    public PostPaymentDTO payForPost(Long postId, UsersEntity seller, PaymentMethod method) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        if (!post.getSeller().getId().equals(seller.getId())) {
            throw new RuntimeException("Not your post");
        }

        PostPaymentsEntity payment = new PostPaymentsEntity();
        payment.setPost(post);
        payment.setSeller(seller);
        payment.setAmount(BigDecimal.valueOf(100000));
        payment.setPaymentMethod(method);
        payment.setStatus(PaymentStatus.PAID); // giả định đã trả
        return PostPaymentMapper.toDTO(paymentsRepository.save(payment));
    }
}
