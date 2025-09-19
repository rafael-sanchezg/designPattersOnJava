package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.domain.strategy.BookSearchContext;
import com.pocs.designpatterns.designpattersonjava.domain.strategy.BookSearchStrategyFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Application service implementing book search use cases using the Strategy Pattern.
 * This service demonstrates how different search algorithms can be applied
 * interchangeably at runtime within the hexagonal architecture.
 */
@Service
public class BookSearchService {

    private final BookRepositoryPort bookRepositoryPort;
    private final BookSearchContext searchContext;

    public BookSearchService(BookRepositoryPort bookRepositoryPort) {
        this.bookRepositoryPort = bookRepositoryPort;
        this.searchContext = new BookSearchContext();
    }

    /**
     * Searches books by type (Fiction/No Fiction) using Type Search Strategy.
     *
     * @param type the book type to search for
     * @return list of books matching the type
     */
    public List<Book> searchBooksByType(String type) {
        List<Book> allBooks = bookRepositoryPort.findAll();
        searchContext.setStrategy(BookSearchStrategyFactory.createStrategy("type"));
        return searchContext.executeSearch(allBooks, type);
    }

    /**
     * Searches books by format (Physical/Digital) using Format Search Strategy.
     *
     * @param format the book format to search for
     * @return list of books matching the format
     */
    public List<Book> searchBooksByFormat(String format) {
        List<Book> allBooks = bookRepositoryPort.findAll();
        searchContext.setStrategy(BookSearchStrategyFactory.createStrategy("format"));
        return searchContext.executeSearch(allBooks, format);
    }

    /**
     * Searches books by availability state using State Search Strategy.
     *
     * @param state the book state to search for
     * @return list of books matching the state
     */
    public List<Book> searchBooksByState(String state) {
        List<Book> allBooks = bookRepositoryPort.findAll();
        searchContext.setStrategy(BookSearchStrategyFactory.createStrategy("state"));
        return searchContext.executeSearch(allBooks, state);
    }

    /**
     * Searches books by title using Title Search Strategy.
     *
     * @param title the title to search for (partial matching)
     * @return list of books matching the title
     */
    public List<Book> searchBooksByTitle(String title) {
        List<Book> allBooks = bookRepositoryPort.findAll();
        searchContext.setStrategy(BookSearchStrategyFactory.createStrategy("title"));
        return searchContext.executeSearch(allBooks, title);
    }

    /**
     * Searches books by author using Author Search Strategy.
     *
     * @param author the author to search for (partial matching)
     * @return list of books matching the author
     */
    public List<Book> searchBooksByAuthor(String author) {
        List<Book> allBooks = bookRepositoryPort.findAll();
        searchContext.setStrategy(BookSearchStrategyFactory.createStrategy("author"));
        return searchContext.executeSearch(allBooks, author);
    }

    /**
     * Searches books by both type and format using Combined Search Strategy.
     *
     * @param type the book type
     * @param format the book format
     * @return list of books matching both criteria
     */
    public List<Book> searchBooksByTypeAndFormat(String type, String format) {
        List<Book> allBooks = bookRepositoryPort.findAll();
        searchContext.setStrategy(BookSearchStrategyFactory.createStrategy("combined"));
        String combinedCriteria = type + ":" + format;
        return searchContext.executeSearch(allBooks, combinedCriteria);
    }

    /**
     * Generic search method that allows runtime strategy selection.
     *
     * @param strategyType the type of search strategy to use
     * @param searchCriteria the criteria to search for
     * @return search results and strategy information
     */
    public Map<String, Object> searchWithStrategy(String strategyType, String searchCriteria) {
        try {
            List<Book> allBooks = bookRepositoryPort.findAll();
            searchContext.setStrategy(BookSearchStrategyFactory.createStrategy(strategyType));

            List<Book> results = searchContext.executeSearch(allBooks, searchCriteria);
            Map<String, Object> strategyInfo = searchContext.getStrategyInfo();

            return Map.of(
                "results", results,
                "strategy", strategyInfo,
                "searchCriteria", searchCriteria,
                "totalResults", results.size()
            );
        } catch (IllegalArgumentException e) {
            return Map.of(
                "error", e.getMessage(),
                "availableStrategies", BookSearchStrategyFactory.getAvailableStrategies()
            );
        }
    }

    /**
     * Gets information about all available search strategies.
     *
     * @return map of available strategies and their descriptions
     */
    public Map<String, Object> getAvailableStrategies() {
        return Map.of(
            "strategies", BookSearchStrategyFactory.getAvailableStrategies(),
            "description", "Available search strategies in the Strategy Pattern implementation"
        );
    }

    /**
     * Gets detailed information about a specific strategy.
     *
     * @param strategyType the strategy type to get information for
     * @return detailed strategy information
     */
    public Map<String, Object> getStrategyInfo(String strategyType) {
        return BookSearchStrategyFactory.getStrategyInfo(strategyType);
    }

    /**
     * Demonstrates all search strategies with sample data.
     *
     * @return demonstration results for all strategies
     */
    public Map<String, Object> demonstrateAllStrategies() {
        List<Book> allBooks = bookRepositoryPort.findAll();
        Map<String, Object> demonstrations = Map.of(
            "fictionBooks", searchBooksByType("Fiction"),
            "digitalBooks", searchBooksByFormat("Digital"),
            "availableBooks", searchBooksByState("Available"),
            "tolkienBooks", searchBooksByAuthor("Tolkien"),
            "fictionDigitalBooks", searchBooksByTypeAndFormat("Fiction", "Digital"),
            "totalBooks", allBooks.size(),
            "strategiesUsed", List.of("Type", "Format", "State", "Author", "Combined")
        );

        return demonstrations;
    }
}
