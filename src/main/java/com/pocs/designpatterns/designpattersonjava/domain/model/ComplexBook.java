package com.pocs.designpatterns.designpattersonjava.domain.model;

import java.time.LocalDate;
import java.util.List;

/**
 * Enhanced Book domain model with additional complexity to demonstrate Builder pattern.
 * This model includes optional fields and complex validation scenarios.
 */
public record ComplexBook(
    int id,
    String title,
    String author,
    String type,
    String format,
    String state,
    String isbn,
    LocalDate publicationDate,
    String publisher,
    Integer pageCount,
    String language,
    String genre,
    Double price,
    String description,
    List<String> tags,
    boolean isAvailableForLoan,
    String location,
    String condition
) {

    /**
     * Builder pattern implementation for creating complex books.
     * This builder allows for flexible and readable object construction.
     */
    public static class Builder {
        // Required fields - marked as final since they're set only once
        private final String title;
        private final String author;
        private final String type;

        // Optional fields with defaults
        private int id = 0;
        private String format = "Physical";
        private String state = "Available";
        private String isbn = "";
        private LocalDate publicationDate = LocalDate.now();
        private String publisher = "";
        private Integer pageCount = 0;
        private String language = "English";
        private String genre = "";
        private Double price = 0.0;
        private String description = "";
        private List<String> tags = List.of();
        private boolean isAvailableForLoan = true;
        private String location = "Main Library";
        private String condition = "Good";

        /**
         * Constructor requiring only essential fields
         */
        public Builder(String title, String author, String type) {
            this.title = title;
            this.author = author;
            this.type = type;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder state(String state) {
            this.state = state;
            return this;
        }

        public Builder isbn(String isbn) {
            this.isbn = isbn;
            return this;
        }

        public Builder publicationDate(LocalDate publicationDate) {
            this.publicationDate = publicationDate;
            return this;
        }

        public Builder publisher(String publisher) {
            this.publisher = publisher;
            return this;
        }

        public Builder pageCount(Integer pageCount) {
            this.pageCount = pageCount;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder genre(String genre) {
            this.genre = genre;
            return this;
        }

        public Builder price(Double price) {
            this.price = price;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder tags(List<String> tags) {
            this.tags = List.copyOf(tags); // Defensive copy
            return this;
        }

        public Builder availableForLoan(boolean isAvailableForLoan) {
            this.isAvailableForLoan = isAvailableForLoan;
            return this;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder condition(String condition) {
            this.condition = condition;
            return this;
        }

        /**
         * Builds the ComplexBook with validation.
         *
         * @return a new ComplexBook instance
         * @throws IllegalArgumentException if validation fails
         */
        public ComplexBook build() {
            validateRequiredFields();
            validateBusinessRules();
            validateFormats();
            return new ComplexBook(
                id, title, author, type, format, state, isbn,
                publicationDate, publisher, pageCount, language,
                genre, price, description, tags, isAvailableForLoan,
                location, condition
            );
        }

        /**
         * Validates required fields.
         */
        private void validateRequiredFields() {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be null or empty");
            }
            if (author == null || author.trim().isEmpty()) {
                throw new IllegalArgumentException("Author cannot be null or empty");
            }
            if (type == null || (!type.equals("Fiction") && !type.equals("No Fiction"))) {
                throw new IllegalArgumentException("Type must be 'Fiction' or 'No Fiction'");
            }
        }

        /**
         * Validates business rules.
         */
        private void validateBusinessRules() {
            if (pageCount != null && pageCount < 0) {
                throw new IllegalArgumentException("Page count cannot be negative");
            }
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Price cannot be negative");
            }
        }

        /**
         * Validates format-specific rules.
         */
        private void validateFormats() {
            if (format == null || (!format.equals("Physical") && !format.equals("Digital"))) {
                throw new IllegalArgumentException("Format must be 'Physical' or 'Digital'");
            }
            if (isbn != null && !isbn.isEmpty() && !isValidISBN(isbn)) {
                throw new IllegalArgumentException("Invalid ISBN format");
            }
        }

        /**
         * Simplified ISBN validation with corrected regex patterns.
         */
        private boolean isValidISBN(String isbn) {
            // ISBN-13 with hyphens: 978-0-123-45678-9
            String isbn13Pattern = "\\d{3}-\\d{1,5}-\\d{1,7}-\\d{1,7}-\\d";
            // ISBN-13 without hyphens: 9780123456789
            String isbn13NoHyphens = "\\d{13}";
            // ISBN-10: 0123456789
            String isbn10NoHyphens = "\\d{10}";

            return isbn.matches(isbn13Pattern) ||
                   isbn.matches(isbn13NoHyphens) ||
                   isbn.matches(isbn10NoHyphens);
        }
    }

    /**
     * Static factory method to start building a ComplexBook.
     */
    public static Builder builder(String title, String author, String type) {
        return new Builder(title, author, type);
    }

    /**
     * Converts a simple Book to a ComplexBook using the builder.
     */
    public static ComplexBook fromSimpleBook(Book book) {
        return new Builder(book.title(), book.author(), book.type())
                .id(book.id())
                .format(book.format())
                .state(book.state())
                .build();
    }

    /**
     * Converts this ComplexBook to a simple Book.
     */
    public Book toSimpleBook() {
        return new Book(id, title, author, type, format, state);
    }
}
