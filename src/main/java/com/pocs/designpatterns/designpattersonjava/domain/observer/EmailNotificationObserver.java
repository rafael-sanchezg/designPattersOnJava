package com.pocs.designpatterns.designpattersonjava.domain.observer;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete observer that sends email notifications when book state changes.
 * This is a demonstration implementation that logs the notification instead of sending actual emails.
 */
public class EmailNotificationObserver implements BookStateObserver {

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationObserver.class);
    private final String recipientEmail;

    public EmailNotificationObserver(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    @Override
    public void onBookStateChanged(Book book, String oldState, String newState) {
        String message = String.format(
            "Email sent to %s: Book '%s' (ID: %d) state changed from '%s' to '%s'",
            recipientEmail, book.title(), book.id(), oldState, newState
        );
        logger.info(message);
        // In a real implementation, this would send an actual email
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }
}

