package com.pocs.designpatterns.designpattersonjava.domain.strategy;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Factory for creating and managing book search strategies.
 * Implements the Factory Pattern alongside the Strategy Pattern.
 */
public final class BookSearchStrategyFactory {

    private static final Map<String, BookSearchStrategy> STRATEGIES = new HashMap<>();

    static {
        STRATEGIES.put("type", new TypeSearchStrategy());
        STRATEGIES.put("format", new FormatSearchStrategy());
        STRATEGIES.put("state", new StateSearchStrategy());
        STRATEGIES.put("title", new TitleSearchStrategy());
        STRATEGIES.put("author", new AuthorSearchStrategy());
        STRATEGIES.put("combined", new CombinedTypeFormatSearchStrategy());
    }

    private BookSearchStrategyFactory() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Creates a strategy based on the strategy type.
     *
     * @param strategyType the type of strategy to create
     * @return the corresponding strategy
     * @throws IllegalArgumentException if strategy type is not supported
     */
    public static BookSearchStrategy createStrategy(String strategyType) {
        BookSearchStrategy strategy = STRATEGIES.get(strategyType.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported strategy type: " + strategyType);
        }
        return strategy;
    }

    /**
     * Gets all available strategy types.
     *
     * @return map of strategy types and their descriptions
     */
    public static Map<String, String> getAvailableStrategies() {
        Map<String, String> strategies = new HashMap<>();
        STRATEGIES.forEach((key, strategy) ->
            strategies.put(key, strategy.getDescription())
        );
        return strategies;
    }

    /**
     * Gets strategy information for a specific type.
     *
     * @param strategyType the strategy type
     * @return strategy information
     */
    public static Map<String, Object> getStrategyInfo(String strategyType) {
        BookSearchStrategy strategy = STRATEGIES.get(strategyType.toLowerCase());
        if (strategy == null) {
            return Map.of("error", "Strategy not found: " + strategyType);
        }

        return Map.of(
            "name", strategy.getStrategyName(),
            "description", strategy.getDescription(),
            "supportedCriteria", strategy.getSupportedCriteria()
        );
    }
}
