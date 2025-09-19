package com.pocs.designpatterns.designpattersonjava.domain.strategy;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

import java.util.List;
import java.util.Map;

/**
 * Context class for the Strategy Pattern.
 * This class manages the selection and execution of different search strategies.
 */
public class BookSearchContext {

    private BookSearchStrategy strategy;

    /**
     * Sets the search strategy to be used.
     *
     * @param strategy the strategy to use for searching
     */
    public void setStrategy(BookSearchStrategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Executes the search using the current strategy.
     *
     * @param books the list of books to search through
     * @param searchCriteria the criteria to search for
     * @return list of books matching the search criteria
     * @throws IllegalStateException if no strategy is set
     */
    public List<Book> executeSearch(List<Book> books, String searchCriteria) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy must be set before executing search");
        }
        return strategy.search(books, searchCriteria);
    }

    /**
     * Gets the current strategy information.
     *
     * @return map containing strategy information
     */
    public Map<String, Object> getStrategyInfo() {
        if (strategy == null) {
            return Map.of("error", "No strategy set");
        }

        return Map.of(
            "name", strategy.getStrategyName(),
            "description", strategy.getDescription(),
            "supportedCriteria", strategy.getSupportedCriteria()
        );
    }
}
