package com.pocs.designpatterns.designpattersonjava.domain.factory;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Factory Provider that combines Factory Method and Abstract Factory patterns.
 * This class provides a centralized way to access different book factories
 * based on the book type (Fiction/Non-Fiction).
 */
public final class BookFactoryProvider {

    // Private constructor to prevent instantiation
    private BookFactoryProvider() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Gets a Factory Method implementation based on book type.
     *
     * @param bookType the type of book ("Fiction" or "No Fiction")
     * @return appropriate BookFactory implementation
     * @throws IllegalArgumentException if book type is not supported
     */
    public static BookFactory getBookFactory(String bookType) {
        return switch (bookType) {
            case "Fiction" -> new FictionBookFactory();
            case "No Fiction" -> new NonFictionBookFactory();
            default -> throw new IllegalArgumentException("Unsupported book type: " + bookType);
        };
    }

    /**
     * Gets an Abstract Factory implementation based on book type.
     *
     * @param bookType the type of book ("Fiction" or "No Fiction")
     * @return appropriate BookAbstractFactory implementation
     * @throws IllegalArgumentException if book type is not supported
     */
    public static BookAbstractFactory getAbstractFactory(String bookType) {
        return switch (bookType) {
            case "Fiction" -> new FictionBookAbstractFactory();
            case "No Fiction" -> new NonFictionBookAbstractFactory();
            default -> throw new IllegalArgumentException("Unsupported book type: " + bookType);
        };
    }

    /**
     * Convenience method to create a book using Factory Method pattern.
     *
     * @param bookType the type of book
     * @param id the book identifier
     * @param title the book title
     * @param author the book author
     * @param format the book format
     * @param state the book state
     * @return a new Book instance
     */
    public static Book createBook(
            String bookType, int id, String title, String author, String format, String state) {
        BookFactory factory = getBookFactory(bookType);
        return factory.processBookCreation(id, title, author, format, state);
    }

    /**
     * Convenience method to create a book using Abstract Factory pattern.
     *
     * @param bookType the type of book
     * @param format the book format ("Physical" or "Digital")
     * @param id the book identifier
     * @param title the book title
     * @param author the book author
     * @param state the book state
     * @return a new Book instance
     */
    public static Book createBookWithFormat(
            String bookType, String format, int id, String title, String author, String state) {
        BookAbstractFactory factory = getAbstractFactory(bookType);

        return switch (format) {
            case "Physical" -> factory.createPhysicalBook(id, title, author, state);
            case "Digital" -> factory.createDigitalBook(id, title, author, state);
            default -> throw new IllegalArgumentException("Unsupported book format: " + format);
        };
    }
}
