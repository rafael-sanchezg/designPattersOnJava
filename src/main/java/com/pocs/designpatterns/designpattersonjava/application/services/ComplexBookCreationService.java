package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.domain.model.ComplexBook;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

/**
 * Service demonstrating the Builder pattern for creating complex books.
 * This service showcases how the Builder pattern provides flexible and readable
 * object construction for complex domain objects.
 */
@Service
public class ComplexBookCreationService {

    private static final Logger logger = LoggerFactory.getLogger(ComplexBookCreationService.class);

    // Constants to avoid string duplication
    private static final String FICTION_TYPE = "Fiction";
    private static final String NON_FICTION_TYPE = "No Fiction";
    private static final String ENGLISH_LANGUAGE = "English";
    private static final String ADDISON_WESLEY_PUBLISHER = "Addison-Wesley Professional";
    private static final String PROGRAMMING_TAG = "programming";

    /**
     * Demonstrates basic Builder pattern usage with minimal configuration.
     *
     * @return list of books created using basic builder pattern
     */
    public List<ComplexBook> createBooksWithBasicBuilder() {
        List<ComplexBook> books = new ArrayList<>();

        // Simple builder usage - only required fields
        ComplexBook book1 = ComplexBook.builder("Clean Code", "Robert C. Martin", NON_FICTION_TYPE)
                .build();
        books.add(book1);

        // Builder with some optional fields
        ComplexBook book2 = ComplexBook.builder("The Hobbit", "J.R.R. Tolkien", FICTION_TYPE)
                .format("Physical")
                .pageCount(310)
                .language(ENGLISH_LANGUAGE)
                .build();
        books.add(book2);

        return books;
    }

    /**
     * Demonstrates advanced Builder pattern usage with complex configuration.
     *
     * @return list of books created using advanced builder features
     */
    public List<ComplexBook> createBooksWithAdvancedBuilder() {
        List<ComplexBook> books = new ArrayList<>();

        // Complex book with full configuration
        ComplexBook complexBook = ComplexBook.builder("Design Patterns", "Gang of Four", NON_FICTION_TYPE)
                .isbn("978-0201633610")
                .publicationDate(LocalDate.of(1994, 10, 31))
                .publisher(ADDISON_WESLEY_PUBLISHER)
                .pageCount(395)
                .language(ENGLISH_LANGUAGE)
                .genre("Computer Science")
                .price(59.99)
                .description("A foundational book on software design patterns")
                .tags(List.of("design-patterns", "software-engineering", PROGRAMMING_TAG))
                .availableForLoan(true)
                .location("Computer Science Section")
                .condition("Excellent")
                .build();
        books.add(complexBook);

        // Digital book with specific digital features
        ComplexBook digitalBook = ComplexBook.builder("Effective Java", "Joshua Bloch", NON_FICTION_TYPE)
                .format("Digital")
                .isbn("978-0134685991")
                .publicationDate(LocalDate.of(2017, 12, 27))
                .publisher(ADDISON_WESLEY_PUBLISHER)
                .pageCount(416)
                .price(39.99)
                .description("Best practices for Java programming")
                .tags(List.of("java", PROGRAMMING_TAG, "best-practices", "digital"))
                .location("Digital Library")
                .condition("N/A")
                .build();
        books.add(digitalBook);

        return books;
    }

    /**
     * Demonstrates Builder pattern with validation scenarios.
     *
     * @return list of books that passed validation
     */
    public List<ComplexBook> createBooksWithValidation() {
        List<ComplexBook> books = new ArrayList<>();

        try {
            // Valid book
            ComplexBook validBook = ComplexBook.builder("1984", "George Orwell", FICTION_TYPE)
                    .isbn("978-0452284234")
                    .pageCount(328)
                    .price(15.99)
                    .build();
            books.add(validBook);

        } catch (IllegalArgumentException e) {
            logger.warn("Validation caught: {}", e.getMessage());
        }

        return books;
    }

    /**
     * Demonstrates method chaining and fluent interface of Builder pattern.
     *
     * @return a book created with fluent method chaining
     */
    public ComplexBook createBookWithFluentInterface() {
        return ComplexBook.builder("Java Concurrency in Practice", "Brian Goetz", NON_FICTION_TYPE)
                .isbn("978-0321349606")
                .publicationDate(LocalDate.of(2006, 5, 9))
                .publisher(ADDISON_WESLEY_PUBLISHER)
                .pageCount(384)
                .language(ENGLISH_LANGUAGE)
                .genre("Computer Science")
                .price(49.99)
                .description("Comprehensive guide to concurrent programming in Java")
                .tags(List.of("java", "concurrency", "threading", PROGRAMMING_TAG))
                .availableForLoan(true)
                .location("Advanced Programming Section")
                .condition("Good")
                .build();
    }

    /**
     * Demonstrates converting between simple Book and ComplexBook.
     *
     * @param simpleBook a simple book to convert
     * @return a ComplexBook with enhanced details
     */
    public ComplexBook enhanceSimpleBook(com.pocs.designpatterns.designpattersonjava.domain.model.Book simpleBook) {
        // Convert simple book and enhance with additional details
        ComplexBook baseComplexBook = ComplexBook.fromSimpleBook(simpleBook);

        // Create a new enhanced version using the builder pattern
        return ComplexBook.builder(baseComplexBook.title(), baseComplexBook.author(), baseComplexBook.type())
                .id(baseComplexBook.id())
                .format(baseComplexBook.format())
                .state(baseComplexBook.state())
                .description("Enhanced version of: " + simpleBook.title())
                .tags(List.of("enhanced", "converted"))
                .build();
    }

    /**
     * Demonstrates creating books with different complexity levels.
     *
     * @return list of books with varying complexity
     */
    public List<ComplexBook> createBooksWithVaryingComplexity() {
        List<ComplexBook> books = new ArrayList<>();

        // Minimal complexity
        books.add(ComplexBook.builder("Simple Book", "Simple Author", FICTION_TYPE).build());

        // Medium complexity
        books.add(ComplexBook.builder("Medium Book", "Medium Author", NON_FICTION_TYPE)
                .pageCount(250)
                .price(25.99)
                .language(ENGLISH_LANGUAGE)
                .build());

        // High complexity
        books.add(ComplexBook.builder("Complex Book", "Complex Author", FICTION_TYPE)
                .isbn("978-1234567890")
                .publicationDate(LocalDate.of(2023, 1, 1))
                .publisher("Complex Publisher")
                .pageCount(500)
                .language(ENGLISH_LANGUAGE)
                .genre("Science Fiction")
                .price(79.99)
                .description("A highly detailed and complex book with extensive metadata")
                .tags(List.of("complex", "detailed", "metadata", "science-fiction"))
                .availableForLoan(true)
                .location("Premium Fiction Section")
                .condition("Mint")
                .build());

        return books;
    }
}
