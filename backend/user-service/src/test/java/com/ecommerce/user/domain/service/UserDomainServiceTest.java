package com.ecommerce.user.domain.service;

import com.ecommerce.common.test.base.BaseUnitTest;
import com.ecommerce.user.domain.exception.DuplicateEmailException;
import com.ecommerce.user.domain.exception.InvalidCredentialsException;
import com.ecommerce.user.domain.exception.UserNotFoundException;
import com.ecommerce.user.domain.model.Role;
import com.ecommerce.user.domain.model.User;
import com.ecommerce.user.domain.model.UserStatus;
import com.ecommerce.user.domain.repository.UserRepository;
import com.ecommerce.user.domain.service.impl.UserDomainServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@DisplayName("UserDomainService Tests")
class UserDomainServiceTest extends BaseUnitTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDomainServiceImpl userDomainService;

    private static final String TEST_EMAIL = "test@example.com";
    private static final String TEST_PASSWORD = "Password123!";
    private static final String TEST_ENCODED_PASSWORD = "encoded_password";
    private static final String TEST_FIRST_NAME = "John";
    private static final String TEST_LAST_NAME = "Doe";

    @Nested
    @DisplayName("registerUser")
    class RegisterUser {

        @Test
        @DisplayName("should register user successfully when email is not taken")
        void shouldRegisterUserSuccessfully() {
            // Given
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(false);
            when(passwordEncoder.encode(TEST_PASSWORD)).thenReturn(TEST_ENCODED_PASSWORD);
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

            // When
            User result = userDomainService.registerUser(
                    TEST_EMAIL, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, Role.CUSTOMER
            );

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo(TEST_EMAIL);
            assertThat(result.getFirstName()).isEqualTo(TEST_FIRST_NAME);
            assertThat(result.getLastName()).isEqualTo(TEST_LAST_NAME);
            assertThat(result.getRole()).isEqualTo(Role.CUSTOMER);
            assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);

            verify(userRepository).existsByEmail(TEST_EMAIL);
            verify(passwordEncoder).encode(TEST_PASSWORD);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("should throw DuplicateEmailException when email is already taken")
        void shouldThrowExceptionWhenEmailExists() {
            // Given
            when(userRepository.existsByEmail(TEST_EMAIL)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> userDomainService.registerUser(
                    TEST_EMAIL, TEST_PASSWORD, TEST_FIRST_NAME, TEST_LAST_NAME, Role.CUSTOMER
            ))
                    .isInstanceOf(DuplicateEmailException.class);

            verify(userRepository).existsByEmail(TEST_EMAIL);
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("authenticate")
    class Authenticate {

        private User activeUser;

        @BeforeEach
        void setUp() {
            activeUser = User.builder()
                    .email(TEST_EMAIL)
                    .passwordHash(TEST_ENCODED_PASSWORD)
                    .firstName(TEST_FIRST_NAME)
                    .lastName(TEST_LAST_NAME)
                    .status(UserStatus.ACTIVE)
                    .role(Role.CUSTOMER)
                    .build();
            activeUser.setId(UUID.randomUUID());
        }

        @Test
        @DisplayName("should authenticate successfully with valid credentials")
        void shouldAuthenticateSuccessfully() {
            // Given
            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(activeUser));
            when(passwordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD)).thenReturn(true);

            // When
            Optional<User> result = userDomainService.authenticate(TEST_EMAIL, TEST_PASSWORD);

            // Then
            assertThat(result).isPresent();
            assertThat(result.get().getEmail()).isEqualTo(TEST_EMAIL);
        }

        @Test
        @DisplayName("should return empty when user not found")
        void shouldReturnEmptyWhenUserNotFound() {
            // Given
            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

            // When
            Optional<User> result = userDomainService.authenticate(TEST_EMAIL, TEST_PASSWORD);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should return empty when password does not match")
        void shouldReturnEmptyWhenPasswordDoesNotMatch() {
            // Given
            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(activeUser));
            when(passwordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD)).thenReturn(false);

            // When
            Optional<User> result = userDomainService.authenticate(TEST_EMAIL, TEST_PASSWORD);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should return empty when user is not active")
        void shouldReturnEmptyWhenUserNotActive() {
            // Given
            User inactiveUser = User.builder()
                    .email(TEST_EMAIL)
                    .passwordHash(TEST_ENCODED_PASSWORD)
                    .firstName(TEST_FIRST_NAME)
                    .lastName(TEST_LAST_NAME)
                    .status(UserStatus.SUSPENDED)
                    .role(Role.CUSTOMER)
                    .build();
            when(userRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(inactiveUser));

            // When
            Optional<User> result = userDomainService.authenticate(TEST_EMAIL, TEST_PASSWORD);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("changePassword")
    class ChangePassword {

        private User existingUser;
        private UUID userId;

        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
            existingUser = User.builder()
                    .email(TEST_EMAIL)
                    .passwordHash(TEST_ENCODED_PASSWORD)
                    .firstName(TEST_FIRST_NAME)
                    .lastName(TEST_LAST_NAME)
                    .status(UserStatus.ACTIVE)
                    .role(Role.CUSTOMER)
                    .build();
            existingUser.setId(userId);
        }

        @Test
        @DisplayName("should change password successfully")
        void shouldChangePasswordSuccessfully() {
            // Given
            String newPassword = "newPassword123";
            String newEncodedPassword = "new_encoded_password";

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD)).thenReturn(true);
            when(passwordEncoder.encode(newPassword)).thenReturn(newEncodedPassword);
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

            // When
            userDomainService.changePassword(userId, TEST_PASSWORD, newPassword);

            // Then
            verify(userRepository).findById(userId);
            verify(passwordEncoder).matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD);
            verify(passwordEncoder).encode(newPassword);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("should throw UserNotFoundException when user not found")
        void shouldThrowExceptionWhenUserNotFound() {
            // Given
            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            // When & Then
            assertThatThrownBy(() -> userDomainService.changePassword(userId, TEST_PASSWORD, "newPassword"))
                    .isInstanceOf(UserNotFoundException.class);
        }

        @Test
        @DisplayName("should throw InvalidCredentialsException when current password wrong")
        void shouldThrowExceptionWhenCurrentPasswordWrong() {
            // Given
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

            // When & Then
            assertThatThrownBy(() -> userDomainService.changePassword(userId, "wrongPassword", "newPassword"))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        @DisplayName("should throw InvalidCredentialsException when current password is same as new one")
        void shouldThrowExceptionWhenOldPasswordIsSameAsNewPassword() {
            // Given
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.matches(TEST_PASSWORD, TEST_ENCODED_PASSWORD)).thenReturn(true);

            // When & Then
            assertThatThrownBy(() -> userDomainService.changePassword(userId, TEST_PASSWORD, TEST_PASSWORD))
                    .isInstanceOf(InvalidCredentialsException.class);
        }
    }
    @Nested
    @DisplayName("retrieve user")
    class UserService{
        private User existingUser;
        private UUID userId;
        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
            existingUser = User.builder()
                    .email(TEST_EMAIL)
                    .passwordHash(TEST_ENCODED_PASSWORD)
                    .firstName(TEST_FIRST_NAME)
                    .lastName(TEST_LAST_NAME)
                    .status(UserStatus.ACTIVE)
                    .role(Role.CUSTOMER)
                    .build();
            existingUser.setId(userId);
        }

        @Test
        @DisplayName("Find the user by userId")
        void shouldReturnUserFromUserId(){
            // Given
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // When
            Optional<User> result = userDomainService.getUserById(userId);

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(existingUser);
            assertThat(result.get().getId()).isEqualTo(userId);
        }

        @Test
        @DisplayName("Find the user by email")
        void shouldReturnUserFromEmail(){
            // Given
            when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(Optional.of(existingUser));

            // When
            Optional<User> result = userDomainService.getUserByEmail(TEST_EMAIL);

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(existingUser);
            assertThat(result.get().getEmail()).isEqualTo(TEST_EMAIL);
        }
    }

    @Nested
    @DisplayName("change user status")
    class UserStatusTest{
        private User existingUser;
        private UUID userId;
        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
            existingUser = User.builder()
                    .email(TEST_EMAIL)
                    .passwordHash(TEST_ENCODED_PASSWORD)
                    .firstName(TEST_FIRST_NAME)
                    .lastName(TEST_LAST_NAME)
                    .status(UserStatus.ACTIVE)
                    .role(Role.CUSTOMER)
                    .build();
            existingUser.setId(userId);
        }

        @Test
        @DisplayName("activate user")
        void userShouldActivated(){
            // Given
            existingUser.setStatus(UserStatus.INACTIVE);
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // When
            userDomainService.activateUser(userId);

            // Then
            verify(userRepository).findById(userId);
            assertThat(existingUser.getStatus()).isEqualTo(UserStatus.ACTIVE);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("deactivate user")
        void userShouldDeactivated(){
            // Given
            existingUser.setStatus(UserStatus.ACTIVE);
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // When
            userDomainService.deactivateUser(userId);

            // Then
            verify(userRepository).findById(userId);
            assertThat(existingUser.getStatus()).isEqualTo(UserStatus.INACTIVE);
            verify(userRepository).save(any(User.class));
        }

        @Test
        @DisplayName("suspend user")
        void userShouldSuspend(){
            // When
            existingUser.setStatus(UserStatus.ACTIVE);
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // Given
            userDomainService.suspendUser(userId);

            // Then
            verify(userRepository).findById(userId);
            assertThat(existingUser.getStatus()).isEqualTo(UserStatus.SUSPENDED);
            verify(userRepository).save(any(User.class));
        }
    }

    @Nested
    @DisplayName("user profile")
    class UserProfileTest{
        private User existingUser;
        private UUID userId;
        @BeforeEach
        void setUp() {
            userId = UUID.randomUUID();
            existingUser = User.builder()
                    .email(TEST_EMAIL)
                    .passwordHash(TEST_ENCODED_PASSWORD)
                    .firstName(TEST_FIRST_NAME)
                    .lastName(TEST_LAST_NAME)
                    .status(UserStatus.ACTIVE)
                    .role(Role.CUSTOMER)
                    .build();
            existingUser.setId(userId);
        }

        @Test
        @DisplayName("update user profile")
        void profileShouldUpdate(){
            // When
            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

            // Given
            userDomainService.updateProfile(userId, "NewFirst", "NewLast", "NewPhone");

            // Then
            verify(userRepository).findById(userId);
            assertThat(existingUser.getFirstName()).isEqualTo("NewFirst");
            assertThat(existingUser.getLastName()).isEqualTo("NewLast");
            assertThat(existingUser.getPhone()).isEqualTo("NewPhone");
            verify(userRepository).save(any(User.class));

        }
    }
}
