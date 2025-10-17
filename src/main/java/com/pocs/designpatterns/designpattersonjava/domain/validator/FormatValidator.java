package com.pocs.designpatterns.designpattersonjava.domain.validator;

/**
 * Concrete validator for book format in the Chain of Responsibility.
 * Validates that the format is either "Physical" or "Digital".
 */
public class FormatValidator extends BookValidator {

    private static final String PHYSICAL_FORMAT = "Physical";
    private static final String DIGITAL_FORMAT = "Digital";

    @Override
    protected void doValidate(String title, String author) throws ValidationException {
        // This validator doesn't use title or author, but needs access to format
        // This is a limitation of the current design - we'll handle it in the service layer
        // For now, this validator is a placeholder that can be extended
    }

    /**
     * Validates the book format.
     *
     * @param format the format to validate
     * @throws ValidationException if validation fails
     */
    public void validateFormat(String format) throws ValidationException {
        if (format == null) {
            throw new ValidationException("Format cannot be null", getValidatorName());
        }

        if (format.trim().isEmpty()) {
            throw new ValidationException("Format cannot be empty", getValidatorName());
        }

        if (!PHYSICAL_FORMAT.equalsIgnoreCase(format) && !DIGITAL_FORMAT.equalsIgnoreCase(format)) {
            throw new ValidationException(
                String.format("Format must be either '%s' or '%s'", PHYSICAL_FORMAT, DIGITAL_FORMAT),
                getValidatorName()
            );
        }
    }

    @Override
    public String getValidatorName() {
        return "FormatValidator";
    }
}

