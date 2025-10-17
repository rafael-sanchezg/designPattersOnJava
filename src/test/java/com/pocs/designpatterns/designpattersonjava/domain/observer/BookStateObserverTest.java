package com.pocs.designpatterns.designpattersonjava.domain.observer;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Observer pattern implementations.
 */
@DisplayName("Observer Pattern Tests")
class BookStateObserverTest {

    private Book book;
    private EmailNotificationObserver emailObserver;
    private InventoryLogObserver inventoryObserver;
    private StatisticsObserver statisticsObserver;

    @BeforeEach
    void setUp() {
        book = new Book(1, "Clean Code", "Robert Martin", "No Fiction", "Physical", "Available");
        emailObserver = new EmailNotificationObserver("test@example.com");
        inventoryObserver = new InventoryLogObserver();
        statisticsObserver = new StatisticsObserver();
    }

    @Test
    @DisplayName("EmailNotificationObserver should have recipient email")
    void emailObserverShouldHaveRecipientEmail() {
        assertEquals("test@example.com", emailObserver.getRecipientEmail());
    }

    @Test
    @DisplayName("EmailNotificationObserver should handle state changes")
    void emailObserverShouldHandleStateChanges() {
        assertDoesNotThrow(() ->
            emailObserver.onBookStateChanged(book, "Available", "Loaned"));
    }

    @Test
    @DisplayName("InventoryLogObserver should handle state changes")
    void inventoryObserverShouldHandleStateChanges() {
        assertDoesNotThrow(() ->
            inventoryObserver.onBookStateChanged(book, "Available", "Loaned"));
    }

    @Test
    @DisplayName("StatisticsObserver should track transitions")
    void statisticsObserverShouldTrackTransitions() {
        statisticsObserver.onBookStateChanged(book, "Available", "Loaned");

        assertEquals(1, statisticsObserver.getTotalTransitions());
        assertTrue(statisticsObserver.getStatistics().containsKey("Available → Loaned"));
        assertEquals(1, statisticsObserver.getStatistics().get("Available → Loaned"));
    }

    @Test
    @DisplayName("StatisticsObserver should accumulate multiple transitions")
    void statisticsObserverShouldAccumulateMultipleTransitions() {
        statisticsObserver.onBookStateChanged(book, "Available", "Loaned");
        statisticsObserver.onBookStateChanged(book, "Available", "Loaned");
        statisticsObserver.onBookStateChanged(book, "Loaned", "Available");

        assertEquals(3, statisticsObserver.getTotalTransitions());
        assertEquals(2, statisticsObserver.getStatistics().get("Available → Loaned"));
        assertEquals(1, statisticsObserver.getStatistics().get("Loaned → Available"));
    }

    @Test
    @DisplayName("StatisticsObserver should reset correctly")
    void statisticsObserverShouldResetCorrectly() {
        statisticsObserver.onBookStateChanged(book, "Available", "Loaned");
        statisticsObserver.resetStatistics();

        assertEquals(0, statisticsObserver.getTotalTransitions());
        assertTrue(statisticsObserver.getStatistics().isEmpty());
    }

    @Test
    @DisplayName("StatisticsObserver should return immutable statistics")
    void statisticsObserverShouldReturnImmutableStatistics() {
        statisticsObserver.onBookStateChanged(book, "Available", "Loaned");
        var stats = statisticsObserver.getStatistics();

        // Modifying returned map should not affect internal state
        stats.put("Test", 999);

        assertFalse(statisticsObserver.getStatistics().containsKey("Test"));
    }

    @Test
    @DisplayName("Multiple observers should work independently")
    void multipleObserversShouldWorkIndependently() {
        emailObserver.onBookStateChanged(book, "Available", "Loaned");
        inventoryObserver.onBookStateChanged(book, "Available", "Loaned");
        statisticsObserver.onBookStateChanged(book, "Available", "Loaned");

        // Statistics observer should have exactly one transition
        assertEquals(1, statisticsObserver.getTotalTransitions());
    }
}

