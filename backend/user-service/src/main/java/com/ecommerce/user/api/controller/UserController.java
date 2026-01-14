package com.ecommerce.user.api.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.common.security.SecurityUtils;
import com.ecommerce.common.security.UserPrincipal;
import com.ecommerce.user.application.dto.request.ChangePasswordRequest;
import com.ecommerce.user.application.dto.request.UpdateProfileRequest;
import com.ecommerce.user.application.dto.response.UserResponse;
import com.ecommerce.user.application.mapper.UserMapper;
import com.ecommerce.user.domain.exception.UserNotFoundException;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.service.UserDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * User profile management controller.
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserDomainService userDomainService;
    private final UserMapper userMapper;

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ApiResponse<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        User user = userDomainService.getUserById(principal.getId())
                .orElseThrow(() -> new UserNotFoundException(principal.getId()));

        return ApiResponse.success(userMapper.toResponse(user));
    }

    @PutMapping("/me")
    @Operation(summary = "Update current user profile")
    public ApiResponse<UserResponse> updateProfile(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody UpdateProfileRequest request) {

        User user = userDomainService.updateProfile(
                principal.getId(),
                request.getFirstName(),
                request.getLastName(),
                request.getPhone()
        );

        return ApiResponse.success(userMapper.toResponse(user), "Profile updated successfully");
    }

    @PutMapping("/me/password")
    @Operation(summary = "Change current user password")
    public ApiResponse<Void> changePassword(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request) {

        userDomainService.changePassword(
                principal.getId(),
                request.getCurrentPassword(),
                request.getNewPassword()
        );

        return ApiResponse.success("Password changed successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get user by ID (Admin only)")
    public ApiResponse<UserResponse> getUserById(@PathVariable UUID id) {
        User user = userDomainService.getUserById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return ApiResponse.success(userMapper.toResponse(user));
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Activate user account (Admin only)")
    public ApiResponse<UserResponse> activateUser(@PathVariable UUID id) {
        User user = userDomainService.activateUser(id);
        return ApiResponse.success(userMapper.toResponse(user), "User activated successfully");
    }

    @PutMapping("/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Suspend user account (Admin only)")
    public ApiResponse<UserResponse> suspendUser(@PathVariable UUID id) {
        User user = userDomainService.suspendUser(id);
        return ApiResponse.success(userMapper.toResponse(user), "User suspended successfully");
    }
}
