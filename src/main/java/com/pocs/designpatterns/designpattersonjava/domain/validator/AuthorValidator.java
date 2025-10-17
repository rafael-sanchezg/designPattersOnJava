package com.pocs.designpatterns.designpattersonjava.domain.validator;

import java.util.regex.Pattern;

/**
 * Concrete validator for book authors in the Chain of Responsibility.
 * Validates that the author is not null, not empty, and follows proper naming conventions.
 */
public class AuthorValidator extends BookValidator {

    private static final int MIN_AUTHOR_LENGTH = 2;
    private static final int MAX_AUTHOR_LENGTH = 255;
    private static final Pattern VALID_AUTHOR_PATTERN = Pattern.compile("^[a-zA-Z\\s.'-]+$");

    @Override
    protected void doValidate(String title, String author) throws ValidationException {
        // Check if author is null
        if (author == null) {
            throw new ValidationException("Author cannot be null", getValidatorName());
        }

        // Check if author is empty or blank
        if (author.trim().isEmpty()) {
            throw new ValidationException("Author cannot be empty or blank", getValidatorName());
        }

        // Check minimum length
        if (author.trim().length() < MIN_AUTHOR_LENGTH) {
            throw new ValidationException(
                String.format("Author name must be at least %d characters long", MIN_AUTHOR_LENGTH),
                getValidatorName()
            );
        }

        // Check maximum length
        if (author.length() > MAX_AUTHOR_LENGTH) {
            throw new ValidationException(
                String.format("Author name cannot exceed %d characters", MAX_AUTHOR_LENGTH),
                getValidatorName()
            );
        }

        // Check for valid characters (letters, spaces, dots, hyphens, apostrophes)
        if (!VALID_AUTHOR_PATTERN.matcher(author.trim()).matches()) {
            throw new ValidationException(
                "Author name contains invalid characters. Only letters, spaces, dots, hyphens, and apostrophes are allowed",
                getValidatorName()
            );
        }

        // Check that author doesn't consist only of special characters
        if (author.trim().matches("^[.'-\\s]+$")) {
            throw new ValidationException(
                "Author name must contain at least one letter",
                getValidatorName()
            );
        }
    }

    @Override
    public String getValidatorName() {
        return "AuthorValidator";
    }
}

