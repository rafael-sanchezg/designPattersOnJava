package com.pocs.designpatterns.designpattersonjava.domain.adapter;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

/**
 * Adapter that wraps the modern Book record to implement IBook interface.
 * This demonstrates how both legacy and modern books can work through the same interface.
 */
public class ModernBookAdapter implements IBook {

    private Book book;

    /**
     * Constructor that wraps a modern Book record.
     *
     * @param book the modern book to adapt
     */
    public ModernBookAdapter(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        this.book = book;
    }

    @Override
    public int getId() {
        return book.id();
    }

    @Override
    public String getTitle() {
        return book.title();
    }

    @Override
    public String getAuthor() {
        return book.author();
    }

    @Override
    public String getType() {
        return book.type();
    }

    @Override
    public String getFormat() {
        return book.format();
    }

    @Override
    public String getState() {
        return book.state();
    }

    @Override
    public void setState(String state) {
        // Since Book is a record (immutable), create a new instance
        this.book = new Book(
            book.id(),
            book.title(),
            book.author(),
            book.type(),
            book.format(),
            state
        );
    }

    @Override
    public String getDisplayInfo() {
        return String.format("Book[id=%d, title='%s', author='%s', type='%s', format='%s', state='%s']",
            getId(), getTitle(), getAuthor(), getType(), getFormat(), getState());
    }

    @Override
    public boolean isAvailable() {
        return "Available".equalsIgnoreCase(book.state());
    }

    /**
     * Gets the wrapped Book record.
     *
     * @return the underlying book
     */
    public Book getBook() {
        return book;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IBook other)) return false;
        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getId());
    }
}

