package com.pocs.designpatterns.designpattersonjava.domain.factory;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Concrete Abstract Factory for creating Fiction books in different formats.
 * Implements the Abstract Factory pattern for Fiction book creation.
 */
public class FictionBookAbstractFactory implements BookAbstractFactory {

    @Override
    public Book createPhysicalBook(int id, String title, String author, String state) {
        return new Book(id, title, author, "Fiction", "Physical", state);
    }

    @Override
    public Book createDigitalBook(int id, String title, String author, String state) {
        return new Book(id, title, author, "Fiction", "Digital", state);
    }
}
