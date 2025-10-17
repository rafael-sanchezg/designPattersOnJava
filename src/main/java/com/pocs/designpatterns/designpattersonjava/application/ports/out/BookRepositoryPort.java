package com.pocs.designpatterns.designpattersonjava.application.ports.out;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

import java.util.List;

/**
 * Output port for book repository operations.
 * Defines the contract that adapters must implement for data persistence.
 */
public interface BookRepositoryPort {

    /**
     * Finds all books in the repository.
     *
     * @return list of all books
     */
    List<Book> findAll();

    /**
     * Saves a book to the repository.
     *
     * @param book the book to save
     * @return the saved book
     */
    Book save(Book book);

    /**
     * Finds a book by its ID.
     *
     * @param id the book identifier
     * @return the book if found, null otherwise
     */
    Book findById(int id);

    /**
     * Deletes a book by its ID.
     *
     * @param id the book identifier
     */
    void deleteById(int id);

    /**
     * Updates an existing book in the repository.
     *
     * @param book the book to update
     * @return the updated book
     */
    Book update(Book book);
}
