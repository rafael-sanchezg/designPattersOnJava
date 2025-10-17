package com.pocs.designpatterns.designpattersonjava.domain.adapter;

/**
 * Interface that defines the contract for books in the new system.
 * All books in the current system must implement this interface.
 */
public interface IBook {

    /**
     * Gets the book ID.
     *
     * @return the book identifier
     */
    int getId();

    /**
     * Gets the book title.
     *
     * @return the book title
     */
    String getTitle();

    /**
     * Gets the book author.
     *
     * @return the book author
     */
    String getAuthor();

    /**
     * Gets the book type (Fiction/No Fiction).
     *
     * @return the book type
     */
    String getType();

    /**
     * Gets the book format (Physical/Digital).
     *
     * @return the book format
     */
    String getFormat();

    /**
     * Gets the book state (Available/Loaned).
     *
     * @return the book state
     */
    String getState();

    /**
     * Sets the book state.
     *
     * @param state the new state
     */
    void setState(String state);

    /**
     * Gets a formatted display string for the book.
     *
     * @return formatted book information
     */
    String getDisplayInfo();

    /**
     * Checks if the book is available for loan.
     *
     * @return true if available, false otherwise
     */
    boolean isAvailable();
}

