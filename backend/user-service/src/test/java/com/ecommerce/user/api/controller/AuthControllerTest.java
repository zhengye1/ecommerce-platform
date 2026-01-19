package com.ecommerce.user.api.controller;

import com.ecommerce.common.security.filter.JwtAuthenticationFilter;
import com.ecommerce.common.security.jwt.JwtProperties;
import com.ecommerce.common.security.jwt.JwtTokenProvider;
import com.ecommerce.common.security.jwt.TokenBlacklistService;
import com.ecommerce.user.application.dto.request.LoginRequest;
import com.ecommerce.user.application.dto.request.RegisterRequest;
import com.ecommerce.user.application.mapper.UserMapper;
import com.ecommerce.user.domain.exception.DuplicateEmailException;
import com.ecommerce.user.domain.exception.InvalidCredentialsException;
import com.ecommerce.user.domain.model.Role;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.model.UserStatus;
import com.ecommerce.user.domain.service.UserDomainService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller tests for AuthController.
 *
 * 呢个test用 @WebMvcTest - 只load controller layer，mock晒dependencies。
 * 适合test: request validation, response format, HTTP status codes
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)  // Disable security filters for unit testing
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        JpaRepositoriesAutoConfiguration.class
})
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // Mock all dependencies
    @MockitoBean
    private UserDomainService userDomainService;
    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;
    @MockitoBean
    private JwtProperties jwtProperties;
    @MockitoBean
    private TokenBlacklistService tokenBlacklistService;
    @MockitoBean
    private UserMapper userMapper;
    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;  // Required by SecurityConfig

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = User.builder()
                .email("test@example.com")
                .passwordHash("encoded_password")
                .firstName("John")
                .lastName("Doe")
                .role(Role.CUSTOMER)
                .status(UserStatus.ACTIVE)
                .build();
        testUser.setId(userId);
    }

    // ========================================
    // Register Endpoint Tests
    // ========================================
    @Nested
    @DisplayName("POST /api/v1/auth/register")
    class RegisterTests {

        @Test
        @DisplayName("should register successfully with valid request")
        void shouldRegisterSuccessfully() throws Exception {
            // Given
            RegisterRequest request = new RegisterRequest();
            request.setEmail("newuser@example.com");
            request.setPassword("password123");
            request.setFirstName("Jane");
            request.setLastName("Doe");

            when(userDomainService.registerUser(anyString(), anyString(), anyString(), anyString(), any(Role.class)))
                    .thenReturn(testUser);
            when(jwtTokenProvider.generateAccessToken(any(), anyString(), anyString()))
                    .thenReturn("mock_access_token");
            when(jwtTokenProvider.generateRefreshToken(any()))
                    .thenReturn("mock_refresh_token");
            when(jwtProperties.getAccessTokenExpiration())
                    .thenReturn(900000L);

            // When & Then
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.accessToken").value("mock_access_token"))
                    .andExpect(jsonPath("$.data.refreshToken").value("mock_refresh_token"));
        }

        @Test
        @DisplayName("should return 400 when email is invalid")
        void shouldReturn400WhenEmailInvalid() throws Exception {
            // Given - invalid email format
            RegisterRequest request = new RegisterRequest();
            request.setEmail("not-an-email");  // Invalid!
            request.setPassword("password123");
            request.setFirstName("Jane");
            request.setLastName("Doe");

            // When & Then
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
            // Validation error - userDomainService should NOT be called
        }

        @Test
        @DisplayName("should return 400 when password too short")
        void shouldReturn400WhenPasswordTooShort() throws Exception {
            // Given
            RegisterRequest request = new RegisterRequest();
            request.setEmail("test@example.com");
            request.setPassword("123");  // Too short! (min 8)
            request.setFirstName("Jane");
            request.setLastName("Doe");

            // When & Then
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("should return 409 when email already exists")
        void shouldReturn409WhenEmailExists() throws Exception {
            // Given
            RegisterRequest request = new RegisterRequest();
            request.setEmail("existing@example.com");
            request.setPassword("password123");
            request.setFirstName("Jane");
            request.setLastName("Doe");

            when(userDomainService.registerUser(anyString(), anyString(), anyString(), anyString(), any(Role.class)))
                    .thenThrow(new DuplicateEmailException("existing@example.com"));

            // When & Then
            mockMvc.perform(post("/api/v1/auth/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isConflict());
        }
    }

    // ========================================
    // Login Endpoint Tests
    // ========================================
    @Nested
    @DisplayName("POST /api/v1/auth/login")
    class LoginTests {

        @Test
        @DisplayName("should login successfully with valid credentials")
        void shouldLoginSuccessfully() throws Exception {
            // Given
            LoginRequest request = new LoginRequest();
            request.setEmail("test@example.com");
            request.setPassword("password123");

            when(userDomainService.authenticate(anyString(), anyString()))
                    .thenReturn(Optional.of(testUser));
            when(jwtTokenProvider.generateAccessToken(any(), anyString(), anyString()))
                    .thenReturn("mock_access_token");
            when(jwtTokenProvider.generateRefreshToken(any()))
                    .thenReturn("mock_refresh_token");
            when(jwtProperties.getAccessTokenExpiration())
                    .thenReturn(900000L);

            // When & Then
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.accessToken").exists());
        }

        @Test
        @DisplayName("should return 401 when credentials invalid")
        void shouldReturn401WhenCredentialsInvalid() throws Exception {
            // Given
            LoginRequest request = new LoginRequest();
            request.setEmail("test@example.com");
            request.setPassword("wrongpassword");

            when(userDomainService.authenticate(anyString(), anyString()))
                    .thenReturn(Optional.empty());

            // When & Then
            mockMvc.perform(post("/api/v1/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ========================================
    // Refresh Token Tests
    // ========================================
    @Nested
    @DisplayName("POST /api/v1/auth/refresh")
    class RefreshTests {

        @Test
        @DisplayName("should refresh token successfully")
        void shouldRefreshSuccessfully() throws Exception {
            // Given
            String refreshToken = "valid_refresh_token";

            when(jwtTokenProvider.validateToken(refreshToken)).thenReturn(true);
            when(tokenBlacklistService.isBlacklisted(refreshToken)).thenReturn(false);
            when(jwtTokenProvider.getUserIdFromToken(refreshToken)).thenReturn(userId);
            when(userDomainService.getUserById(userId)).thenReturn(Optional.of(testUser));
            when(jwtTokenProvider.generateAccessToken(any(), anyString(), anyString()))
                    .thenReturn("new_access_token");
            when(jwtTokenProvider.generateRefreshToken(any()))
                    .thenReturn("new_refresh_token");
            when(jwtProperties.getAccessTokenExpiration())
                    .thenReturn(900000L);

            // When & Then
            mockMvc.perform(post("/api/v1/auth/refresh")
                            .header("X-Refresh-Token", refreshToken))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.accessToken").value("new_access_token"));
        }

        @Test
        @DisplayName("should return 401 when refresh token invalid")
        void shouldReturn401WhenTokenInvalid() throws Exception {
            // Given
            String invalidToken = "invalid_token";
            when(jwtTokenProvider.validateToken(invalidToken)).thenReturn(false);

            // When & Then
            mockMvc.perform(post("/api/v1/auth/refresh")
                            .header("X-Refresh-Token", invalidToken))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("should return 401 when token blacklisted")
        void shouldReturn401WhenTokenBlacklisted() throws Exception {
            // Given
            String blacklistedToken = "blacklisted_token";
            when(jwtTokenProvider.validateToken(blacklistedToken)).thenReturn(true);
            when(tokenBlacklistService.isBlacklisted(blacklistedToken)).thenReturn(true);

            // When & Then
            mockMvc.perform(post("/api/v1/auth/refresh")
                            .header("X-Refresh-Token", blacklistedToken))
                    .andExpect(status().isUnauthorized());
        }
    }
}
