package com.pocs.designpatterns.designpattersonjava.domain.factory;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Factory Method pattern for creating books.
 * This abstract class defines the factory method that subclasses will implement
 * to create specific types of books.
 */
public abstract class BookFactory {

    /**
     * Factory method to create a book.
     * Subclasses will implement this method to create specific book types.
     *
     * @param id the book identifier
     * @param title the book title
     * @param author the book author
     * @param format the book format (Physical/Digital)
     * @param state the book state (Available/Loaned)
     * @return a new Book instance
     */
    public abstract Book createBook(int id, String title, String author, String format, String state);

    /**
     * Template method that uses the factory method.
     * This method provides common logic and delegates the creation to the factory method.
     *
     * @param id the book identifier
     * @param title the book title
     * @param author the book author
     * @param format the book format
     * @param state the book state
     * @return a configured Book instance
     */
    public final Book processBookCreation(int id, String title, String author, String format, String state) {
        // Common validation logic
        validateBookData(title, author, format, state);

        // Delegate to factory method
        Book book = createBook(id, title, author, format, state);

        // Additional processing if needed
        logBookCreation(book);

        return book;
    }

    private void validateBookData(String title, String author, String format, String state) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be null or empty");
        }
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Book author cannot be null or empty");
        }
        if (!isValidFormat(format)) {
            throw new IllegalArgumentException("Invalid book format: " + format);
        }
        if (!isValidState(state)) {
            throw new IllegalArgumentException("Invalid book state: " + state);
        }
    }

    private boolean isValidFormat(String format) {
        return "Physical".equals(format) || "Digital".equals(format);
    }

    private boolean isValidState(String state) {
        return "Available".equals(state) || "Loaned".equals(state);
    }

    private void logBookCreation(Book book) {
        System.out.println("Created book: " + book.title() + " by " + book.author() +
                          " [Type: " + book.type() + ", Format: " + book.format() + "]");
    }
}
