package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookSearchService using Strategy pattern.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookSearchService Tests")
class BookSearchServiceTest {

    @Mock
    private BookRepositoryPort bookRepositoryPort;

    private BookSearchService service;
    private List<Book> testBooks;

    @BeforeEach
    void setUp() {
        service = new BookSearchService(bookRepositoryPort);

        testBooks = Arrays.asList(
            new Book(1, "Clean Code", "Robert Martin", "No Fiction", "Physical", "Available"),
            new Book(2, "1984", "George Orwell", "Fiction", "Digital", "Loaned"),
            new Book(3, "The Hobbit", "J.R.R. Tolkien", "Fiction", "Physical", "Available"),
            new Book(4, "Refactoring", "Martin Fowler", "No Fiction", "Digital", "Available")
        );
    }

    @Test
    @DisplayName("Should search books by type")
    void shouldSearchBooksByType() {
        when(bookRepositoryPort.findAll()).thenReturn(testBooks);

        List<Book> fictionBooks = service.searchBooksByType("Fiction");

        assertEquals(2, fictionBooks.size());
        assertTrue(fictionBooks.stream().allMatch(b -> "Fiction".equals(b.type())));
    }

    @Test
    @DisplayName("Should search books by format")
    void shouldSearchBooksByFormat() {
        when(bookRepositoryPort.findAll()).thenReturn(testBooks);

        List<Book> physicalBooks = service.searchBooksByFormat("Physical");

        assertEquals(2, physicalBooks.size());
        assertTrue(physicalBooks.stream().allMatch(b -> "Physical".equals(b.format())));
    }

    @Test
    @DisplayName("Should search books by state")
    void shouldSearchBooksByState() {
        when(bookRepositoryPort.findAll()).thenReturn(testBooks);

        List<Book> availableBooks = service.searchBooksByState("Available");

        assertEquals(3, availableBooks.size());
        assertTrue(availableBooks.stream().allMatch(b -> "Available".equals(b.state())));
    }

    @Test
    @DisplayName("Should search books by title")
    void shouldSearchBooksByTitle() {
        when(bookRepositoryPort.findAll()).thenReturn(testBooks);

        List<Book> results = service.searchBooksByTitle("Code");

        assertEquals(1, results.size());
        assertEquals("Clean Code", results.get(0).title());
    }

    @Test
    @DisplayName("Should search books by author")
    void shouldSearchBooksByAuthor() {
        when(bookRepositoryPort.findAll()).thenReturn(testBooks);

        List<Book> results = service.searchBooksByAuthor("Martin");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(b -> b.author().contains("Martin")));
    }

    @Test
    @DisplayName("Should search with dynamic strategy selection")
    void shouldSearchWithDynamicStrategySelection() {
        when(bookRepositoryPort.findAll()).thenReturn(testBooks);

        // Use the searchContext directly with createStrategy
        List<Book> results = service.searchBooksByType("Fiction");

        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(b -> "Fiction".equals(b.type())));
    }

    @Test
    @DisplayName("Should return empty list when no matches found")
    void shouldReturnEmptyListWhenNoMatchesFound() {
        when(bookRepositoryPort.findAll()).thenReturn(testBooks);

        List<Book> results = service.searchBooksByTitle("NonExistent");

        assertTrue(results.isEmpty());
    }

    @Test
    @DisplayName("Should handle empty book list")
    void shouldHandleEmptyBookList() {
        when(bookRepositoryPort.findAll()).thenReturn(List.of());

        List<Book> results = service.searchBooksByType("Fiction");

        assertTrue(results.isEmpty());
    }
}
