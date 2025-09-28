package org.cartradingplatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.PostDTO;
import org.cartradingplatform.model.dto.response.PostWithPaymentResponse;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.enums.PaymentMethod;
import org.cartradingplatform.model.enums.PostStatus;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.PostService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/seller/posts")
@RequiredArgsConstructor
public class SellerPostController {
    private final PostService postService;

    private UsersEntity getCurrentSeller() {
        return ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

    private String savePostImage(MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()) return null;

        String uploadPath = System.getProperty("user.dir") + "/uploads/postImg/";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String newFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadPath, newFileName);

        try(InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        return "/post/img/" + newFileName;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PostWithPaymentResponse>> createPost(@RequestPart("postDTO") String postJson,
                                                                           @RequestPart(value = "imageFile", required = true)  List<MultipartFile> imageFiles,
                                                                           HttpServletRequest request) {

        try {
            PostDTO postDTO = new ObjectMapper().readValue(postJson, PostDTO.class);

            if (imageFiles != null && !imageFiles.isEmpty()) {
                for (MultipartFile file : imageFiles) {
                    String imagePath = savePostImage(file);
                    if (imagePath != null) {
                        postDTO.getImages().add(imagePath);
                    }
                }
            }

            PostWithPaymentResponse dto =  postService.createPost(getCurrentSeller().getId(), postDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(
                            "Tạo bai viet thanh cong,  chuyển tới thanh toán",
                            HttpStatus.CREATED.value(), dto, request.getRequestURI())

            );
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(
                            new ApiResponse<>(
                                    "Error processing image: " + e.getMessage(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    null,
                                    request.getRequestURI()
                            )
                    );

        }

    }

    // Lấy danh sách bài đăng của seller hiện tại
    @GetMapping
    public ResponseEntity<ApiResponse<List<PostDTO>>> getMyPosts(HttpServletRequest request) {

        List<PostDTO> postDTOList = postService.getMyPosts(getCurrentSeller().getId());

        return ResponseEntity.ok( new ApiResponse<>(
                "Lấy thành công tất cả bài đăng",
                HttpStatus.OK.value(),
                postDTOList,
                request.getRequestURI()
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> getPostById(@PathVariable Long id,
                                                            HttpServletRequest request) {

        PostDTO postDTO = postService.getPostById(getCurrentSeller().getId(), id);
        return ResponseEntity.ok(new ApiResponse<>(
                "Lấy thành công bài đăng" + id,
                HttpStatus.OK.value(),
                postDTO,
                request.getRequestURI())
        );
    }

    // Cập nhật post
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PostDTO>> updatePost(@PathVariable Long id,
                                                           @RequestPart("postDTO") String postJson,
                                                           @RequestPart(value = "imageFile", required =  false) List<MultipartFile>  imageFiles,
                                                           HttpServletRequest request) {
        try {
            PostDTO postDTO = null;
            if(postJson != null) {
                postDTO = new ObjectMapper().readValue(postJson, PostDTO.class);
            }

            if (postDTO == null) {
                postDTO = new PostDTO();
            }

            // Nếu có upload ảnh mới => replace ảnh cũ
            if (imageFiles != null && !imageFiles.isEmpty()) {
                List<String> newImages = new ArrayList<>();
                for (MultipartFile file : imageFiles) {
                    String imagePath = savePostImage(file);
                    if (imagePath != null) {
                        newImages.add(imagePath);
                    }
                }
                postDTO.setImages(newImages);
            } else {
                // Không upload ảnh mới => giữ nguyên ảnh cũ
                PostDTO oldPost = postService.getPostById(getCurrentSeller().getId(), id);
                postDTO.setImages(oldPost != null && oldPost.getImages() != null
                        ? new ArrayList<>(oldPost.getImages())
                        : new ArrayList<>());
            }

            PostDTO updatePost = postService.updatePost(getCurrentSeller().getId(), id, postDTO);
            return ResponseEntity.ok(new ApiResponse<>(
                    "Cập nhật thành công bài đăng",
                    HttpStatus.OK.value(),
                    updatePost,
                    request.getRequestURI())
            );

        }catch (IOException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(
                                    "Error processing image: " + e.getMessage(),
                                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                    null,
                                    request.getRequestURI()
                            )
                    );
        }
    }

    // Xóa post
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deletePost(@PathVariable Long id,
                                                          HttpServletRequest request) {
        postService.deletePost(getCurrentSeller().getId(), id);
        return ResponseEntity.ok(new ApiResponse<>(
                "Xóa bài đăng thành công",
                HttpStatus.NO_CONTENT.value(),
                null,
                request.getRequestURI())
        );
    }


    @PostMapping("/{postId}/retry-payment")
    public ResponseEntity<PostWithPaymentResponse> retryPayment(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                @PathVariable Long postId) throws UnsupportedEncodingException {
        PostWithPaymentResponse response = postService.retryPayment(currentUser.getUser().getId(), postId, PaymentMethod.VNPAY);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{postId}/status")
    public ResponseEntity<ApiResponse<PostDTO>> updatePostStatus(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                                 @PathVariable Long postId,
                                                                 @RequestParam PostStatus newStatus,
                                                                 HttpServletRequest request) {
        PostDTO updatedPost = postService.updatePostStatus(postId, newStatus, currentUser);
        return ResponseEntity.ok(new ApiResponse<>(
                "Cập nhật trạng thái thành công",
                HttpStatus.OK.value(),
                updatedPost,
                request.getRequestURI()
        ));
    }

}