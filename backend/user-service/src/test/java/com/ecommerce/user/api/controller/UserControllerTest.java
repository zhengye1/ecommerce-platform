package com.ecommerce.user.api.controller;

import com.ecommerce.common.security.UserPrincipal;
import com.ecommerce.common.security.filter.JwtAuthenticationFilter;
import com.ecommerce.user.application.dto.request.ChangePasswordRequest;
import com.ecommerce.user.application.dto.request.UpdateProfileRequest;
import com.ecommerce.user.application.dto.response.UserResponse;
import com.ecommerce.user.application.mapper.UserMapper;
import com.ecommerce.user.domain.exception.InvalidCredentialsException;
import com.ecommerce.user.domain.model.Role;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.model.UserStatus;
import com.ecommerce.user.domain.service.UserDomainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Controller tests for UserController.
 *
 * 关键点：用 SecurityContextHolder 注入 UserPrincipal
 * 1. @BeforeEach 设置 SecurityContext
 * 2. @AfterEach 清理 SecurityContext
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
@DisplayName("UserController Tests")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserDomainService userDomainService;

    @MockitoBean
    private UserMapper userMapper;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // Test data
    private UUID userId;
    private User testUser;
    private UserResponse userResponse;

    private UUID adminUserId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        adminUserId = UUID.randomUUID();

        // Setup test user
        testUser = User.builder()
                .email("test@example.com")
                .passwordHash("encoded")
                .firstName("John")
                .lastName("Doe")
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();
        testUser.setId(userId);

        // setup test admin user
        User adminUser = User.builder()
                .email("admin@example.com")
                .passwordHash("encoded")
                .firstName("Admin")
                .lastName("Test")
                .role(Role.ADMIN)
                .status(UserStatus.ACTIVE)
                .build();
        adminUser.setId(adminUserId);

        // Setup response
        userResponse = new UserResponse();
        userResponse.setId(userId);
        userResponse.setEmail("test@example.com");
        userResponse.setFirstName("John");
        userResponse.setLastName("Doe");

        // 关键：注入 UserPrincipal 到 SecurityContext
        UserPrincipal principal = UserPrincipal.builder()
                .id(userId)
                .email("test@example.com")
                .role("CUSTOMER")
                .enabled(true)
                .accountNonLocked(true)
                .build();

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @AfterEach
    void tearDown() {
        // 清理 SecurityContext
        SecurityContextHolder.clearContext();
    }

    @Nested
    @DisplayName("GET /api/v1/users/me")
    class GetCurrentUserTests {

        @Test
        @DisplayName("should return current user profile")
        void shouldReturnProfile() throws Exception {
            // Given
            when(userDomainService.getUserById(userId)).thenReturn(Optional.of(testUser));
            when(userMapper.toResponse(testUser)).thenReturn(userResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/users/me"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.email").value("test@example.com"))
                    .andExpect(jsonPath("$.data.firstName").value("John"));
        }

        @Test
        @DisplayName("should return 404 when user not found")
        void shouldReturn404WhenNotFound() throws Exception {
            // Given - use any() matcher for more stable mocking
            when(userDomainService.getUserById(any(UUID.class))).thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(get("/api/v1/users/me"))
                    .andExpect(status().isNotFound());
        }
    }

    // TODO: 你可以继续加:
    // - PUT /api/v1/users/me (update profile)
    @Nested
    @DisplayName("PUT /api/v1/users/me")
    class UpdateUserProfileTest{

        @Test
        @DisplayName("should return current user profile")
        void shouldUpdateProfile() throws Exception{
            // Given
            UpdateProfileRequest request = new UpdateProfileRequest();
            request.setFirstName("NewFirst");
            request.setLastName("NewLast");
            request.setPhone("+16478617036");
            User updatedUser = User.builder()
                    .email("test@example.com")
                    .firstName("NewFirst")
                    .lastName("NewLast")
                    .phone("+85291234567")
                    .role(Role.CUSTOMER)
                    .status(UserStatus.ACTIVE)
                    .build();
            updatedUser.setId(userId);
            UserResponse updatedResponse = new UserResponse();
            updatedResponse.setId(userId);
            updatedResponse.setFirstName("NewFirst");
            updatedResponse.setLastName("NewLast");
            updatedResponse.setPhone("+16478617036");

            when(userDomainService.updateProfile(eq(userId), anyString(), anyString(), anyString()))
                    .thenReturn(updatedUser);
            when(userMapper.toResponse(updatedUser)).thenReturn(updatedResponse);

            // When & Then - 实际perform PUT request
            mockMvc.perform(put("/api/v1/users/me")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.firstName").value("NewFirst"))
                    .andExpect(jsonPath("$.data.lastName").value("NewLast"))
                    .andExpect(jsonPath("$.data.phone").value("+16478617036"))
                    .andExpect(jsonPath("$.message").value("Profile updated successfully"));

        }

        @Test
        @DisplayName("Should change password")
        void shouldChangePassword() throws Exception{
            // Given
            ChangePasswordRequest request = new ChangePasswordRequest();
            request.setCurrentPassword("OldPass1!");
            request.setNewPassword("NewPass1!");

            // When & Then - 实际perform PUT request
            mockMvc.perform(put("/api/v1/users/me/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value("Password changed successfully"));

            verify(userDomainService).changePassword(eq(userId), eq("OldPass1!"), eq("NewPass1!"));
        }

        @Test
        @DisplayName("Should throw exception for wrong old password")
        void shouldThrow401ForWrongPassword() throws Exception{
            // Given
            ChangePasswordRequest request = new ChangePasswordRequest();
            request.setCurrentPassword("OldPass1!");
            request.setNewPassword("NewPass1!");

            doThrow(new InvalidCredentialsException("Current password is incorrect"))
                    .when(userDomainService).changePassword(eq(userId), anyString(), anyString());

            // When & Then - 实际perform PUT request
            mockMvc.perform(put("/api/v1/users/me/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should throw exception for old password and new password same")
        void shouldThrow401ForSamePasswordInChange() throws Exception{
            // Given
            ChangePasswordRequest request = new ChangePasswordRequest();
            request.setCurrentPassword("OldPass1!");
            request.setNewPassword("OldPass1!");

            // 全部 arguments 都要用 matcher (eq)
            doThrow(new InvalidCredentialsException("New password must be different from current password"))
                    .when(userDomainService).changePassword(eq(userId), eq("OldPass1!"), eq("OldPass1!"));

            // When & Then
            mockMvc.perform(put("/api/v1/users/me/password")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }


    // - Admin endpoints (需要改 principal.role = "ADMIN")
    @Nested
    @DisplayName("Admin controller Test")
    class AdminControlTest{
        @Test
        @DisplayName("Should return user details")
        void shouldReturnUserDetails() throws Exception{
            // Given
            // Set principal to admin
            UserPrincipal principal = UserPrincipal.builder()
                    .id(adminUserId)
                    .email("admin@example.com")
                    .role("ADMIN")
                    .enabled(true)
                    .accountNonLocked(true)
                    .build();
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            when(userDomainService.getUserById(userId)).thenReturn(Optional.of(testUser));
            when(userMapper.toResponse(testUser)).thenReturn(userResponse);

            // When & Then
            mockMvc.perform(get("/api/v1/users/" + userId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.email").value("test@example.com"))
                    .andExpect(jsonPath("$.data.firstName").value("John"));
        }

        // Note: Authorization tests (e.g., CUSTOMER cannot access admin endpoints)
        // should be done in integration tests with @SpringBootTest, not here.
        // @WebMvcTest with addFilters=false does not enforce @PreAuthorize.

        @Test
        @DisplayName("Activate user success")
        void activateUserSuccess() throws Exception{
            when(userDomainService.activateUser(userId)).thenReturn(testUser);
            when(userMapper.toResponse(testUser)).thenReturn(userResponse);
            userResponse.setStatus(UserStatus.ACTIVE);

            // When & Then
            mockMvc.perform(put("/api/v1/users/" + userId + "/activate") )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("User activated successfully"))
                    .andExpect(jsonPath("$.data.status").value("ACTIVE"));
        }

        @Test
        @DisplayName("Deactivate user success")
        void deactivateUserSuccess() throws Exception{
            when(userDomainService.deactivateUser(userId)).thenReturn(testUser);
            when(userMapper.toResponse(testUser)).thenReturn(userResponse);
            userResponse.setStatus(UserStatus.INACTIVE);

            // When & Then
            mockMvc.perform(put("/api/v1/users/" + userId + "/deactivate") )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("User deactivated successfully"))
                    .andExpect(jsonPath("$.data.status").value("INACTIVE"));
        }

        @Test
        @DisplayName("Suspend user success")
        void suspendUserSuccess() throws Exception{
            when(userDomainService.suspendUser(userId)).thenReturn(testUser);
            when(userMapper.toResponse(testUser)).thenReturn(userResponse);
            userResponse.setStatus(UserStatus.SUSPENDED);

            // When & Then
            mockMvc.perform(put("/api/v1/users/" + userId + "/suspend") )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("User suspended successfully"))
                    .andExpect(jsonPath("$.data.status").value("SUSPENDED"));
        }
    }
}
