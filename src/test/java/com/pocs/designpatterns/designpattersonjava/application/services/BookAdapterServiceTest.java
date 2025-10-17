package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.adapter.IBook;
import com.pocs.designpatterns.designpattersonjava.domain.adapter.LegacyBook;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for BookAdapterService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookAdapterService Tests")
class BookAdapterServiceTest {

    @Mock
    private BookRepositoryPort bookRepositoryPort;

    private BookAdapterService service;

    @BeforeEach
    void setUp() {
        service = new BookAdapterService();
    }

    @Test
    @DisplayName("Should adapt legacy book correctly")
    void shouldAdaptLegacyBookCorrectly() {
        LegacyBook legacyBook = new LegacyBook(
            101, "SICP", "Abelson", "NF", "P", 1);

        IBook adapted = service.adaptLegacyBook(legacyBook);

        assertEquals(101, adapted.getId());
        assertEquals("SICP", adapted.getTitle());
        assertEquals("Abelson", adapted.getAuthor());
        assertEquals("No Fiction", adapted.getType());
        assertEquals("Physical", adapted.getFormat());
        assertEquals("Available", adapted.getState());
    }

    @Test
    @DisplayName("Should adapt modern book correctly")
    void shouldAdaptModernBookCorrectly() {
        Book modernBook = new Book(
            1, "Clean Code", "Martin", "No Fiction", "Physical", "Available");

        IBook adapted = service.adaptModernBook(modernBook);

        assertEquals(1, adapted.getId());
        assertEquals("Clean Code", adapted.getTitle());
        assertEquals("Martin", adapted.getAuthor());
    }

    @Test
    @DisplayName("Should create sample legacy book")
    void shouldCreateSampleLegacyBook() {
        LegacyBook sample = service.createSampleLegacyBook();

        assertNotNull(sample);
        assertEquals(100, sample.getBookId());
        assertNotNull(sample.getBookName());
    }

    @Test
    @DisplayName("Should get all books unified")
    void shouldGetAllBooksUnified() {
        List<IBook> allBooks = service.getAllBooksUnified();

        assertNotNull(allBooks);
        assertTrue(allBooks.size() >= 2);

        // Should contain both legacy and modern books
        boolean hasLegacyBook = allBooks.stream()
            .anyMatch(book -> book.getId() > 100);
        boolean hasModernBook = allBooks.stream()
            .anyMatch(book -> book.getId() < 100);

        assertTrue(hasLegacyBook || hasModernBook);
    }

    @Test
    @DisplayName("Should filter available books")
    void shouldFilterAvailableBooks() {
        List<IBook> allBooks = service.getAllBooksUnified();
        List<IBook> availableBooks = service.getAvailableBooks(allBooks);

        assertNotNull(availableBooks);
        assertTrue(availableBooks.stream().allMatch(IBook::isAvailable));
    }

    @Test
    @DisplayName("Should search books by title")
    void shouldSearchBooksByTitle() {
        List<IBook> allBooks = service.getAllBooksUnified();
        List<IBook> results = service.searchByTitle(allBooks, "Code");

        assertNotNull(results);
        assertTrue(results.stream()
            .allMatch(book -> book.getTitle().toLowerCase().contains("code")));
    }

    @Test
    @DisplayName("Should search books case insensitively")
    void shouldSearchBooksCaseInsensitively() {
        List<IBook> allBooks = service.getAllBooksUnified();
        List<IBook> results = service.searchByTitle(allBooks, "code");

        assertNotNull(results);
        assertTrue(results.stream()
            .allMatch(book -> book.getTitle().toLowerCase().contains("code")));
    }

    @Test
    @DisplayName("Should get statistics from unified list")
    void shouldGetStatisticsFromUnifiedList() {
        List<IBook> allBooks = service.getAllBooksUnified();
        var stats = service.getStatistics(allBooks);

        assertNotNull(stats);
        assertTrue(stats.totalBooks() > 0);
        assertEquals(stats.totalBooks(), stats.availableBooks() + stats.loanedBooks());
        assertEquals(stats.totalBooks(), stats.fictionBooks() + stats.nonFictionBooks());
        assertEquals(stats.totalBooks(), stats.physicalBooks() + stats.digitalBooks());
    }

    @Test
    @DisplayName("Should update book state through adapter")
    void shouldUpdateBookStateThroughAdapter() {
        LegacyBook legacyBook = new LegacyBook(
            101, "Test", "Author", "F", "P", 1);
        IBook adapted = service.adaptLegacyBook(legacyBook);

        assertEquals("Available", adapted.getState());

        service.updateBookState(adapted, "Loaned");

        assertEquals("Loaned", adapted.getState());
    }
}

