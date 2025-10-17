package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.domain.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookValidationService using Chain of Responsibility pattern.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookValidationService Tests")
class BookValidationServiceTest {

    @Mock
    private BookRepositoryPort bookRepositoryPort;

    private BookValidationService service;

    @BeforeEach
    void setUp() {
        service = new BookValidationService(bookRepositoryPort);
    }

    @Test
    @DisplayName("Should validate and create book with valid data")
    void shouldValidateAndCreateBookWithValidData() throws ValidationException {
        Book expectedBook = new Book(1, "Clean Code", "Robert Martin",
            "No Fiction", "Physical", "Available");

        when(bookRepositoryPort.save(any(Book.class))).thenReturn(expectedBook);

        Book result = service.validateAndCreateBook(
            "Clean Code", "Robert Martin", "No Fiction", "Physical", "Available");

        assertNotNull(result);
        assertEquals("Clean Code", result.title());
        verify(bookRepositoryPort, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("Should fail validation with invalid title")
    void shouldFailValidationWithInvalidTitle() {
        assertThrows(ValidationException.class, () ->
            service.validateAndCreateBook("", "Robert Martin", "Fiction", "Physical", "Available"));

        verify(bookRepositoryPort, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should fail validation with invalid author")
    void shouldFailValidationWithInvalidAuthor() {
        assertThrows(ValidationException.class, () ->
            service.validateAndCreateBook("Clean Code", "M", "Fiction", "Physical", "Available"));

        verify(bookRepositoryPort, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should fail validation with invalid type")
    void shouldFailValidationWithInvalidType() {
        assertThrows(ValidationException.class, () ->
            service.validateAndCreateBook("Clean Code", "Martin", "InvalidType", "Physical", "Available"));

        verify(bookRepositoryPort, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should fail validation with invalid format")
    void shouldFailValidationWithInvalidFormat() {
        assertThrows(ValidationException.class, () ->
            service.validateAndCreateBook("Clean Code", "Martin", "Fiction", "InvalidFormat", "Available"));

        verify(bookRepositoryPort, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should fail validation with invalid state")
    void shouldFailValidationWithInvalidState() {
        assertThrows(ValidationException.class, () ->
            service.validateAndCreateBook("Clean Code", "Martin", "Fiction", "Physical", "InvalidState"));

        verify(bookRepositoryPort, never()).save(any(Book.class));
    }

    @Test
    @DisplayName("Should validate basic fields successfully")
    void shouldValidateBasicFieldsSuccessfully() {
        var result = service.validateBasicFields("Clean Code", "Robert Martin");

        assertTrue(result.isValid());
        assertEquals("Validation successful", result.message());
        assertNull(result.failedValidator());
    }

    @Test
    @DisplayName("Should fail basic validation with invalid title")
    void shouldFailBasicValidationWithInvalidTitle() {
        var result = service.validateBasicFields("", "Robert Martin");

        assertFalse(result.isValid());
        assertTrue(result.message().contains("Title"));
        assertNotNull(result.failedValidator());
    }

    @Test
    @DisplayName("Should fail basic validation with invalid author")
    void shouldFailBasicValidationWithInvalidAuthor() {
        var result = service.validateBasicFields("Clean Code", "M");

        assertFalse(result.isValid());
        assertTrue(result.message().contains("Author"));
        assertNotNull(result.failedValidator());
    }

    @Test
    @DisplayName("Should validate complete book successfully")
    void shouldValidateCompleteBookSuccessfully() {
        Book book = new Book(1, "Clean Code", "Robert Martin",
            "No Fiction", "Physical", "Available");

        var result = service.validateBook(book);

        assertTrue(result.isValid());
    }

    @Test
    @DisplayName("Should fail validation with null book")
    void shouldFailValidationWithNullBook() {
        var result = service.validateBook(null);

        assertFalse(result.isValid());
        assertTrue(result.message().contains("null"));
    }

    @Test
    @DisplayName("Should provide validator information")
    void shouldProvideValidatorInformation() {
        var info = service.getValidatorInfo();

        assertNotNull(info);
        assertTrue(info.containsKey("pattern"));
        assertTrue(info.containsKey("validators"));
        assertEquals("Chain of Responsibility", info.get("pattern"));
    }

    @Test
    @DisplayName("Should demonstrate validation chain")
    void shouldDemonstrateValidationChain() {
        var demo = service.demonstrateValidationChain();

        assertNotNull(demo);
        assertTrue(demo.containsKey("testCases"));
        assertTrue(demo.containsKey("totalTests"));
        assertTrue(demo.containsKey("passed"));
        assertTrue(demo.containsKey("failed"));
    }
}

