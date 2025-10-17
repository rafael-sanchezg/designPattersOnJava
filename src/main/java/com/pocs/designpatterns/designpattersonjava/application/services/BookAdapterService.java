package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.domain.adapter.IBook;
import com.pocs.designpatterns.designpattersonjava.domain.adapter.LegacyBook;
import com.pocs.designpatterns.designpattersonjava.domain.adapter.LegacyBookAdapter;
import com.pocs.designpatterns.designpattersonjava.domain.adapter.ModernBookAdapter;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for managing book adapters and integrating legacy books into the system.
 * Demonstrates the Adapter pattern by providing a unified interface for both
 * legacy and modern books.
 */
@Service
public class BookAdapterService {

    /**
     * Converts a legacy book to the IBook interface using the adapter.
     *
     * @param legacyBook the legacy book to adapt
     * @return adapted book implementing IBook interface
     */
    public IBook adaptLegacyBook(LegacyBook legacyBook) {
        return new LegacyBookAdapter(legacyBook);
    }

    /**
     * Converts a modern Book record to the IBook interface.
     *
     * @param book the modern book to adapt
     * @return adapted book implementing IBook interface
     */
    public IBook adaptModernBook(Book book) {
        return new ModernBookAdapter(book);
    }

    /**
     * Creates a sample legacy book for demonstration.
     *
     * @return a legacy book instance
     */
    public LegacyBook createSampleLegacyBook() {
        return new LegacyBook(
            100,
            "The Art of Computer Programming",
            "Donald Knuth",
            "NF",  // Non-Fiction
            "P",   // Physical
            1      // Available
        );
    }

    /**
     * Demonstrates working with both legacy and modern books through the same interface.
     *
     * @return list of books from different sources
     */
    public List<IBook> getAllBooksUnified() {
        List<IBook> allBooks = new ArrayList<>();

        // Add legacy books (adapted)
        LegacyBook legacy1 = new LegacyBook(101, "Introduction to Algorithms",
            "Cormen, Leiserson, Rivest", "NF", "P", 1);
        LegacyBook legacy2 = new LegacyBook(102, "The C Programming Language",
            "Kernighan and Ritchie", "NF", "D", 0);

        allBooks.add(adaptLegacyBook(legacy1));
        allBooks.add(adaptLegacyBook(legacy2));

        // Add modern books (adapted)
        Book modern1 = new Book(1, "Clean Code", "Robert Martin",
            "No Fiction", "Physical", "Available");
        Book modern2 = new Book(2, "1984", "George Orwell",
            "Fiction", "Digital", "Loaned");

        allBooks.add(adaptModernBook(modern1));
        allBooks.add(adaptModernBook(modern2));

        return allBooks;
    }

    /**
     * Demonstrates updating book state through the adapter interface.
     *
     * @param book the book to update
     * @param newState the new state
     */
    public void updateBookState(IBook book, String newState) {
        String oldState = book.getState();
        book.setState(newState);
        System.out.printf("Book '%s' state changed: %s → %s%n",
            book.getTitle(), oldState, newState);
    }

    /**
     * Filters available books from a mixed list.
     *
     * @param books list of books (legacy and modern)
     * @return list of available books
     */
    public List<IBook> getAvailableBooks(List<IBook> books) {
        return books.stream()
            .filter(IBook::isAvailable)
            .toList();
    }

    /**
     * Searches books by title (works with both legacy and modern books).
     *
     * @param books list of books to search
     * @param titleFragment fragment of title to search for
     * @return matching books
     */
    public List<IBook> searchByTitle(List<IBook> books, String titleFragment) {
        return books.stream()
            .filter(book -> book.getTitle().toLowerCase()
                .contains(titleFragment.toLowerCase()))
            .toList();
    }

    /**
     * Gets book statistics from a unified list.
     *
     * @param books list of books
     * @return statistics map
     */
    public BookStatistics getStatistics(List<IBook> books) {
        long available = books.stream().filter(IBook::isAvailable).count();
        long loaned = books.size() - available;
        long fiction = books.stream()
            .filter(b -> "Fiction".equalsIgnoreCase(b.getType())).count();
        long nonFiction = books.stream()
            .filter(b -> "No Fiction".equalsIgnoreCase(b.getType())).count();
        long physical = books.stream()
            .filter(b -> "Physical".equalsIgnoreCase(b.getFormat())).count();
        long digital = books.stream()
            .filter(b -> "Digital".equalsIgnoreCase(b.getFormat())).count();

        return new BookStatistics(
            books.size(),
            available,
            loaned,
            fiction,
            nonFiction,
            physical,
            digital
        );
    }

    /**
     * Record to hold book statistics.
     */
    public record BookStatistics(
        long totalBooks,
        long availableBooks,
        long loanedBooks,
        long fictionBooks,
        long nonFictionBooks,
        long physicalBooks,
        long digitalBooks
    ) {}
}

