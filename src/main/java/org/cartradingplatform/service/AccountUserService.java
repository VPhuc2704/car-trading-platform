package org.cartradingplatform.service;

import org.cartradingplatform.model.dto.PageResponse;
import org.cartradingplatform.model.dto.requests.ResetPassword;
import org.cartradingplatform.model.dto.requests.UpdateUserRequestDTO;
import org.cartradingplatform.model.dto.response.ProfileDTO;
import org.cartradingplatform.security.CustomUserDetails;
import org.springframework.data.domain.Pageable;

public interface AccountUserService {
    ProfileDTO getUserProfile(Long id);
    ProfileDTO updateUserProfile(Long id, UpdateUserRequestDTO dto, CustomUserDetails  currentUser);
    PageResponse<ProfileDTO> getAllUsers(String email, String roleName, Boolean isActive, Pageable pageable);
    ProfileDTO createUser(ProfileDTO dto);
    void deleteUser(Long id);
    void resetPassword(Long id, ResetPassword resetPassword);


}
