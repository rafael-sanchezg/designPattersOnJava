package com.pocs.designpatterns.designpattersonjava.domain.factory;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Concrete factory for creating Fiction books.
 * Implements the Factory Method pattern.
 */
public class FictionBookFactory extends BookFactory {

    @Override
    public Book createBook(int id, String title, String author, String format, String state) {
        return new Book(id, title, author, "Fiction", format, state);
    }
}
