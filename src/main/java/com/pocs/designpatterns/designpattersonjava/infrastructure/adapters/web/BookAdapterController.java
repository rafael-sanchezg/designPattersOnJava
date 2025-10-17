package com.pocs.designpatterns.designpattersonjava.infrastructure.adapters.web;

import com.pocs.designpatterns.designpattersonjava.application.services.BookAdapterService;
import com.pocs.designpatterns.designpattersonjava.domain.adapter.IBook;
import com.pocs.designpatterns.designpattersonjava.domain.adapter.LegacyBook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for demonstrating the Adapter pattern.
 * Provides endpoints for integrating legacy books with the modern system.
 */
@RestController
@RequestMapping("/books/adapter")
public class BookAdapterController {

    private final BookAdapterService bookAdapterService;

    public BookAdapterController(BookAdapterService bookAdapterService) {
        this.bookAdapterService = bookAdapterService;
    }

    /**
     * Gets all books (legacy and modern) through the unified IBook interface.
     *
     * @return unified list of all books
     */
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllBooksUnified() {
        List<IBook> allBooks = bookAdapterService.getAllBooksUnified();

        Map<String, Object> response = new HashMap<>();
        response.put("totalBooks", allBooks.size());
        response.put("books", allBooks.stream()
            .map(this::bookToMap)
            .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    /**
     * Gets only available books from the unified list.
     *
     * @return available books
     */
    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> getAvailableBooks() {
        List<IBook> allBooks = bookAdapterService.getAllBooksUnified();
        List<IBook> availableBooks = bookAdapterService.getAvailableBooks(allBooks);

        Map<String, Object> response = new HashMap<>();
        response.put("totalAvailable", availableBooks.size());
        response.put("books", availableBooks.stream()
            .map(this::bookToMap)
            .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    /**
     * Searches books by title across legacy and modern systems.
     *
     * @param title title fragment to search for
     * @return matching books
     */
    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchBooks(@RequestParam String title) {
        List<IBook> allBooks = bookAdapterService.getAllBooksUnified();
        List<IBook> matchingBooks = bookAdapterService.searchByTitle(allBooks, title);

        Map<String, Object> response = new HashMap<>();
        response.put("searchTerm", title);
        response.put("resultsCount", matchingBooks.size());
        response.put("books", matchingBooks.stream()
            .map(this::bookToMap)
            .collect(Collectors.toList()));

        return ResponseEntity.ok(response);
    }

    /**
     * Creates a sample legacy book and adapts it.
     *
     * @return the adapted legacy book
     */
    @PostMapping("/legacy/sample")
    public ResponseEntity<Map<String, Object>> createSampleLegacyBook() {
        LegacyBook legacyBook = bookAdapterService.createSampleLegacyBook();
        IBook adaptedBook = bookAdapterService.adaptLegacyBook(legacyBook);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Legacy book created and adapted successfully");
        response.put("legacyFormat", Map.of(
            "bookId", legacyBook.getBookId(),
            "bookName", legacyBook.getBookName(),
            "writerName", legacyBook.getWriterName(),
            "category", legacyBook.getCategory(),
            "mediaType", legacyBook.getMediaType(),
            "availabilityStatus", legacyBook.getAvailabilityStatus()
        ));
        response.put("adaptedFormat", bookToMap(adaptedBook));

        return ResponseEntity.ok(response);
    }

    /**
     * Gets statistics from unified book list.
     *
     * @return book statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        List<IBook> allBooks = bookAdapterService.getAllBooksUnified();
        BookAdapterService.BookStatistics stats = bookAdapterService.getStatistics(allBooks);

        Map<String, Object> response = new HashMap<>();
        response.put("totalBooks", stats.totalBooks());
        response.put("availability", Map.of(
            "available", stats.availableBooks(),
            "loaned", stats.loanedBooks()
        ));
        response.put("types", Map.of(
            "fiction", stats.fictionBooks(),
            "nonFiction", stats.nonFictionBooks()
        ));
        response.put("formats", Map.of(
            "physical", stats.physicalBooks(),
            "digital", stats.digitalBooks()
        ));

        return ResponseEntity.ok(response);
    }

    /**
     * Gets information about the Adapter pattern implementation.
     *
     * @return pattern information
     */
    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getAdapterPatternInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("pattern", "Adapter Pattern");
        info.put("description", "Integrates legacy books with incompatible interfaces into the new system");

        info.put("components", Map.of(
            "IBook", "Target interface - defines the interface that clients expect",
            "LegacyBook", "Adaptee - existing class with incompatible interface",
            "LegacyBookAdapter", "Adapter - converts LegacyBook interface to IBook interface",
            "ModernBookAdapter", "Adapter - converts modern Book record to IBook interface"
        ));

        info.put("legacyFormat", Map.of(
            "bookId", "Legacy ID field",
            "bookName", "Legacy title field (adapted to 'title')",
            "writerName", "Legacy author field (adapted to 'author')",
            "category", "F=Fiction, NF=No Fiction (adapted to full names)",
            "mediaType", "P=Physical, D=Digital (adapted to full names)",
            "availabilityStatus", "1=Available, 0=Loaned (adapted to 'Available'/'Loaned')"
        ));

        info.put("benefits", List.of(
            "Reuses existing legacy code without modification",
            "Provides uniform interface for different book sources",
            "Allows legacy and modern systems to work together",
            "Facilitates gradual system migration",
            "Maintains backward compatibility"
        ));

        info.put("endpoints", Map.of(
            "allBooks", "GET /books/adapter/all - Get all books (legacy + modern)",
            "available", "GET /books/adapter/available - Get available books",
            "search", "GET /books/adapter/search?title=... - Search across all books",
            "sampleLegacy", "POST /books/adapter/legacy/sample - Create sample legacy book",
            "statistics", "GET /books/adapter/statistics - Get unified statistics",
            "info", "GET /books/adapter/info - Get adapter pattern information"
        ));

        return ResponseEntity.ok(info);
    }

    /**
     * Demonstrates the adapter pattern with detailed comparison.
     *
     * @return demonstration results
     */
    @GetMapping("/demo")
    public ResponseEntity<Map<String, Object>> demonstrateAdapterPattern() {
        Map<String, Object> demo = new HashMap<>();

        // Create legacy book
        LegacyBook legacyBook = new LegacyBook(
            999,
            "Structure and Interpretation of Computer Programs",
            "Abelson and Sussman",
            "NF",
            "P",
            1
        );

        // Adapt it
        IBook adaptedBook = bookAdapterService.adaptLegacyBook(legacyBook);

        demo.put("scenario", "Integrating legacy book system with modern system");
        demo.put("legacyBookRawData", Map.of(
            "bookId", legacyBook.getBookId(),
            "bookName", legacyBook.getBookName(),
            "writerName", legacyBook.getWriterName(),
            "category", legacyBook.getCategory(),
            "mediaType", legacyBook.getMediaType(),
            "availabilityStatus", legacyBook.getAvailabilityStatus()
        ));
        demo.put("adaptedBookUnifiedInterface", bookToMap(adaptedBook));
        demo.put("explanation", "The adapter translates legacy format to modern interface seamlessly");

        // Show unified operations
        List<IBook> allBooks = bookAdapterService.getAllBooksUnified();
        demo.put("totalBooksInSystem", allBooks.size());
        demo.put("availableBooksCount", bookAdapterService.getAvailableBooks(allBooks).size());

        return ResponseEntity.ok(demo);
    }

    // Helper method

    private Map<String, Object> bookToMap(IBook book) {
        Map<String, Object> bookMap = new HashMap<>();
        bookMap.put("id", book.getId());
        bookMap.put("title", book.getTitle());
        bookMap.put("author", book.getAuthor());
        bookMap.put("type", book.getType());
        bookMap.put("format", book.getFormat());
        bookMap.put("state", book.getState());
        bookMap.put("available", book.isAvailable());
        return bookMap;
    }
}

