package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.domain.validator.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for validating books using the Chain of Responsibility pattern.
 * Demonstrates how validation chains process book data sequentially.
 */
@Service
public class BookValidationService {

    private final BookRepositoryPort bookRepositoryPort;
    private final BookValidator completeValidationChain;
    private final BookValidator basicValidationChain;

    public BookValidationService(BookRepositoryPort bookRepositoryPort) {
        this.bookRepositoryPort = bookRepositoryPort;

        // Initialize validation chains
        this.completeValidationChain = ValidationChainFactory.createCompleteValidationChain();
        this.basicValidationChain = ValidationChainFactory.createBasicValidationChain();
    }

    /**
     * Validates and creates a new book using the complete validation chain.
     *
     * @param title the book title
     * @param author the book author
     * @param type the book type
     * @param format the book format
     * @param state the book state
     * @return the created book
     * @throws ValidationException if validation fails
     */
    public Book validateAndCreateBook(String title, String author, String type,
                                     String format, String state) throws ValidationException {
        // Run through basic validation chain (title and author)
        completeValidationChain.validate(title, author);

        // Validate type
        TypeValidator typeValidator = new TypeValidator();
        typeValidator.validateType(type);

        // Validate format
        FormatValidator formatValidator = new FormatValidator();
        formatValidator.validateFormat(format);

        // Validate state
        StateValidator stateValidator = new StateValidator();
        stateValidator.validateState(state);

        // If all validations pass, create and save the book
        Book book = new Book(0, title, author, type, format, state);
        return bookRepositoryPort.save(book);
    }

    /**
     * Validates only title and author using basic validation chain.
     *
     * @param title the book title
     * @param author the book author
     * @return validation result
     */
    public ValidationResult validateBasicFields(String title, String author) {
        try {
            basicValidationChain.validate(title, author);
            return new ValidationResult(true, "Validation successful", null);
        } catch (ValidationException e) {
            return new ValidationResult(false, e.getMessage(), e.getValidatorName());
        }
    }

    /**
     * Validates a complete book object.
     *
     * @param book the book to validate
     * @return validation result
     */
    public ValidationResult validateBook(Book book) {
        try {
            if (book == null) {
                throw new ValidationException("Book cannot be null");
            }

            completeValidationChain.validate(book.title(), book.author());

            new TypeValidator().validateType(book.type());
            new FormatValidator().validateFormat(book.format());
            new StateValidator().validateState(book.state());

            return new ValidationResult(true, "Book validation successful", null);
        } catch (ValidationException e) {
            return new ValidationResult(false, e.getMessage(), e.getValidatorName());
        }
    }

    /**
     * Validates a list of books and returns results for each.
     *
     * @param books the list of books to validate
     * @return list of validation results
     */
    public List<ValidationResult> validateBooks(List<Book> books) {
        List<ValidationResult> results = new ArrayList<>();

        for (Book book : books) {
            results.add(validateBook(book));
        }

        return results;
    }

    /**
     * Demonstrates the validation chain by attempting to validate with intentional errors.
     *
     * @return demonstration results
     */
    public Map<String, Object> demonstrateValidationChain() {
        Map<String, Object> results = new HashMap<>();
        List<Map<String, Object>> testCases = new ArrayList<>();

        // Test Case 1: Valid book
        testCases.add(testValidation("Clean Code", "Robert Martin", "Valid Book"));

        // Test Case 2: Empty title
        testCases.add(testValidation("", "Robert Martin", "Empty Title"));

        // Test Case 3: Null title
        testCases.add(testValidation(null, "Robert Martin", "Null Title"));

        // Test Case 4: Invalid title (too long)
        String longTitle = "A".repeat(300);
        testCases.add(testValidation(longTitle, "Robert Martin", "Title Too Long"));

        // Test Case 5: Invalid title characters
        testCases.add(testValidation("Clean Code <script>", "Robert Martin", "Invalid Title Characters"));

        // Test Case 6: Valid title, empty author
        testCases.add(testValidation("Clean Code", "", "Empty Author"));

        // Test Case 7: Valid title, null author
        testCases.add(testValidation("Clean Code", null, "Null Author"));

        // Test Case 8: Valid title, short author
        testCases.add(testValidation("Clean Code", "M", "Author Too Short"));

        // Test Case 9: Valid title, invalid author characters
        testCases.add(testValidation("Clean Code", "Robert@Martin123", "Invalid Author Characters"));

        // Test Case 10: Both valid
        testCases.add(testValidation("Design Patterns", "Gang of Four", "Both Valid"));

        results.put("testCases", testCases);
        results.put("totalTests", testCases.size());
        results.put("passed", testCases.stream().filter(tc -> (Boolean) tc.get("valid")).count());
        results.put("failed", testCases.stream().filter(tc -> !(Boolean) tc.get("valid")).count());

        return results;
    }

    /**
     * Gets information about available validators in the chain.
     *
     * @return validator information
     */
    public Map<String, Object> getValidatorInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("pattern", "Chain of Responsibility");
        info.put("description", "Validates book data through a chain of validators");

        List<Map<String, String>> validators = List.of(
            Map.of(
                "name", "TitleValidator",
                "validates", "Book title",
                "rules", "Not null, not empty, 1-255 characters, no invalid characters"
            ),
            Map.of(
                "name", "AuthorValidator",
                "validates", "Book author",
                "rules", "Not null, not empty, 2-255 characters, only letters, spaces, dots, hyphens, apostrophes"
            ),
            Map.of(
                "name", "TypeValidator",
                "validates", "Book type",
                "rules", "Must be 'Fiction' or 'No Fiction'"
            ),
            Map.of(
                "name", "FormatValidator",
                "validates", "Book format",
                "rules", "Must be 'Physical' or 'Digital'"
            ),
            Map.of(
                "name", "StateValidator",
                "validates", "Book state",
                "rules", "Must be 'Available' or 'Loaned'"
            )
        );

        info.put("validators", validators);
        info.put("chains", Map.of(
            "complete", "Title → Author → Type → Format → State",
            "basic", "Title → Author",
            "custom", "Can be configured with any combination"
        ));

        return info;
    }

    // Helper methods

    private Map<String, Object> testValidation(String title, String author, String testName) {
        Map<String, Object> result = new HashMap<>();
        result.put("testName", testName);
        result.put("title", title);
        result.put("author", author);

        ValidationResult validationResult = validateBasicFields(title, author);
        result.put("valid", validationResult.isValid());
        result.put("message", validationResult.message());
        result.put("failedValidator", validationResult.failedValidator());

        return result;
    }

    /**
     * Record to represent validation results.
     */
    public record ValidationResult(
        boolean isValid,
        String message,
        String failedValidator
    ) {}
}
