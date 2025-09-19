package com.pocs.designpatterns.designpattersonjava.domain.factory;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Concrete factory for creating Non-Fiction books.
 * Implements the Factory Method pattern.
 */
public class NonFictionBookFactory extends BookFactory {

    @Override
    public Book createBook(int id, String title, String author, String format, String state) {
        return new Book(id, title, author, "No Fiction", format, state);
    }
}
