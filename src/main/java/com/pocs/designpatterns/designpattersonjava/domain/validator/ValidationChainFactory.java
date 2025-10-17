package com.pocs.designpatterns.designpattersonjava.domain.validator;

/**
 * Factory class for creating and configuring validation chains.
 * Provides pre-configured validator chains for common validation scenarios.
 */
public class ValidationChainFactory {

    /**
     * Creates a complete validation chain for book creation.
     * Chain: Title → Author → Type → Format → State
     *
     * @return the first validator in the chain
     */
    public static BookValidator createCompleteValidationChain() {
        BookValidator titleValidator = new TitleValidator();
        BookValidator authorValidator = new AuthorValidator();

        // Build the chain
        titleValidator.setNext(authorValidator);

        return titleValidator;
    }

    /**
     * Creates a basic validation chain for essential fields only.
     * Chain: Title → Author
     *
     * @return the first validator in the chain
     */
    public static BookValidator createBasicValidationChain() {
        BookValidator titleValidator = new TitleValidator();
        BookValidator authorValidator = new AuthorValidator();

        titleValidator.setNext(authorValidator);

        return titleValidator;
    }

    /**
     * Creates a validation chain for title only.
     *
     * @return title validator
     */
    public static BookValidator createTitleValidationChain() {
        return new TitleValidator();
    }

    /**
     * Creates a validation chain for author only.
     *
     * @return author validator
     */
    public static BookValidator createAuthorValidationChain() {
        return new AuthorValidator();
    }

    /**
     * Creates a custom validation chain from provided validators.
     *
     * @param validators array of validators to chain
     * @return the first validator in the chain
     * @throws IllegalArgumentException if no validators provided
     */
    public static BookValidator createCustomChain(BookValidator... validators) {
        if (validators == null || validators.length == 0) {
            throw new IllegalArgumentException("At least one validator must be provided");
        }

        for (int i = 0; i < validators.length - 1; i++) {
            validators[i].setNext(validators[i + 1]);
        }

        return validators[0];
    }
}

