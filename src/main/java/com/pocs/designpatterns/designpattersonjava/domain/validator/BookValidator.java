package com.pocs.designpatterns.designpattersonjava.domain.validator;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Abstract handler in the Chain of Responsibility pattern for book validation.
 * Each validator in the chain can either handle the validation or pass it to the next validator.
 */
public abstract class BookValidator {

    protected BookValidator nextValidator;

    /**
     * Sets the next validator in the chain.
     *
     * @param nextValidator the next validator to call if this one passes
     * @return the next validator for fluent chaining
     */
    public BookValidator setNext(BookValidator nextValidator) {
        this.nextValidator = nextValidator;
        return nextValidator;
    }

    /**
     * Validates the book data. If validation passes and there's a next validator,
     * the request is forwarded to the next validator in the chain.
     *
     * @param title the book title to validate
     * @param author the book author to validate
     * @throws ValidationException if validation fails
     */
    public void validate(String title, String author) throws ValidationException {
        doValidate(title, author);

        if (nextValidator != null) {
            nextValidator.validate(title, author);
        }
    }

    /**
     * Validates a complete Book object.
     *
     * @param book the book to validate
     * @throws ValidationException if validation fails
     */
    public void validate(Book book) throws ValidationException {
        if (book == null) {
            throw new ValidationException("Book cannot be null");
        }
        validate(book.title(), book.author());
    }

    /**
     * Abstract method that each concrete validator must implement.
     * Contains the specific validation logic for each validator.
     *
     * @param title the book title to validate
     * @param author the book author to validate
     * @throws ValidationException if validation fails
     */
    protected abstract void doValidate(String title, String author) throws ValidationException;

    /**
     * Returns the name of this validator for logging and debugging purposes.
     *
     * @return validator name
     */
    public abstract String getValidatorName();
}

