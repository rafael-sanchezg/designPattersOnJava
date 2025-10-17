package com.pocs.designpatterns.designpattersonjava.domain.validator;

/**
 * Concrete validator for book titles in the Chain of Responsibility.
 * Validates that the title is not null, not empty, and meets minimum length requirements.
 */
public class TitleValidator extends BookValidator {

    private static final int MIN_TITLE_LENGTH = 1;
    private static final int MAX_TITLE_LENGTH = 255;

    @Override
    protected void doValidate(String title, String author) throws ValidationException {
        // Check if title is null
        if (title == null) {
            throw new ValidationException("Title cannot be null", getValidatorName());
        }

        // Check if title is empty or blank
        if (title.trim().isEmpty()) {
            throw new ValidationException("Title cannot be empty or blank", getValidatorName());
        }

        // Check minimum length
        if (title.trim().length() < MIN_TITLE_LENGTH) {
            throw new ValidationException(
                String.format("Title must be at least %d character(s) long", MIN_TITLE_LENGTH),
                getValidatorName()
            );
        }

        // Check maximum length
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new ValidationException(
                String.format("Title cannot exceed %d characters", MAX_TITLE_LENGTH),
                getValidatorName()
            );
        }

        // Check for invalid characters (only basic validation)
        if (title.matches(".*[<>{}\\[\\]].*")) {
            throw new ValidationException(
                "Title contains invalid characters: < > { } [ ]",
                getValidatorName()
            );
        }
    }

    @Override
    public String getValidatorName() {
        return "TitleValidator";
    }
}

