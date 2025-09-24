package org.cartradingplatform.model.mapper;

import org.cartradingplatform.model.dto.response.ProfileDTO;
import org.cartradingplatform.model.entity.UsersEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public ProfileDTO toDTO(UsersEntity users){
        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setId(users.getId());
        profileDTO.setEmail(users.getEmail());
        profileDTO.setFullName(users.getFullName());
        profileDTO.setNumberPhone(users.getNumberPhone());
        profileDTO.setDateOfBirth(users.getDateOfBirth());
        profileDTO.setGender(users.getGender());
        return profileDTO;
    }

    public UsersEntity toEntity(ProfileDTO profileDTO){
        UsersEntity users = new UsersEntity();
        users.setId(profileDTO.getId());
        users.setEmail(profileDTO.getEmail());
        users.setFullName(profileDTO.getFullName());
        users.setNumberPhone(profileDTO.getNumberPhone());
        users.setDateOfBirth(profileDTO.getDateOfBirth());
        users.setGender(profileDTO.getGender());
        return users;
    }

}
