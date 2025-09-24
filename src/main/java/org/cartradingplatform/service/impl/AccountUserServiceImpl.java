package org.cartradingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.exceptions.UsersException;
import org.cartradingplatform.model.dto.PageResponse;
import org.cartradingplatform.model.dto.requests.UpdateUserRequestDTO;
import org.cartradingplatform.model.dto.response.ProfileDTO;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.enums.RoleName;
import org.cartradingplatform.model.mapper.UserMapper;
import org.cartradingplatform.repository.UserRepository;
import org.cartradingplatform.security.CustomUserDetails;
import org.cartradingplatform.service.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountUserServiceImpl implements AccountUserService {
    private final UserRepository  userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    public ProfileDTO getUserProfile(Long id) {

        UsersEntity userProfile = userRepository.findById(id)
                .orElseThrow(()-> new UsersException("Khong tim thay user"));

        return userMapper.toDTO(userProfile);
    }

    @Override
    public ProfileDTO updateUserProfile(Long id, UpdateUserRequestDTO dto, CustomUserDetails currentUser) {
        UsersEntity user = userRepository.findById(id)
                .orElseThrow(()-> new UsersException("Khong tim thay user"));

        // Nếu là ADMIN -> được update tất cả field
        if (currentUser.getUser().getRoleName().equals(RoleName.ADMIN)) {
            if (dto.getEmail() != null) user.setEmail(dto.getEmail());
            if (dto.getFullName() != null) user.setFullName(dto.getFullName());
            if (dto.getNumberPhone() != null) user.setNumberPhone(dto.getNumberPhone());
            if (dto.getDateOfBirth() != null) user.setDateOfBirth(dto.getDateOfBirth());
            if (dto.getGender() != null) user.setGender(dto.getGender());
            if (dto.getRoleName() != null) user.setRoleName(dto.getRoleName());
            if (dto.getIsActive() != null) user.setActive(dto.getIsActive());
        }
        // Nếu là USER -> chỉ update hồ sơ cá nhân của chính mình
        else if (currentUser.getUser().getId().equals(id)) {
            if (dto.getFullName() != null) user.setFullName(dto.getFullName());
            if (dto.getNumberPhone() != null) user.setNumberPhone(dto.getNumberPhone());
            if (dto.getDateOfBirth() != null) user.setDateOfBirth(dto.getDateOfBirth());
            if (dto.getGender() != null) user.setGender(dto.getGender());
        } else {
            throw new UsersException("Bạn không có quyền cập nhật user khác");
        }

        UsersEntity saved = userRepository.save(user);

        return userMapper.toDTO(saved);
    }

    @Override
    public PageResponse<ProfileDTO> getAllUsers(String email, String roleName, Boolean isActive, Pageable pageable) {
            RoleName roleEnum = null;
            if (roleName != null && !roleName.isBlank()) {
                roleEnum = RoleName.valueOf(roleName.toUpperCase());
            }
            Page<UsersEntity> page = userRepository.findAllWithFilters(email, roleEnum, isActive, pageable);

            List<ProfileDTO> content = page.getContent().stream()
                    .map(userMapper::toDTO)
                    .toList();

            return new PageResponse<>(
                    content,
                    page.getNumber(),
                    page.getSize(),
                    page.getTotalElements(),
                    page.getTotalPages(),
                    page.isFirst(),
                    page.isLast()
            );
    }

    @Override
    public ProfileDTO createUser(ProfileDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new UsersException("Email đã tồn tại vui lòng chọn email khác.");
        }
        UsersEntity user = new UsersEntity();
        user = userMapper.toEntity(dto);

        UsersEntity saved = userRepository.save(user);
        return userMapper.toDTO(saved);
    }

    @Override
    public ProfileDTO updateUserStatus(Long id, boolean active) {
        UsersEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UsersException("Không tìm thấy user"));

        user.setActive(active);
        UsersEntity saved = userRepository.save(user);

        return userMapper.toDTO(saved);
    }


}
