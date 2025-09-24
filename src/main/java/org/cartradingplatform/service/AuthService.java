package org.cartradingplatform.service;

import org.cartradingplatform.model.dto.requests.LoginRequestDTO;
import org.cartradingplatform.model.dto.requests.RegisterRequestDTO;
import org.cartradingplatform.model.dto.response.UserInfoDTO;

import java.util.Map;

public interface AuthService {
    Map<String, Object> login(LoginRequestDTO request);
    UserInfoDTO createUser(RegisterRequestDTO request);
    void logout(String email, String token);
}
