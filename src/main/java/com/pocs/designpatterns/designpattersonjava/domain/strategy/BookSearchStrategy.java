package com.pocs.designpatterns.designpattersonjava.domain.strategy;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

import java.util.List;

/**
 * Strategy interface for book search algorithms.
 * This interface defines the contract that all search strategies must implement
 * following the Strategy Pattern.
 */
public interface BookSearchStrategy {

    /**
     * Searches for books based on the strategy's algorithm.
     *
     * @param books the list of books to search through
     * @param searchCriteria the criteria to search for
     * @return list of books matching the search criteria
     */
    List<Book> search(List<Book> books, String searchCriteria);

    /**
     * Returns the name of this search strategy.
     *
     * @return strategy name for identification
     */
    String getStrategyName();

    /**
     * Returns a description of how this strategy works.
     *
     * @return strategy description
     */
    String getDescription();

    /**
     * Returns the supported search criteria for this strategy.
     *
     * @return list of supported criteria
     */
    List<String> getSupportedCriteria();
}
