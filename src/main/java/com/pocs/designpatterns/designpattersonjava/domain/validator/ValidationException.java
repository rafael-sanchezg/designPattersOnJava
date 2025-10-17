package com.pocs.designpatterns.designpattersonjava.domain.validator;

/**
 * Custom exception for validation errors in the Chain of Responsibility pattern.
 */
public class ValidationException extends Exception {

    private final String validatorName;

    public ValidationException(String message) {
        super(message);
        this.validatorName = "Unknown";
    }

    public ValidationException(String message, String validatorName) {
        super(message);
        this.validatorName = validatorName;
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.validatorName = "Unknown";
    }

    public ValidationException(String message, String validatorName, Throwable cause) {
        super(message, cause);
        this.validatorName = validatorName;
    }

    public String getValidatorName() {
        return validatorName;
    }

    @Override
    public String getMessage() {
        return String.format("[%s] %s", validatorName, super.getMessage());
    }
}

