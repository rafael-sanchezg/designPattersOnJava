package com.pocs.designpatterns.designpattersonjava.domain.validator;

/**
 * Concrete validator for book state in the Chain of Responsibility.
 * Validates that the state is either "Available" or "Loaned".
 */
public class StateValidator extends BookValidator {

    private static final String AVAILABLE_STATE = "Available";
    private static final String LOANED_STATE = "Loaned";

    @Override
    protected void doValidate(String title, String author) throws ValidationException {
        // This validator doesn't use title or author, but needs access to state
        // This is handled in the service layer
    }

    /**
     * Validates the book state.
     *
     * @param state the state to validate
     * @throws ValidationException if validation fails
     */
    public void validateState(String state) throws ValidationException {
        if (state == null) {
            throw new ValidationException("State cannot be null", getValidatorName());
        }

        if (state.trim().isEmpty()) {
            throw new ValidationException("State cannot be empty", getValidatorName());
        }

        if (!AVAILABLE_STATE.equalsIgnoreCase(state) && !LOANED_STATE.equalsIgnoreCase(state)) {
            throw new ValidationException(
                String.format("State must be either '%s' or '%s'", AVAILABLE_STATE, LOANED_STATE),
                getValidatorName()
            );
        }
    }

    @Override
    public String getValidatorName() {
        return "StateValidator";
    }
}

