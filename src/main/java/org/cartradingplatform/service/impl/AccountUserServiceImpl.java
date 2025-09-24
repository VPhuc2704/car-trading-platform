package org.cartradingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import org.cartradingplatform.exceptions.UsersException;
import org.cartradingplatform.model.dto.response.ProfileDTO;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.mapper.UserMapper;
import org.cartradingplatform.repository.UserRepository;
import org.cartradingplatform.service.AccountUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountUserServiceImpl implements AccountUserService {
    private final UserRepository  userRepository;
    private final UserMapper userMapper;


    @Override
    public ProfileDTO getUserProfile(Long id) {

        UsersEntity userProfile = userRepository.findById(id)
                .orElseThrow(()-> new UsersException("Khong tim thay user"));

        return userMapper.toDTO(userProfile);
    }
}
