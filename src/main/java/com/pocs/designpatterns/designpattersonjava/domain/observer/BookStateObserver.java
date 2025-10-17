package com.pocs.designpatterns.designpattersonjava.domain.observer;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Observer interface for book state changes.
 * Observers implementing this interface will be notified when a book's state changes.
 */
public interface BookStateObserver {

    /**
     * Called when a book's state changes.
     *
     * @param book the book whose state changed
     * @param oldState the previous state
     * @param newState the new state
     */
    void onBookStateChanged(Book book, String oldState, String newState);
}

