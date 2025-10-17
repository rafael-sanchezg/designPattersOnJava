package com.pocs.designpatterns.designpattersonjava.domain.strategy;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Strategy pattern implementations.
 */
@DisplayName("Strategy Pattern Tests")
class BookSearchStrategyTest {

    private List<Book> books;

    @BeforeEach
    void setUp() {
        books = Arrays.asList(
            new Book(1, "Clean Code", "Robert Martin", "No Fiction", "Physical", "Available"),
            new Book(2, "1984", "George Orwell", "Fiction", "Digital", "Loaned"),
            new Book(3, "The Hobbit", "J.R.R. Tolkien", "Fiction", "Physical", "Available"),
            new Book(4, "Refactoring", "Martin Fowler", "No Fiction", "Digital", "Available")
        );
    }

    @Test
    @DisplayName("TitleSearchStrategy should find books by title")
    void titleSearchStrategyShouldFindBooksByTitle() {
        BookSearchStrategy strategy = new TitleSearchStrategy();
        List<Book> results = strategy.search(books, "Clean");

        assertEquals(1, results.size());
        assertEquals("Clean Code", results.get(0).title());
    }

    @Test
    @DisplayName("TitleSearchStrategy should be case insensitive")
    void titleSearchStrategyShouldBeCaseInsensitive() {
        BookSearchStrategy strategy = new TitleSearchStrategy();
        List<Book> results = strategy.search(books, "clean");

        assertEquals(1, results.size());
        assertEquals("Clean Code", results.get(0).title());
    }

    @Test
    @DisplayName("TitleSearchStrategy should support partial matching")
    void titleSearchStrategyShouldSupportPartialMatching() {
        BookSearchStrategy strategy = new TitleSearchStrategy();
        List<Book> results = strategy.search(books, "The");

        assertEquals(1, results.size());
        assertEquals("The Hobbit", results.get(0).title());
    }

    @Test
    @DisplayName("AuthorSearchStrategy should find books by author")
    void authorSearchStrategyShouldFindBooksByAuthor() {
        BookSearchStrategy strategy = new AuthorSearchStrategy();
        List<Book> results = strategy.search(books, "Martin");

        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(b -> b.author().contains("Martin")));
    }

    @Test
    @DisplayName("AuthorSearchStrategy should be case insensitive")
    void authorSearchStrategyShouldBeCaseInsensitive() {
        BookSearchStrategy strategy = new AuthorSearchStrategy();
        List<Book> results = strategy.search(books, "martin");

        assertEquals(2, results.size());
    }

    @Test
    @DisplayName("TypeSearchStrategy should filter by Fiction")
    void typeSearchStrategyShouldFilterByFiction() {
        BookSearchStrategy strategy = new TypeSearchStrategy();
        List<Book> results = strategy.search(books, "Fiction");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(b -> "Fiction".equals(b.type())));
    }

    @Test
    @DisplayName("TypeSearchStrategy should filter by No Fiction")
    void typeSearchStrategyShouldFilterByNoFiction() {
        BookSearchStrategy strategy = new TypeSearchStrategy();
        List<Book> results = strategy.search(books, "No Fiction");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(b -> "No Fiction".equals(b.type())));
    }

    @Test
    @DisplayName("FormatSearchStrategy should filter by Physical")
    void formatSearchStrategyShouldFilterByPhysical() {
        BookSearchStrategy strategy = new FormatSearchStrategy();
        List<Book> results = strategy.search(books, "Physical");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(b -> "Physical".equals(b.format())));
    }

    @Test
    @DisplayName("FormatSearchStrategy should filter by Digital")
    void formatSearchStrategyShouldFilterByDigital() {
        BookSearchStrategy strategy = new FormatSearchStrategy();
        List<Book> results = strategy.search(books, "Digital");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(b -> "Digital".equals(b.format())));
    }

    @Test
    @DisplayName("StateSearchStrategy should filter by Available")
    void stateSearchStrategyShouldFilterByAvailable() {
        BookSearchStrategy strategy = new StateSearchStrategy();
        List<Book> results = strategy.search(books, "Available");

        assertEquals(3, results.size());
        assertTrue(results.stream().allMatch(b -> "Available".equals(b.state())));
    }

    @Test
    @DisplayName("StateSearchStrategy should filter by Loaned")
    void stateSearchStrategyShouldFilterByLoaned() {
        BookSearchStrategy strategy = new StateSearchStrategy();
        List<Book> results = strategy.search(books, "Loaned");

        assertEquals(1, results.size());
        assertEquals("Loaned", results.get(0).state());
    }

    @Test
    @DisplayName("BookSearchStrategyFactory should create correct strategy")
    void factoryShouldCreateCorrectStrategy() {
        BookSearchStrategy titleStrategy = BookSearchStrategyFactory.createStrategy("title");
        assertTrue(titleStrategy instanceof TitleSearchStrategy);

        BookSearchStrategy authorStrategy = BookSearchStrategyFactory.createStrategy("author");
        assertTrue(authorStrategy instanceof AuthorSearchStrategy);
    }

    @Test
    @DisplayName("BookSearchStrategyFactory should throw for unknown strategy")
    void factoryShouldThrowForUnknownStrategy() {
        assertThrows(IllegalArgumentException.class,
            () -> BookSearchStrategyFactory.createStrategy("unknown"));
    }

    @Test
    @DisplayName("Strategy should have name")
    void strategyShouldHaveName() {
        BookSearchStrategy strategy = new TitleSearchStrategy();
        assertNotNull(strategy.getStrategyName());
        assertFalse(strategy.getStrategyName().isEmpty());
    }

    @Test
    @DisplayName("Strategy should have description")
    void strategyShouldHaveDescription() {
        BookSearchStrategy strategy = new TitleSearchStrategy();
        assertNotNull(strategy.getDescription());
        assertFalse(strategy.getDescription().isEmpty());
    }
}
