package org.cartradingplatform.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.cartradingplatform.model.dto.requests.LoginRequestDTO;
import org.cartradingplatform.model.dto.requests.RegisterRequestDTO;
import org.cartradingplatform.model.dto.response.LoginResponse;
import org.cartradingplatform.model.dto.response.TokenDTO;
import org.cartradingplatform.model.dto.response.UserInfoDTO;

import org.cartradingplatform.security.JwtTokenProvider;
import org.cartradingplatform.service.AuthService;
import org.cartradingplatform.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO request) {
        Map<String, Object> auth = authService.login(request);

        UserInfoDTO userInfo = new UserInfoDTO(
                (String) auth.get("fullName"),
                (String) auth.get("email"),
                (String) auth.get("role"),
                (String) auth.get("numberPhone")
        );

        TokenDTO tokens = new TokenDTO(
                "Bearer",
                (String) auth.get("token")
        );

        LoginResponse loginResponse = new LoginResponse(userInfo, tokens);
        ApiResponse<LoginResponse> apiResponse = new ApiResponse<>(
                "Login successful",
                200,
                loginResponse,
                "/api/auth/login"
        );

        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequestDTO request) {
        try {
            UserInfoDTO user = authService.createUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>("User registered successfully.", 201, user, "/api/auth/register"));
        } catch (Exception e) {
            log.error("Registration failed for user: {}", request.getEmail(), e);
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>("Registration failed", 400, e.getMessage(), "/api/auth/register"));
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authorization) {
        try {
            // Lấy email từ access token
            String accessToken = authorization.replace("Bearer ", "");
            String email = jwtTokenProvider.getUsernameFromToken(accessToken);

            // Logout
            authService.logout(email, accessToken);

            return ResponseEntity.ok().body(new ApiResponse<>("Logged out successfully", 200, null , "/api/auth/logout"));
        } catch (Exception e) {
            log.error("Logout failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Logout failed");
        }
    }

}
