package org.cartradingplatform.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cartradingplatform.exceptions.UsersException;
import org.cartradingplatform.model.dto.requests.LoginRequestDTO;
import org.cartradingplatform.model.dto.requests.RegisterRequestDTO;
import org.cartradingplatform.model.dto.response.UserInfoDTO;
import org.cartradingplatform.model.entity.BlacklistedTokens;
import org.cartradingplatform.model.entity.UsersEntity;
import org.cartradingplatform.model.enums.RoleName;
import org.cartradingplatform.repository.BlacklistedTokensRepository;
import org.cartradingplatform.repository.UserRepository;
import org.cartradingplatform.security.JwtTokenProvider;
import org.cartradingplatform.service.AuthService;
import org.cartradingplatform.service.BlacklistedTokensService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlacklistedTokensService blacklistedTokensService;
    private final BlacklistedTokensRepository  blacklistedTokensRepository;

    @Override
    public Map<String, Object> login(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = tokenProvider.generateAccessToken(userDetails);

        UsersEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsersException("User not found"));


        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("type", "Bearer");
        response.put("id", user.getId());
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());
        response.put("numberPhone", user.getNumberPhone());
        response.put("role", user.getRoleName().name());

        return response;
    }

    @Override
    public UserInfoDTO createUser(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UsersException("Email is already in use");
        }

        UsersEntity user = new UsersEntity();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setNumberPhone(request.getPhone());
        user.setRoleName(RoleName.valueOf(request.getRoleName()));
        user.setActive(true);

        userRepository.save(user);

        return new UserInfoDTO(
                user.getFullName(),
                user.getEmail(),
                user.getRoleName().name(),
                user.getNumberPhone()
        );
    }

    @Override
    @Transactional
    public void logout(String email, String token) {
        try {

            if (blacklistedTokensRepository.existsByToken(token)) {
                log.warn("Token already blacklisted, skipping: {}", token);
                return;
            }

            // Find user by email
            UsersEntity user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsersException("User not found"));

            BlacklistedTokens blacklistedTokens = new BlacklistedTokens();
            blacklistedTokens.setToken(token);
            blacklistedTokens.setUser(user);
            blacklistedTokens.setRevoked(true);
            blacklistedTokensRepository.save(blacklistedTokens);

            // Revoke refresh token in database
            if (token != null && !token.isEmpty()) {
                blacklistedTokensService.blacklistToken(token, user);
                log.info("Access token blacklisted for user: {}", email);
            }

            // Clear security context
            SecurityContextHolder.clearContext();

            log.info("User {} logged out successfully", email);

        } catch (Exception e) {
            log.error("Error during logout for user {}: {}", email, e.getMessage());
            // Still clear context even if there's an error
            SecurityContextHolder.clearContext();
        }
    }


}
