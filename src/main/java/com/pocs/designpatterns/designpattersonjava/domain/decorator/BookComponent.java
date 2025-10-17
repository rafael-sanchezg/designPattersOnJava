package com.pocs.designpatterns.designpattersonjava.domain.decorator;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Component interface for Book operations.
 * Defines the contract that both concrete components and decorators must implement.
 */
public interface BookComponent {

    /**
     * Gets the book entity.
     *
     * @return the book
     */
    Book getBook();

    /**
     * Gets a description of the book and its features.
     *
     * @return description string
     */
    String getDescription();

    /**
     * Gets the current state of the book.
     *
     * @return state string
     */
    String getState();

    /**
     * Gets any additional information about the book.
     *
     * @return additional info
     */
    String getAdditionalInfo();
}

