package org.cartradingplatform.model.mapper;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.model.dto.response.ProfileDTO;
import org.cartradingplatform.model.entity.UsersEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    public ProfileDTO toDTO(UsersEntity users){
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(users.getId());
        profileDTO.setEmail(users.getEmail());
        profileDTO.setFullName(users.getFullName());
        profileDTO.setNumberPhone(users.getNumberPhone());
        profileDTO.setRole(users.getRoleName());
        profileDTO.setIsActive(users.isActive());
        return profileDTO;
    }

    public UsersEntity toEntity(ProfileDTO profileDTO){
        UsersEntity users = new UsersEntity();
        users.setPasswordHash(passwordEncoder.encode("123456"));
        users.setEmail(profileDTO.getEmail());
        users.setFullName(profileDTO.getFullName());
        users.setNumberPhone(profileDTO.getNumberPhone());
        users.setRoleName(profileDTO.getRole());
        users.setActive(profileDTO.getIsActive() != null ? profileDTO.getIsActive() : true);
        return users;
    }

}
