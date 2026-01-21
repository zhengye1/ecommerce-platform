package com.ecommerce.user.application.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for PasswordValidator.
 *
 * Password rules:
 * - Length: 8-12 characters
 * - At least 1 uppercase letter
 * - At least 1 lowercase letter
 * - At least 1 digit
 * - At least 1 special character (!@#$%^&*()_+-=[]{}|;:,.<>?)
 */
@DisplayName("PasswordValidator Tests")
class PasswordValidatorTest {

    private PasswordValidator validator;

    @BeforeEach
    void setUp() {
        validator = new PasswordValidator();
    }

    // ========================================
    // Valid Password Tests
    // ========================================
    @Nested
    @DisplayName("Valid passwords")
    class ValidPasswords {

        @ParameterizedTest
        @DisplayName("should accept valid passwords")
        @ValueSource(strings = {
                "Pass@123",      // 8 chars - minimum length
                "Pass@12345",    // 10 chars - middle
                "Pass@1234567",  // 12 chars - maximum length
                "Aa1!Aa1!",      // 8 chars with repeating pattern
                "Test#9876",     // different special char
                "Xy$45678ab",    // special char in middle
                "abcDEF1@2"      // mixed positions
        })
        void shouldAcceptValidPasswords(String password) {
            assertThat(validator.isValid(password, null)).isTrue();
        }
    }

    // ========================================
    // Invalid Length Tests
    // ========================================
    @Nested
    @DisplayName("Invalid length")
    class InvalidLength {

        @ParameterizedTest
        @DisplayName("should reject passwords shorter than 8 characters")
        @ValueSource(strings = {
                "Aa1@",         // 4 chars
                "Aa1@567",      // 7 chars
                "A1@abcd"       // 7 chars
        })
        void shouldRejectTooShort(String password) {
            assertThat(validator.isValid(password, null)).isFalse();
        }

        @ParameterizedTest
        @DisplayName("should reject passwords longer than 12 characters")
        @ValueSource(strings = {
                "Pass@12345678",    // 13 chars
                "Pass@123456789",   // 14 chars
                "VeryLongPass@1"    // 14 chars
        })
        void shouldRejectTooLong(String password) {
            assertThat(validator.isValid(password, null)).isFalse();
        }
    }

    // ========================================
    // Missing Character Type Tests
    // ========================================
    @Nested
    @DisplayName("Missing required characters")
    class MissingCharacters {

        @Test
        @DisplayName("should reject password without uppercase")
        void shouldRejectNoUppercase() {
            assertThat(validator.isValid("pass@123", null)).isFalse();
        }

        @Test
        @DisplayName("should reject password without lowercase")
        void shouldRejectNoLowercase() {
            assertThat(validator.isValid("PASS@123", null)).isFalse();
        }

        @Test
        @DisplayName("should reject password without digit")
        void shouldRejectNoDigit() {
            assertThat(validator.isValid("Pass@abc", null)).isFalse();
        }

        @Test
        @DisplayName("should reject password without special character")
        void shouldRejectNoSpecialChar() {
            assertThat(validator.isValid("Pass1234", null)).isFalse();
        }
    }

    // ========================================
    // Null/Empty Tests
    // ========================================
    @Nested
    @DisplayName("Null and empty")
    class NullAndEmpty {

        @ParameterizedTest
        @NullSource
        @DisplayName("should reject null password")
        void shouldRejectNull(String password) {
            assertThat(validator.isValid(password, null)).isFalse();
        }

        @Test
        @DisplayName("should reject empty password")
        void shouldRejectEmpty() {
            assertThat(validator.isValid("", null)).isFalse();
        }
    }

    // ========================================
    // Special Character Coverage Tests
    // ========================================
    @Nested
    @DisplayName("Special character variations")
    class SpecialCharacters {

        @ParameterizedTest
        @DisplayName("should accept various special characters")
        @ValueSource(strings = {
                "Pass!123",     // !
                "Pass@123",     // @
                "Pass#123",     // #
                "Pass$123",     // $
                "Pass%123",     // %
                "Pass^123",     // ^
                "Pass&123",     // &
                "Pass*123",     // *
                "Pass(123)",    // ()
                "Pass_123",     // _
                "Pass+123",     // +
                "Pass-1234",    // -
                "Pass=1234",    // =
                "Pass[123]",    // []
                "Pass{123}",    // {}
                "Pass|1234",    // |
                "Pass;1234",    // ;
                "Pass:1234",    // :
                "Pass,1234",    // ,
                "Pass.1234",    // .
                "Pass<123>",    // <>
                "Pass?1234"     // ?
        })
        void shouldAcceptVariousSpecialChars(String password) {
            assertThat(validator.isValid(password, null)).isTrue();
        }
    }
}
