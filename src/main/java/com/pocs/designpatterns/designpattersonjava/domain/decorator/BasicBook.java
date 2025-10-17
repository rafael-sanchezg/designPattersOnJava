package com.pocs.designpatterns.designpattersonjava.domain.decorator;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Concrete component that represents a basic book without any decorations.
 * This is the base component that decorators will wrap.
 */
public class BasicBook implements BookComponent {

    private final Book book;

    public BasicBook(Book book) {
        this.book = book;
    }

    @Override
    public Book getBook() {
        return book;
    }

    @Override
    public String getDescription() {
        return String.format("Book: '%s' by %s [%s, %s]",
            book.title(), book.author(), book.type(), book.format());
    }

    @Override
    public String getState() {
        return book.state();
    }

    @Override
    public String getAdditionalInfo() {
        return "";
    }
}

