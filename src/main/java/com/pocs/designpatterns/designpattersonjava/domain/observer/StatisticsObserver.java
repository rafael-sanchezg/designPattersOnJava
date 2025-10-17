package com.pocs.designpatterns.designpattersonjava.domain.observer;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Concrete observer that tracks statistics about book state changes.
 * Maintains counters for different types of state transitions.
 */
public class StatisticsObserver implements BookStateObserver {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsObserver.class);
    private final Map<String, Integer> stateTransitions = new HashMap<>();
    private int totalTransitions = 0;

    @Override
    public void onBookStateChanged(Book book, String oldState, String newState) {
        String transitionKey = oldState + " → " + newState;
        stateTransitions.merge(transitionKey, 1, Integer::sum);
        totalTransitions++;

        logger.info("STATISTICS UPDATE - Total transitions: {}, Transition '{}' count: {}",
            totalTransitions, transitionKey, stateTransitions.get(transitionKey));
    }

    /**
     * Gets the current statistics.
     *
     * @return map of state transitions and their counts
     */
    public Map<String, Integer> getStatistics() {
        return new HashMap<>(stateTransitions);
    }

    /**
     * Gets the total number of state transitions.
     *
     * @return total transitions count
     */
    public int getTotalTransitions() {
        return totalTransitions;
    }

    /**
     * Resets all statistics.
     */
    public void resetStatistics() {
        stateTransitions.clear();
        totalTransitions = 0;
        logger.info("Statistics have been reset");
    }
}

