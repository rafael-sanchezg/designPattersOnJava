package com.pocs.designpatterns.designpattersonjava.domain.strategy;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete strategy for searching books by title.
 * Implements partial matching for title searches.
 */
class TitleSearchStrategy implements BookSearchStrategy {

    @Override
    public List<Book> search(List<Book> books, String searchCriteria) {
        if (searchCriteria == null || searchCriteria.trim().isEmpty()) {
            return List.of();
        }

        String normalizedCriteria = searchCriteria.toLowerCase().trim();

        return books.stream()
                .filter(book -> book.title().toLowerCase().contains(normalizedCriteria))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "Title Search";
    }

    @Override
    public String getDescription() {
        return "Searches books by title using case-insensitive partial matching";
    }

    @Override
    public List<String> getSupportedCriteria() {
        return List.of("Any text");
    }
}

/**
 * Concrete strategy for searching books by author.
 * Implements partial matching for author searches.
 */
class AuthorSearchStrategy implements BookSearchStrategy {

    @Override
    public List<Book> search(List<Book> books, String searchCriteria) {
        if (searchCriteria == null || searchCriteria.trim().isEmpty()) {
            return List.of();
        }

        String normalizedCriteria = searchCriteria.toLowerCase().trim();

        return books.stream()
                .filter(book -> book.author().toLowerCase().contains(normalizedCriteria))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "Author Search";
    }

    @Override
    public String getDescription() {
        return "Searches books by author using case-insensitive partial matching";
    }

    @Override
    public List<String> getSupportedCriteria() {
        return List.of("Any author name");
    }
}

/**
 * Concrete strategy for combined type and format search.
 * Allows searching for books that match both type and format criteria.
 */
class CombinedTypeFormatSearchStrategy implements BookSearchStrategy {

    @Override
    public List<Book> search(List<Book> books, String searchCriteria) {
        if (searchCriteria == null || searchCriteria.trim().isEmpty()) {
            return List.of();
        }

        // Expected format: "type:format" (e.g., "Fiction:Digital")
        String[] parts = searchCriteria.split(":");
        if (parts.length != 2) {
            return List.of();
        }

        String type = parts[0].trim();
        String format = parts[1].trim();

        return books.stream()
                .filter(book -> book.type().equalsIgnoreCase(type) &&
                               book.format().equalsIgnoreCase(format))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "Combined Type-Format Search";
    }

    @Override
    public String getDescription() {
        return "Searches books by both type and format using 'type:format' syntax (e.g., 'Fiction:Digital')";
    }

    @Override
    public List<String> getSupportedCriteria() {
        return List.of("Fiction:Physical", "Fiction:Digital", "No Fiction:Physical", "No Fiction:Digital");
    }
}
