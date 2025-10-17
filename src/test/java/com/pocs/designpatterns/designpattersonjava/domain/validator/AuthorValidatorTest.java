package com.pocs.designpatterns.designpattersonjava.domain.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for AuthorValidator.
 */
@DisplayName("AuthorValidator Tests")
class AuthorValidatorTest {

    private AuthorValidator validator;

    @BeforeEach
    void setUp() {
        validator = new AuthorValidator();
    }

    @Test
    @DisplayName("Should pass validation for valid author")
    void shouldPassValidationForValidAuthor() {
        assertDoesNotThrow(() -> validator.validate("Clean Code", "Robert Martin"));
    }

    @Test
    @DisplayName("Should fail when author is null")
    void shouldFailWhenAuthorIsNull() {
        ValidationException exception = assertThrows(ValidationException.class,
            () -> validator.validate("Clean Code", null));

        assertTrue(exception.getMessage().contains("Author cannot be null"));
        assertEquals("AuthorValidator", exception.getValidatorName());
    }

    @Test
    @DisplayName("Should fail when author is empty")
    void shouldFailWhenAuthorIsEmpty() {
        ValidationException exception = assertThrows(ValidationException.class,
            () -> validator.validate("Clean Code", ""));

        assertTrue(exception.getMessage().contains("Author cannot be empty"));
    }

    @Test
    @DisplayName("Should fail when author is too short")
    void shouldFailWhenAuthorIsTooShort() {
        ValidationException exception = assertThrows(ValidationException.class,
            () -> validator.validate("Clean Code", "M"));

        assertTrue(exception.getMessage().contains("at least 2 characters"));
    }

    @Test
    @DisplayName("Should fail when author exceeds maximum length")
    void shouldFailWhenAuthorExceedsMaxLength() {
        String longAuthor = "A".repeat(256);

        ValidationException exception = assertThrows(ValidationException.class,
            () -> validator.validate("Clean Code", longAuthor));

        assertTrue(exception.getMessage().contains("cannot exceed 255 characters"));
    }

    @Test
    @DisplayName("Should fail when author contains invalid characters")
    void shouldFailWhenAuthorContainsInvalidCharacters() {
        ValidationException exception = assertThrows(ValidationException.class,
            () -> validator.validate("Clean Code", "Robert@Martin"));

        assertTrue(exception.getMessage().contains("invalid characters"));
    }

    @Test
    @DisplayName("Should fail when author contains numbers")
    void shouldFailWhenAuthorContainsNumbers() {
        ValidationException exception = assertThrows(ValidationException.class,
            () -> validator.validate("Clean Code", "Robert123"));

        assertTrue(exception.getMessage().contains("invalid characters"));
    }

    @Test
    @DisplayName("Should fail when author is only special characters")
    void shouldFailWhenAuthorIsOnlySpecialCharacters() {
        ValidationException exception = assertThrows(ValidationException.class,
            () -> validator.validate("Clean Code", ".-'"));

        assertTrue(exception.getMessage().contains("at least one letter"));
    }

    @Test
    @DisplayName("Should pass for author with dots")
    void shouldPassForAuthorWithDots() {
        assertDoesNotThrow(() -> validator.validate("The Hobbit", "J.R.R. Tolkien"));
    }

    @Test
    @DisplayName("Should pass for author with hyphen")
    void shouldPassForAuthorWithHyphen() {
        assertDoesNotThrow(() -> validator.validate("Being and Time", "Martin Heidegger"));
        assertDoesNotThrow(() -> validator.validate("Nausea", "Jean-Paul Sartre"));
    }

    @Test
    @DisplayName("Should pass for author with apostrophe")
    void shouldPassForAuthorWithApostrophe() {
        assertDoesNotThrow(() -> validator.validate("The Kite Runner", "Khaled Hosseini"));
        assertDoesNotThrow(() -> validator.validate("Angela's Ashes", "Frank McCourt"));
    }

    @Test
    @DisplayName("Should return correct validator name")
    void shouldReturnCorrectValidatorName() {
        assertEquals("AuthorValidator", validator.getValidatorName());
    }
}

