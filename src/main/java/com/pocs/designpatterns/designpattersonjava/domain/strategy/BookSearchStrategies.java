package com.pocs.designpatterns.designpattersonjava.domain.strategy;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Concrete strategy for searching books by type (Fiction/No Fiction).
 * Implements the Strategy Pattern for type-based filtering.
 */
class TypeSearchStrategy implements BookSearchStrategy {

    private static final List<String> SUPPORTED_TYPES = List.of("Fiction", "No Fiction");

    @Override
    public List<Book> search(List<Book> books, String searchCriteria) {
        if (searchCriteria == null || searchCriteria.trim().isEmpty()) {
            return List.of();
        }

        String normalizedCriteria = searchCriteria.trim();

        return books.stream()
                .filter(book -> book.type().equalsIgnoreCase(normalizedCriteria))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "Type Search";
    }

    @Override
    public String getDescription() {
        return "Searches books by type (Fiction/No Fiction) using exact matching";
    }

    @Override
    public List<String> getSupportedCriteria() {
        return SUPPORTED_TYPES;
    }
}

/**
 * Concrete strategy for searching books by format (Physical/Digital).
 * Implements the Strategy Pattern for format-based filtering.
 */
class FormatSearchStrategy implements BookSearchStrategy {

    private static final List<String> SUPPORTED_FORMATS = List.of("Physical", "Digital");

    @Override
    public List<Book> search(List<Book> books, String searchCriteria) {
        if (searchCriteria == null || searchCriteria.trim().isEmpty()) {
            return List.of();
        }

        String normalizedCriteria = searchCriteria.trim();

        return books.stream()
                .filter(book -> book.format().equalsIgnoreCase(normalizedCriteria))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "Format Search";
    }

    @Override
    public String getDescription() {
        return "Searches books by format (Physical/Digital) using exact matching";
    }

    @Override
    public List<String> getSupportedCriteria() {
        return SUPPORTED_FORMATS;
    }
}

/**
 * Concrete strategy for searching books by availability state.
 * Implements the Strategy Pattern for state-based filtering.
 */
class StateSearchStrategy implements BookSearchStrategy {

    private static final List<String> SUPPORTED_STATES = List.of("Available", "Loaned");

    @Override
    public List<Book> search(List<Book> books, String searchCriteria) {
        if (searchCriteria == null || searchCriteria.trim().isEmpty()) {
            return List.of();
        }

        String normalizedCriteria = searchCriteria.trim();

        return books.stream()
                .filter(book -> book.state().equalsIgnoreCase(normalizedCriteria))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "State Search";
    }

    @Override
    public String getDescription() {
        return "Searches books by availability state (Available/Loaned) using exact matching";
    }

    @Override
    public List<String> getSupportedCriteria() {
        return SUPPORTED_STATES;
    }
}
