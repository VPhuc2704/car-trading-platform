package org.cartradingplatform.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.exceptions.PostException;
import org.cartradingplatform.model.dto.CarDetailDTO;
import org.cartradingplatform.model.dto.PageResponse;
import org.cartradingplatform.model.dto.PostDTO;
import org.cartradingplatform.model.dto.response.PostWithPaymentResponse;
import org.cartradingplatform.model.entity.CarDetailEntity;
import org.cartradingplatform.model.entity.PostPaymentsEntity;
import org.cartradingplatform.model.entity.PostsEntity;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.enums.*;
import org.cartradingplatform.model.mapper.PostMapper;
import org.cartradingplatform.repository.PostPaymentsRepository;
import org.cartradingplatform.repository.PostsRepository;
import org.cartradingplatform.repository.UserRepository;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.PostService;
import org.cartradingplatform.service.VNPayService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostsRepository postsRepository;
    private final PostPaymentsRepository paymentsRepository;
    private final VNPayService vnPayService;

    // Tạo bài đăng
    @Override
    public PostWithPaymentResponse createPost(Long sellerId, PostDTO dto) throws UnsupportedEncodingException {
        UsersEntity seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new PostException("Seller not found"));

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
        return postsRepository.findBySellerIdAndIsDeletedFalse(sellerId)
                .stream().map(PostMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PostDTO getPostById(Long sellerId, Long postId) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostException("Bài viết không tìm thấy"));

        if (!post.getSeller().getId().equals(sellerId)) {
            throw new PostException("Không được phép xem bài viết này");
        }

        return PostMapper.toDTO(post);
    }


    @Override
    @Transactional
    public void deletePost(Long sellerId, Long postId) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostException("Bài viết không tìm thấy"));

        if (!post.getSeller().getId().equals(sellerId)) {
            throw new PostException("Không được phép xóa bài đăng này");
        }

        PostPaymentsEntity latestPayment  = paymentsRepository.findTopByPostOrderByPaymentIdDesc(post).orElse(null);

        if (post.getStatus() == PostStatus.DRAFT && latestPayment != null && latestPayment.getStatus() == PaymentStatus.PENDING  ) {
            paymentsRepository.deleteByPost(post);
            postsRepository.delete(post);
        } else {
            // Soft delete
            post.setIsDeleted(true);
            postsRepository.save(post);
        }
    }

    // Cập nhật bài đăng
    @Override
    @Transactional
    public PostDTO updatePost(Long sellerId, Long postId, PostDTO dto)  {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostException("Bài viết không tìm thấy"));

        if (!post.getSeller().getId().equals(sellerId)) {
            throw new PostException("Không được phép cập nhật bài viết này");
        }

        if (!post.getStatus().canSellerEdit()){
            throw new PostException("Bài đăng ở trạng thái " + post.getStatus() + " không thể chỉnh sửa");
        }

        if (dto.getTitle() != null) post.setTitle(dto.getTitle());
        if (dto.getDescription() != null) post.setDescription(dto.getDescription());
        if (dto.getPrice() != null) post.setPrice(dto.getPrice());
        if (dto.getLocation() != null) post.setLocation(dto.getLocation());
        if (dto.getImages() != null) post.setImages(new ArrayList<>(dto.getImages()));
        if (dto.getPhoneContact() != null) post.setPhoneContact(dto.getPhoneContact());
        if (dto.getSellerType() != null) post.setSellerType(SellerType.valueOf(dto.getSellerType()));

        // update car detail
        if (dto.getCarDetailDTO() != null) {
            CarDetailEntity carDetail = post.getCarDetail();
            CarDetailDTO carDTO = dto.getCarDetailDTO();

            if (carDTO.getMake() != null) carDetail.setMake(carDTO.getMake());
            if (carDTO.getModel() != null) carDetail.setModel(carDTO.getModel());
            if (carDTO.getYear() != null) carDetail.setYear(carDTO.getYear());
            if (carDTO.getMileage() != null) carDetail.setMileage(carDTO.getMileage());
            if (carDTO.getFuelType() != null) carDetail.setFuelType(carDTO.getFuelType());
            if (carDTO.getTransmission() != null) carDetail.setTransmission(carDTO.getTransmission());
            if (carDTO.getColor() != null) carDetail.setColor(carDTO.getColor());
            if (carDTO.getCondition() != null) carDetail.setCondition(carDTO.getCondition());
        }

        post.setStatus(PostStatus.PENDING);

        return PostMapper.toDTO(postsRepository.save(post));
    }

    @Override
    @Transactional
    public String retryPayment(Long sellerId, Long postId, PaymentMethod method) throws UnsupportedEncodingException {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostException("Bài viết không tìm thấy"));

        if (!post.getSeller().getId().equals(sellerId)) {
            throw new PostException("Không được phép thanh toán lại cho bài viết này");
        }

        if (post.getStatus() != PostStatus.DRAFT) {
            throw new PostException("Chỉ bài viết ở trạng thái DRAFT mới được thanh toán lại");
        }

        // Tạo payment mới
        PostPaymentsEntity newPayment = new PostPaymentsEntity();
        newPayment.setPost(post);
        newPayment.setSeller(post.getSeller());
        newPayment.setAmount(BigDecimal.valueOf(50000)); // phí đăng bài
        newPayment.setPaymentMethod(method);
        newPayment.setStatus(PaymentStatus.PENDING);
        newPayment = paymentsRepository.save(newPayment);

        // Tạo link thanh toán mới
        return vnPayService.createPayment(
                newPayment.getAmount().toPlainString(),
                newPayment.getPaymentId()
        );
    }

