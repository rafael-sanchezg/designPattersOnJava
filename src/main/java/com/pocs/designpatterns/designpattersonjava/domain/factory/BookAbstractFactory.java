package com.pocs.designpatterns.designpattersonjava.domain.factory;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Abstract Factory pattern interface for creating books with different formats.
 * This interface defines the contract for creating both physical and digital books.
 */
public interface BookAbstractFactory {

    /**
     * Creates a physical book.
     *
     * @param id the book identifier
     * @param title the book title
     * @param author the book author
     * @param state the book state
     * @return a physical Book instance
     */
    Book createPhysicalBook(int id, String title, String author, String state);

    /**
     * Creates a digital book.
     *
     * @param id the book identifier
     * @param title the book title
     * @param author the book author
     * @param state the book state
     * @return a digital Book instance
     */
    Book createDigitalBook(int id, String title, String author, String state);
}
