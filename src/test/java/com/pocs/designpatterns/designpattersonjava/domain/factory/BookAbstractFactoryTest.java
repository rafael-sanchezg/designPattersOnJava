package com.pocs.designpatterns.designpattersonjava.domain.factory;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Abstract Factory pattern implementations.
 */
@DisplayName("Abstract Factory Pattern Tests")
class BookAbstractFactoryTest {

    @Test
    @DisplayName("FictionBookAbstractFactory should create Physical Fiction books")
    void fictionFactoryShouldCreatePhysicalBooks() {
        BookAbstractFactory factory = new FictionBookAbstractFactory();
        Book book = factory.createPhysicalBook(0, "1984", "Orwell", "Available");

        assertEquals("Fiction", book.type());
        assertEquals("Physical", book.format());
        assertEquals("1984", book.title());
    }

    @Test
    @DisplayName("FictionBookAbstractFactory should create Digital Fiction books")
    void fictionFactoryShouldCreateDigitalBooks() {
        BookAbstractFactory factory = new FictionBookAbstractFactory();
        Book book = factory.createDigitalBook(0, "Brave New World", "Huxley", "Available");

        assertEquals("Fiction", book.type());
        assertEquals("Digital", book.format());
    }

    @Test
    @DisplayName("NonFictionBookAbstractFactory should create Physical No Fiction books")
    void nonFictionFactoryShouldCreatePhysicalBooks() {
        BookAbstractFactory factory = new NonFictionBookAbstractFactory();
        Book book = factory.createPhysicalBook(0, "Clean Code", "Martin", "Available");

        assertEquals("No Fiction", book.type());
        assertEquals("Physical", book.format());
    }

    @Test
    @DisplayName("NonFictionBookAbstractFactory should create Digital No Fiction books")
    void nonFictionFactoryShouldCreateDigitalBooks() {
        BookAbstractFactory factory = new NonFictionBookAbstractFactory();
        Book book = factory.createDigitalBook(0, "Refactoring", "Fowler", "Available");

        assertEquals("No Fiction", book.type());
        assertEquals("Digital", book.format());
    }

    @Test
    @DisplayName("BookFactoryProvider should return FictionBookAbstractFactory")
    void providerShouldReturnFictionAbstractFactory() {
        BookAbstractFactory factory = BookFactoryProvider.getAbstractFactory("Fiction");

        assertNotNull(factory);
        assertTrue(factory instanceof FictionBookAbstractFactory);
    }

    @Test
    @DisplayName("BookFactoryProvider should return NonFictionBookAbstractFactory")
    void providerShouldReturnNonFictionAbstractFactory() {
        BookAbstractFactory factory = BookFactoryProvider.getAbstractFactory("No Fiction");

        assertNotNull(factory);
        assertTrue(factory instanceof NonFictionBookAbstractFactory);
    }

    @Test
    @DisplayName("BookFactoryProvider should throw for invalid abstract factory type")
    void providerShouldThrowForInvalidAbstractFactoryType() {
        assertThrows(IllegalArgumentException.class,
            () -> BookFactoryProvider.getAbstractFactory("InvalidType"));
    }

    @Test
    @DisplayName("createBookWithFormat convenience method should work")
    void createBookWithFormatShouldWork() {
        Book book = BookFactoryProvider.createBookWithFormat(
            "Fiction", "Digital", 0, "Dune", "Herbert", "Available");

        assertEquals("Fiction", book.type());
        assertEquals("Digital", book.format());
        assertEquals("Dune", book.title());
    }

    @Test
    @DisplayName("Abstract Factory should ensure product family consistency")
    void abstractFactoryShouldEnsureProductFamilyConsistency() {
        BookAbstractFactory fictionFactory = new FictionBookAbstractFactory();

        Book physical = fictionFactory.createPhysicalBook(0, "Book1", "Author", "Available");
        Book digital = fictionFactory.createDigitalBook(0, "Book2", "Author", "Available");

        // Both should be Fiction (same family)
        assertEquals(physical.type(), digital.type());
        assertEquals("Fiction", physical.type());
    }
}

