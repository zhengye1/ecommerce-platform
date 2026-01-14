package com.ecommerce.user.api.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.common.security.jwt.JwtProperties;
import com.ecommerce.common.security.jwt.JwtTokenProvider;
import com.ecommerce.common.security.jwt.TokenBlacklistService;
import com.ecommerce.user.application.dto.request.LoginRequest;
import com.ecommerce.user.application.dto.request.RegisterRequest;
import com.ecommerce.user.application.dto.response.AuthResponse;
import com.ecommerce.user.application.dto.response.UserResponse;
import com.ecommerce.user.application.mapper.UserMapper;
import com.ecommerce.user.domain.exception.InvalidCredentialsException;
import com.ecommerce.user.domain.model.Role;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.service.UserDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller for login, register, logout.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User authentication endpoints")
public class AuthController {

    private final UserDomainService userDomainService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;
    private final TokenBlacklistService tokenBlacklistService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register a new user")
    public ApiResponse<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request for email: {}", request.getEmail());

        User user = userDomainService.registerUser(
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                Role.CUSTOMER
        );

        AuthResponse authResponse = createAuthResponse(user);
        return ApiResponse.success(authResponse, "Registration successful");
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email and password")
    public ApiResponse<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for email: {}", request.getEmail());

        User user = userDomainService.authenticate(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidCredentialsException::new);

        AuthResponse authResponse = createAuthResponse(user);
        return ApiResponse.success(authResponse, "Login successful");
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout and invalidate token")
    public ApiResponse<Void> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = jwtTokenProvider.extractTokenFromHeader(authHeader);

        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
            log.info("User logged out successfully");
        }

        return ApiResponse.success("Logout successful");
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ApiResponse<AuthResponse> refreshToken(@RequestHeader("X-Refresh-Token") String refreshToken) {
        // TODO: Implement refresh token logic
        // 1. Validate refresh token
        // 2. Check if not blacklisted
        // 3. Generate new access token
        throw new UnsupportedOperationException("Refresh token not implemented yet");
    }

    private AuthResponse createAuthResponse(User user) {
        String accessToken = jwtTokenProvider.generateAccessToken(
                user.getId(),
                user.getEmail(),
                user.getRole().name()
        );

        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getId());
        UserResponse userResponse = userMapper.toResponse(user);

        return AuthResponse.of(
                accessToken,
                refreshToken,
                jwtProperties.getAccessTokenExpiration() / 1000,
                userResponse
        );
    }
}
