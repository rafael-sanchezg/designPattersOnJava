package com.pocs.designpatterns.designpattersonjava.domain.factory;

import com.pocs.designpatterns.designpattersonjava.domain.model.ComplexBook;
import java.time.LocalDate;
import java.util.List;

/**
 * Builder Factory that combines Builder pattern with Factory Method pattern.
 * This factory creates complex books using the builder pattern while maintaining
 * the factory pattern structure for different book types.
 */
public abstract class ComplexBookBuilderFactory {

    /**
     * Factory method to create a builder pre-configured for the specific book type.
     * Subclasses implement this to provide type-specific default configurations.
     *
     * @param title the book title
     * @param author the book author
     * @return a pre-configured ComplexBook.Builder
     */
    public abstract ComplexBook.Builder createBookBuilder(String title, String author);

    /**
     * Template method that creates a complete book using the builder pattern.
     * This demonstrates how Factory Method can work with Builder pattern.
     *
     * @param title the book title
     * @param author the book author
     * @return a fully configured ComplexBook
     */
    public final ComplexBook createDefaultBook(String title, String author) {
        return createBookBuilder(title, author)
                .publicationDate(LocalDate.now())
                .language("English")
                .condition("Good")
                .availableForLoan(true)
                .build();
    }

    /**
     * Creates a premium book with enhanced features.
     *
     * @param title the book title
     * @param author the book author
     * @param isbn the book ISBN
     * @param publisher the publisher
     * @param price the book price
     * @return a premium ComplexBook
     */
    public final ComplexBook createPremiumBook(String title, String author, String isbn,
                                             String publisher, Double price) {
        return createBookBuilder(title, author)
                .isbn(isbn)
                .publisher(publisher)
                .price(price)
                .condition("Excellent")
                .availableForLoan(true)
                .location("Premium Section")
                .build();
    }

    /**
     * Creates a digital-only book with specific digital features.
     *
     * @param title the book title
     * @param author the book author
     * @param fileSize additional metadata for digital books
     * @return a digital ComplexBook
     */
    public final ComplexBook createDigitalBook(String title, String author, String fileSize) {
        return createBookBuilder(title, author)
                .format("Digital")
                .description("Digital edition - " + fileSize)
                .location("Digital Library")
                .availableForLoan(true)
                .tags(List.of("digital", "downloadable"))
                .build();
    }
}

/**
 * Concrete factory for creating Fiction books using the Builder pattern.
 */
class FictionBookBuilderFactory extends ComplexBookBuilderFactory {

    @Override
    public ComplexBook.Builder createBookBuilder(String title, String author) {
        return ComplexBook.builder(title, author, "Fiction")
                .genre("Fiction")
                .tags(List.of("fiction", "literature"))
                .location("Fiction Section")
                .language("English");
    }
}

/**
 * Concrete factory for creating Non-Fiction books using the Builder pattern.
 */
class NonFictionBookBuilderFactory extends ComplexBookBuilderFactory {

    @Override
    public ComplexBook.Builder createBookBuilder(String title, String author) {
        return ComplexBook.builder(title, author, "No Fiction")
                .genre("Non-Fiction")
                .tags(List.of("non-fiction", "educational"))
                .location("Non-Fiction Section")
                .language("English");
    }
}

/**
 * Provider for Builder-based factories.
 * This integrates the Builder pattern with the existing factory provider pattern.
 */
class ComplexBookBuilderFactoryProvider {

    private ComplexBookBuilderFactoryProvider() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Gets the appropriate builder factory based on book type.
     *
     * @param bookType "Fiction" or "No Fiction"
     * @return the corresponding builder factory
     * @throws IllegalArgumentException if book type is unsupported
     */
    public static ComplexBookBuilderFactory getBuilderFactory(String bookType) {
        return switch (bookType) {
            case "Fiction" -> new FictionBookBuilderFactory();
            case "No Fiction" -> new NonFictionBookBuilderFactory();
            default -> throw new IllegalArgumentException("Unsupported book type: " + bookType);
        };
    }

    /**
     * Convenience method to create a complex book using the Builder pattern.
     *
     * @param bookType the type of book
     * @param title the book title
     * @param author the book author
     * @return a default configured ComplexBook
     */
    public static ComplexBook createComplexBook(String bookType, String title, String author) {
        ComplexBookBuilderFactory factory = getBuilderFactory(bookType);
        return factory.createDefaultBook(title, author);
    }

    /**
     * Creates a premium complex book with additional features.
     *
     * @param bookType the type of book
     * @param title the book title
     * @param author the book author
     * @param isbn the book ISBN
     * @param publisher the publisher
     * @param price the book price
     * @return a premium ComplexBook
     */
    public static ComplexBook createPremiumComplexBook(String bookType, String title,
                                                     String author, String isbn,
                                                     String publisher, Double price) {
        ComplexBookBuilderFactory factory = getBuilderFactory(bookType);
        return factory.createPremiumBook(title, author, isbn, publisher, price);
    }
}
