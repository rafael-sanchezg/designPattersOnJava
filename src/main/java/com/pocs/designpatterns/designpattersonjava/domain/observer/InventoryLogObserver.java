package com.pocs.designpatterns.designpattersonjava.domain.observer;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete observer that logs book state changes to an inventory log.
 * Maintains an audit trail of all state changes.
 */
public class InventoryLogObserver implements BookStateObserver {

    private static final Logger logger = LoggerFactory.getLogger(InventoryLogObserver.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onBookStateChanged(Book book, String oldState, String newState) {
        String timestamp = LocalDateTime.now().format(formatter);
        String logEntry = String.format(
            "[%s] INVENTORY LOG - Book ID: %d, Title: '%s', Author: '%s', State Change: %s → %s",
            timestamp, book.id(), book.title(), book.author(), oldState, newState
        );
        logger.info(logEntry);
        // In a real implementation, this could write to a file or database
    }
}

