package com.pocs.designpatterns.designpattersonjava.infrastructure.adapters.web;

import com.pocs.designpatterns.designpattersonjava.application.services.BookStateService;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.domain.observer.EmailNotificationObserver;
import com.pocs.designpatterns.designpattersonjava.domain.observer.InventoryLogObserver;
import com.pocs.designpatterns.designpattersonjava.domain.observer.StatisticsObserver;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * REST controller for demonstrating the Observer pattern.
 * Manages book state changes and observer notifications.
 */
@RestController
@RequestMapping("/books/observer")
public class BookObserverController {

    private final BookStateService bookStateService;
    private final StatisticsObserver statisticsObserver;

    public BookObserverController(BookStateService bookStateService) {
        this.bookStateService = bookStateService;

        // Initialize and register default observers
        this.statisticsObserver = new StatisticsObserver();
        bookStateService.registerObserver(statisticsObserver);
        bookStateService.registerObserver(new InventoryLogObserver());
        bookStateService.registerObserver(new EmailNotificationObserver("library@example.com"));
    }

    /**
     * Updates a book's state and triggers observer notifications.
     *
     * @param bookId the ID of the book to update
     * @param newState the new state (Available or Loaned)
     * @return the updated book
     */
    @PutMapping("/{bookId}/state")
    public Book updateBookState(@PathVariable int bookId, @RequestParam String newState) {
        return bookStateService.updateBookState(bookId, newState);
    }

    /**
     * Registers a new email notification observer.
     *
     * @param email the email address for notifications
     * @return confirmation message
     */
    @PostMapping("/observers/email")
    public Map<String, String> addEmailObserver(@RequestParam String email) {
        EmailNotificationObserver observer = new EmailNotificationObserver(email);
        bookStateService.registerObserver(observer);
        return Map.of(
            "message", "Email observer registered successfully",
            "email", email,
            "totalObservers", String.valueOf(bookStateService.getObserverCount())
        );
    }

    /**
     * Gets statistics about book state changes.
     *
     * @return statistics data
     */
    @GetMapping("/statistics")
    public Map<String, Object> getStatistics() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalTransitions", statisticsObserver.getTotalTransitions());
        response.put("transitionDetails", statisticsObserver.getStatistics());
        response.put("activeObservers", bookStateService.getObserverCount());
        return response;
    }

    /**
     * Resets the statistics.
     *
     * @return confirmation message
     */
    @DeleteMapping("/statistics")
    public Map<String, String> resetStatistics() {
        statisticsObserver.resetStatistics();
        return Map.of("message", "Statistics reset successfully");
    }

    /**
     * Gets information about the Observer pattern implementation.
     *
     * @return pattern information
     */
    @GetMapping("/info")
    public Map<String, Object> getObserverPatternInfo() {
        return Map.of(
            "pattern", "Observer Pattern",
            "description", "Notifies multiple observers when a book's state changes",
            "observers", Map.of(
                "EmailNotificationObserver", "Sends email notifications when book state changes",
                "InventoryLogObserver", "Logs all state changes to an audit trail",
                "StatisticsObserver", "Tracks statistics about state transitions"
            ),
            "endpoints", Map.of(
                "updateState", "PUT /books/observer/{bookId}/state?newState=Available|Loaned",
                "addEmailObserver", "POST /books/observer/observers/email?email=user@example.com",
                "getStatistics", "GET /books/observer/statistics",
                "resetStatistics", "DELETE /books/observer/statistics"
            ),
            "currentObservers", bookStateService.getObserverCount()
        );
    }

    /**
     * Demonstrates the Observer pattern by changing multiple book states.
     *
     * @return summary of changes
     */
    @PostMapping("/demo")
    public Map<String, Object> demonstrateObserverPattern() {
        Map<String, Object> response = new HashMap<>();

        // Change state of book 1 from Available to Loaned
        Book book1 = bookStateService.updateBookState(1, "Loaned");

        // Change state of book 2 from Available to Loaned
        Book book2 = bookStateService.updateBookState(2, "Loaned");

        // Change state of book 3 from Loaned to Available
        Book book3 = bookStateService.updateBookState(3, "Available");

        response.put("message", "Observer pattern demonstration completed");
        response.put("booksUpdated", 3);
        response.put("book1", book1);
        response.put("book2", book2);
        response.put("book3", book3);
        response.put("totalTransitions", statisticsObserver.getTotalTransitions());
        response.put("activeObservers", bookStateService.getObserverCount());

        return response;
    }
}