//    ==================================================================================================================
//                                                      ADMIN

    @Override
    public PageResponse<PostDTO> getAllPosts(PostStatus status, Pageable pageable) {
        Page<PostsEntity> posts;
        if (status != null) {
            posts = postsRepository.findByStatusAndIsDeletedFalse(status, pageable);
        } else {
            List<PostStatus> excludedStatuses = Arrays.asList(PostStatus.DRAFT, PostStatus.HIDDEN);
            posts = postsRepository.findByIsDeletedFalseAndStatusNotIn(excludedStatuses, pageable);
        }

        List<PostDTO> content = posts.getContent().stream()
                .map(PostMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isFirst(),
                posts.isLast()
        );
    }

    @Override
    public PostDTO getPostById(Long postId) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostException("Bài viết không tìm thấy"));
        if (post.getIsDeleted()) {
            throw new PostException("Bài viết đã bị xóa");
        }
        return PostMapper.toDTO(post);
    }


    @Override
    @Transactional
    public void deletePost(Long postId) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostException("Bài viết không tìm thấy"));

        post.setIsDeleted(true);
        postsRepository.save(post);
    }
//    ====================================================================================================================
//                                                           ADMIN & SELLER

    @Override
    @Transactional
    public PostDTO updatePostStatus(Long postId, PostStatus newStatus, CustomUserDetails currentUser) {
        PostsEntity post = postsRepository.findById(postId)
                .orElseThrow(() -> new PostException("Không tìm thấy bài viết"));

        PostStatus current = post.getStatus();
        RoleName role = currentUser.getUser().getRoleName();

        if (Boolean.TRUE.equals(post.getIsDeleted())) {
            throw new PostException("Bài viết đã bị xóa, không thể thay đổi trạng thái");
        }

        if (role == RoleName.SELLER) {
            if (!post.getSeller().getId().equals(currentUser.getUser().getId())) {
                throw new PostException("Seller không sở hữu bài viết này");
            }
            if (!current.canSellerTransitionTo(newStatus)) {
                throw new PostException(
                        String.format("Seller không thể chuyển từ %s sang %s", current, newStatus)
                );
            }
        }

        if (role == RoleName.ADMIN) {
            if (!current.canAdminTransitionTo(newStatus)) {
                throw new PostException(
                        String.format("Admin không thể chuyển từ %s sang %s", current, newStatus)
                );
            }
        }

        post.setStatus(newStatus);
        return  PostMapper.toDTO(postsRepository.save(post));
    }

    //    ====================================================================================================================
    //                                                       PUBLIC
    private final EntityManager entityManager;

    @Override
    public PageResponse<PostDTO> getPublicPosts(Pageable pageable) {
        Page<PostsEntity> posts = postsRepository.findByIsDeletedFalseAndStatus(PostStatus.APPROVED, pageable);

        List<PostDTO> content = posts.getContent()
                .stream()
                .map(PostMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                content,
                posts.getNumber(),
                posts.getSize(),
                posts.getTotalElements(),
                posts.getTotalPages(),
                posts.isFirst(),
                posts.isLast()
        );
    }

    @Override
    public PostDTO getPublicPostById(Long postId) {
        PostsEntity post = postsRepository.findById(postId).orElseThrow(() -> new PostException("Bài viết không tìm thấy"));
        if (Boolean.TRUE.equals(post.getIsDeleted()) || post.getStatus() != PostStatus.APPROVED) {
            throw new PostException("Bài viết không tồn tại");
        }
        return PostMapper.toDTO(post);
    }


    @Override
    public ApiResponse<List<PostDTO>> searchCars(Map<String, String> params) {
        StringBuilder jpql = new StringBuilder("SELECT p FROM PostsEntity p WHERE p.isDeleted = false AND p.status = :status");

        List<String> conditions = new ArrayList<>();

        if (params.containsKey("make")) conditions.add("p.carDetail.make = :make");
        if (params.containsKey("model")) conditions.add("p.carDetail.model = :model");
        if (params.containsKey("year")) conditions.add("p.carDetail.year = :year");
        if (params.containsKey("color")) conditions.add("p.carDetail.color = :color");
        if (params.containsKey("condition")) conditions.add("p.carDetail.condition = :condition");
        if (params.containsKey("minPrice")) conditions.add("p.price >= :minPrice");
        if (params.containsKey("maxPrice")) conditions.add("p.price <= :maxPrice");

        if (!conditions.isEmpty()) {
            jpql.append(" AND ").append(String.join(" AND ", conditions));
        }

        TypedQuery<PostsEntity> query = entityManager.createQuery(jpql.toString(), PostsEntity.class);
        query.setParameter("status", PostStatus.APPROVED);

        // set params
        params.forEach((k, v) -> {
            switch (k) {
                case "make", "model", "color", "condition" -> query.setParameter(k, v);
                case "year" -> query.setParameter(k, Integer.valueOf(v));
                case "minPrice", "maxPrice" -> query.setParameter(k, new BigDecimal(v));
            }
        });

        List<PostDTO> results = query.getResultList().stream()
                .map(PostMapper::toDTO)
                .toList();

        return new ApiResponse<>("Search results", 200, results, "/api/posts/search");
    }

}
