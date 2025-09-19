package com.pocs.designpatterns.designpattersonjava.domain.factory;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Concrete Abstract Factory for creating Non-Fiction books in different formats.
 * Implements the Abstract Factory pattern for Non-Fiction book creation.
 */
public class NonFictionBookAbstractFactory implements BookAbstractFactory {

    @Override
    public Book createPhysicalBook(int id, String title, String author, String state) {
        return new Book(id, title, author, "No Fiction", "Physical", state);
    }

    @Override
    public Book createDigitalBook(int id, String title, String author, String state) {
        return new Book(id, title, author, "No Fiction", "Digital", state);
    }
}
