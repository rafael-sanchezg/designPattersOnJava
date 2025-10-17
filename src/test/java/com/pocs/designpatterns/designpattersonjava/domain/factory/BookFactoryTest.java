package com.pocs.designpatterns.designpattersonjava.domain.factory;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Factory Method pattern implementations.
 */
@DisplayName("Factory Method Pattern Tests")
class BookFactoryTest {

    @Test
    @DisplayName("FictionBookFactory should create Fiction books")
    void fictionFactoryShouldCreateFictionBooks() {
        BookFactory factory = new FictionBookFactory();
        Book book = factory.createBook(0, "1984", "George Orwell", "Digital", "Available");

        assertEquals("Fiction", book.type());
        assertEquals("1984", book.title());
        assertEquals("George Orwell", book.author());
        assertEquals("Digital", book.format());
        assertEquals("Available", book.state());
    }

    @Test
    @DisplayName("NonFictionBookFactory should create No Fiction books")
    void nonFictionFactoryShouldCreateNoFictionBooks() {
        BookFactory factory = new NonFictionBookFactory();
        Book book = factory.createBook(0, "Clean Code", "Robert Martin", "Physical", "Available");

        assertEquals("No Fiction", book.type());
        assertEquals("Clean Code", book.title());
        assertEquals("Robert Martin", book.author());
    }

    @Test
    @DisplayName("processBookCreation should validate title")
    void processBookCreationShouldValidateTitle() {
        BookFactory factory = new FictionBookFactory();

        assertThrows(IllegalArgumentException.class,
            () -> factory.processBookCreation(0, "", "Author", "Physical", "Available"));
    }

    @Test
    @DisplayName("processBookCreation should validate author")
    void processBookCreationShouldValidateAuthor() {
        BookFactory factory = new FictionBookFactory();

        assertThrows(IllegalArgumentException.class,
            () -> factory.processBookCreation(0, "Title", null, "Physical", "Available"));
    }

    @Test
    @DisplayName("processBookCreation should validate format")
    void processBookCreationShouldValidateFormat() {
        BookFactory factory = new FictionBookFactory();

        assertThrows(IllegalArgumentException.class,
            () -> factory.processBookCreation(0, "Title", "Author", "InvalidFormat", "Available"));
    }

    @Test
    @DisplayName("processBookCreation should validate state")
    void processBookCreationShouldValidateState() {
        BookFactory factory = new FictionBookFactory();

        assertThrows(IllegalArgumentException.class,
            () -> factory.processBookCreation(0, "Title", "Author", "Physical", "InvalidState"));
    }

    @Test
    @DisplayName("processBookCreation should accept valid data")
    void processBookCreationShouldAcceptValidData() {
        BookFactory factory = new FictionBookFactory();

        assertDoesNotThrow(
            () -> factory.processBookCreation(0, "Title", "Author", "Physical", "Available"));
    }

    @Test
    @DisplayName("BookFactoryProvider should return FictionBookFactory")
    void bookFactoryProviderShouldReturnFictionFactory() {
        BookFactory factory = BookFactoryProvider.getBookFactory("Fiction");

        assertNotNull(factory);
        assertTrue(factory instanceof FictionBookFactory);
    }

    @Test
    @DisplayName("BookFactoryProvider should return NonFictionBookFactory")
    void bookFactoryProviderShouldReturnNonFictionFactory() {
        BookFactory factory = BookFactoryProvider.getBookFactory("No Fiction");

        assertNotNull(factory);
        assertTrue(factory instanceof NonFictionBookFactory);
    }

    @Test
    @DisplayName("BookFactoryProvider should throw for invalid type")
    void bookFactoryProviderShouldThrowForInvalidType() {
        assertThrows(IllegalArgumentException.class,
            () -> BookFactoryProvider.getBookFactory("InvalidType"));
    }

    @Test
    @DisplayName("BookFactoryProvider createBook convenience method should work")
    void bookFactoryProviderCreateBookShouldWork() {
        Book book = BookFactoryProvider.createBook(
            "Fiction", 0, "The Hobbit", "Tolkien", "Physical", "Available");

        assertEquals("Fiction", book.type());
        assertEquals("The Hobbit", book.title());
    }
}

