package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.domain.observer.BookStateObserver;
import com.pocs.designpatterns.designpattersonjava.domain.observer.BookStateSubject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing book state changes with Observer pattern.
 * Implements BookStateSubject to notify observers when book states change.
 */
@Service
public class BookStateService implements BookStateSubject {

    private final BookRepositoryPort bookRepositoryPort;
    private final List<BookStateObserver> observers = new ArrayList<>();

    private Book currentBook;
    private String oldState;
    private String newState;

    public BookStateService(BookRepositoryPort bookRepositoryPort) {
        this.bookRepositoryPort = bookRepositoryPort;
    }

    @Override
    public void registerObserver(BookStateObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(BookStateObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (BookStateObserver observer : observers) {
            observer.onBookStateChanged(currentBook, oldState, newState);
        }
    }

    /**
     * Updates the state of a book and notifies all observers.
     *
     * @param bookId the ID of the book to update
     * @param newState the new state for the book
     * @return the updated book
     * @throws IllegalArgumentException if book not found or invalid state
     */
    public Book updateBookState(int bookId, String newState) {
        // Validate new state
        if (!isValidState(newState)) {
            throw new IllegalArgumentException("Invalid state: " + newState +
                ". Valid states are: Available, Loaned");
        }

        // Fetch the book
        Book book = bookRepositoryPort.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found with ID: " + bookId);
        }

        // Check if state actually changed
        if (book.state().equals(newState)) {
            return book; // No change needed
        }

        // Store old state for notification
        this.oldState = book.state();
        this.newState = newState;

        // Create updated book with new state
        Book updatedBook = new Book(
            book.id(),
            book.title(),
            book.author(),
            book.type(),
            book.format(),
            newState
        );

        // Update in repository
        this.currentBook = updateBookInRepository(updatedBook);

        // Notify all observers
        notifyObservers();

        return this.currentBook;
    }

    /**
     * Updates a book in the repository.
     * Since Book is a record and the repository doesn't have an update method,
     * we need to add this functionality.
     */
    private Book updateBookInRepository(Book book) {
        return bookRepositoryPort.update(book);
    }

    /**
     * Validates if a state is valid.
     *
     * @param state the state to validate
     * @return true if valid, false otherwise
     */
    private boolean isValidState(String state) {
        return "Available".equals(state) || "Loaned".equals(state);
    }

    /**
     * Gets all registered observers.
     *
     * @return list of observers
     */
    public List<BookStateObserver> getObservers() {
        return new ArrayList<>(observers);
    }

    /**
     * Gets the count of registered observers.
     *
     * @return number of observers
     */
    public int getObserverCount() {
        return observers.size();
    }
}
