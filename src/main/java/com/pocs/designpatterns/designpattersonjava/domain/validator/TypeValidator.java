package com.pocs.designpatterns.designpattersonjava.domain.validator;

/**
 * Concrete validator for book type in the Chain of Responsibility.
 * Validates that the type is either "Fiction" or "No Fiction".
 */
public class TypeValidator extends BookValidator {

    private static final String FICTION_TYPE = "Fiction";
    private static final String NON_FICTION_TYPE = "No Fiction";

    @Override
    protected void doValidate(String title, String author) throws ValidationException {
        // This validator doesn't use title or author, but needs access to type
        // This is handled in the service layer
    }

    /**
     * Validates the book type.
     *
     * @param type the type to validate
     * @throws ValidationException if validation fails
     */
    public void validateType(String type) throws ValidationException {
        if (type == null) {
            throw new ValidationException("Type cannot be null", getValidatorName());
        }

        if (type.trim().isEmpty()) {
            throw new ValidationException("Type cannot be empty", getValidatorName());
        }

        if (!FICTION_TYPE.equalsIgnoreCase(type) && !NON_FICTION_TYPE.equalsIgnoreCase(type)) {
            throw new ValidationException(
                String.format("Type must be either '%s' or '%s'", FICTION_TYPE, NON_FICTION_TYPE),
                getValidatorName()
            );
        }
    }

    @Override
    public String getValidatorName() {
        return "TypeValidator";
    }
}

