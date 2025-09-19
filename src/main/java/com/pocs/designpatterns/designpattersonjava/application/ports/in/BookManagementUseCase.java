package com.pocs.designpatterns.designpattersonjava.application.ports.in;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

import java.util.List;

/**
 * Input port for book management operations.
 * Defines the contract for book-related use cases.
 */
public interface BookManagementUseCase {

    /**
     * Retrieves all books from the repository.
     *
     * @return list of all books
     */
    List<Book> getAllBooks();

    /**
     * Creates books using Factory Method pattern.
     *
     * @return list of books created with different factories
     */
    List<Book> createBooksWithFactoryMethod();

    /**
     * Creates books using Abstract Factory pattern.
     *
     * @return list of books created with abstract factories
     */
    List<Book> createBooksWithAbstractFactory();

    /**
     * Creates books using convenience methods.
     *
     * @return list of books created with convenience methods
     */
    List<Book> createBooksWithConvenienceMethods();

    /**
     * Creates a book dynamically based on factory type.
     *
     * @param bookType the type of book
     * @param useAbstractFactory whether to use abstract factory
     * @return created book
     */
    Book createBookDynamically(String bookType, boolean useAbstractFactory);

    /**
     * Creates a book using Factory Method pattern.
     *
     * @param bookType the book type
     * @param title the book title
     * @param author the book author
     * @param format the book format
     * @param state the book state
     * @return created book
     */
    Book createBookWithFactoryMethod(String bookType, String title, String author, String format, String state);

    /**
     * Creates a book using Abstract Factory pattern.
     *
     * @param bookType the book type
     * @param format the book format
     * @param title the book title
     * @param author the book author
     * @param state the book state
     * @return created book
     */
    Book createBookWithAbstractFactory(String bookType, String format, String title, String author, String state);
}
