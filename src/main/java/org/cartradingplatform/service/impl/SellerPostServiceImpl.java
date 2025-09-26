package org.cartradingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.CarDetailDTO;
import org.cartradingplatform.model.dto.PostDTO;
import org.cartradingplatform.model.dto.PostPaymentDTO;
import org.cartradingplatform.model.dto.response.PostWithPaymentResponse;
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
import org.cartradingplatform.service.VNPayService;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
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
    private final VNPayService vnPayService;

    // Tạo bài đăng
    @Override
    public PostWithPaymentResponse createPost(Long sellerId, PostDTO dto) throws UnsupportedEncodingException {
        UsersEntity seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        PostsEntity post = PostMapper.toEntity(dto, seller);
        post = postsRepository.save(post);

        PostPaymentsEntity payment = new PostPaymentsEntity();
        payment.setPost(post);
        payment.setSeller(seller);
        payment.setAmount(BigDecimal.valueOf(50000)); // phí đăng bài
        payment.setPaymentMethod(PaymentMethod.VNPAY); // chưa chọn
        payment.setStatus(PaymentStatus.PENDING);
        payment = paymentsRepository.save(payment);

        String vnpayUrl = vnPayService.createPayment(payment.getAmount().toPlainString(), payment.getPaymentId());

        return new PostWithPaymentResponse(PostMapper.toDTO(post), vnpayUrl);
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
                .orElseThrow(() -> new RuntimeException("Bài viết không tìm thấy"));

        if (!post.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Không được phép xem bài viết này");
        }

        return PostMapper.toDTO(post);
    }

    @Override
    public void deletePost(Long sellerId, Long postId) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Bài viết không tìm thấy"));

        if (!post.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Không được phép xóa bài đăng này");
        }

        postsRepository.delete(post);
    }

    // Cập nhật bài đăng
    @Override
    public PostDTO updatePost(Long sellerId, Long postId, PostDTO dto) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Bài viết không tìm thấy"));

        if (!post.getSeller().getId().equals(sellerId)) {
            throw new RuntimeException("Không được phép cập nhật bài viết này");
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
}
