package com.pocs.designpatterns.designpattersonjava.domain.decorator;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Abstract decorator for BookComponent.
 * Provides a base implementation that delegates to the wrapped component.
 */
public abstract class BookDecorator implements BookComponent {

    protected final BookComponent wrappedBook;

    protected BookDecorator(BookComponent wrappedBook) {
        this.wrappedBook = wrappedBook;
    }

    @Override
    public Book getBook() {
        return wrappedBook.getBook();
    }

    @Override
    public String getDescription() {
        return wrappedBook.getDescription();
    }

    @Override
    public String getState() {
        return wrappedBook.getState();
    }

    @Override
    public String getAdditionalInfo() {
        return wrappedBook.getAdditionalInfo();
    }
}

