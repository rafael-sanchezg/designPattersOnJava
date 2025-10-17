package com.pocs.designpatterns.designpattersonjava.infrastructure.adapters.web;

import com.pocs.designpatterns.designpattersonjava.application.services.BookValidationService;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.domain.validator.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for demonstrating the Chain of Responsibility pattern.
 * Provides endpoints for validating book data using validation chains.
 */
@RestController
@RequestMapping("/books/validation")
public class BookValidationController {

    private final BookValidationService bookValidationService;

    public BookValidationController(BookValidationService bookValidationService) {
        this.bookValidationService = bookValidationService;
    }

    /**
     * Validates and creates a new book using the complete validation chain.
     *
     * @param title the book title
     * @param author the book author
     * @param type the book type
     * @param format the book format
     * @param state the book state
     * @return the created book or validation error
     */
    @PostMapping("/create")
    public ResponseEntity<?> validateAndCreateBook(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String type,
            @RequestParam String format,
            @RequestParam String state) {

        try {
            Book book = bookValidationService.validateAndCreateBook(title, author, type, format, state);

            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Book validated and created successfully",
                "book", book
            ));
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false,
                "error", e.getMessage(),
                "validator", e.getValidatorName()
            ));
        }
    }

    /**
     * Validates only title and author using basic validation chain.
     *
     * @param title the book title
     * @param author the book author
     * @return validation result
     */
    @GetMapping("/validate-basic")
    public ResponseEntity<Map<String, Object>> validateBasicFields(
            @RequestParam String title,
            @RequestParam String author) {

        BookValidationService.ValidationResult result =
            bookValidationService.validateBasicFields(title, author);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", result.isValid());
        response.put("message", result.message());
        response.put("title", title);
        response.put("author", author);

        if (!result.isValid()) {
            response.put("failedValidator", result.failedValidator());
        }

        HttpStatus status = result.isValid() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Validates a complete book using the full validation chain.
     *
     * @param book the book to validate
     * @return validation result
     */
    @PostMapping("/validate-book")
    public ResponseEntity<Map<String, Object>> validateBook(@RequestBody BookRequest book) {
        Book bookEntity = new Book(
            0,
            book.title(),
            book.author(),
            book.type(),
            book.format(),
            book.state()
        );

        BookValidationService.ValidationResult result =
            bookValidationService.validateBook(bookEntity);

        Map<String, Object> response = new HashMap<>();
        response.put("valid", result.isValid());
        response.put("message", result.message());
        response.put("book", bookEntity);

        if (!result.isValid()) {
            response.put("failedValidator", result.failedValidator());
        }

        HttpStatus status = result.isValid() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(response);
    }

    /**
     * Demonstrates the validation chain with various test cases.
     *
     * @return demonstration results
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demonstrateValidationChain() {
        Map<String, Object> results = bookValidationService.demonstrateValidationChain();
        return ResponseEntity.ok(results);
    }

    /**
     * Gets information about the Chain of Responsibility pattern implementation.
     *
     * @return pattern information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getValidatorInfo() {
        Map<String, Object> info = bookValidationService.getValidatorInfo();

        info.put("endpoints", Map.of(
            "create", "POST /books/validation/create - Validate and create book",
            "validateBasic", "GET /books/validation/validate-basic?title=...&author=...",
            "validateBook", "POST /books/validation/validate-book - Validate complete book",
            "demo", "GET /books/validation/demo - Demonstrate validation chain",
            "info", "GET /books/validation/info - Get validator information"
        ));

        return ResponseEntity.ok(info);
    }

    /**
     * Request DTO for book validation.
     */
    public record BookRequest(
        String title,
        String author,
        String type,
        String format,
        String state
    ) {}
}

