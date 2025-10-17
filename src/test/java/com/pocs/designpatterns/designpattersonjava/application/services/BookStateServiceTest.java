package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.domain.observer.BookStateObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookStateService using Observer pattern.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookStateService Tests")
class BookStateServiceTest {

    @Mock
    private BookRepositoryPort bookRepositoryPort;

    @Mock
    private BookStateObserver observer;

    private BookStateService service;

    @BeforeEach
    void setUp() {
        service = new BookStateService(bookRepositoryPort);
    }

    @Test
    @DisplayName("Should register observer successfully")
    void shouldRegisterObserverSuccessfully() {
        service.registerObserver(observer);

        assertEquals(1, service.getObserverCount());
    }

    @Test
    @DisplayName("Should not register duplicate observers")
    void shouldNotRegisterDuplicateObservers() {
        service.registerObserver(observer);
        service.registerObserver(observer);

        assertEquals(1, service.getObserverCount());
    }

    @Test
    @DisplayName("Should remove observer successfully")
    void shouldRemoveObserverSuccessfully() {
        service.registerObserver(observer);
        service.removeObserver(observer);

        assertEquals(0, service.getObserverCount());
    }

    @Test
    @DisplayName("Should update book state and notify observers")
    void shouldUpdateBookStateAndNotifyObservers() {
        Book book = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Available");
        Book updatedBook = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Loaned");

        when(bookRepositoryPort.findById(1)).thenReturn(book);
        when(bookRepositoryPort.update(any(Book.class))).thenReturn(updatedBook);

        service.registerObserver(observer);

        Book result = service.updateBookState(1, "Loaned");

        assertEquals("Loaned", result.state());
        verify(observer).onBookStateChanged(any(Book.class), eq("Available"), eq("Loaned"));
    }

    @Test
    @DisplayName("Should fail to update with invalid state")
    void shouldFailToUpdateWithInvalidState() {
        assertThrows(IllegalArgumentException.class,
            () -> service.updateBookState(1, "InvalidState"));

        verify(bookRepositoryPort, never()).findById(anyInt());
    }

    @Test
    @DisplayName("Should fail to update non-existent book")
    void shouldFailToUpdateNonExistentBook() {
        when(bookRepositoryPort.findById(999)).thenReturn(null);

        assertThrows(IllegalArgumentException.class,
            () -> service.updateBookState(999, "Loaned"));
    }

    @Test
    @DisplayName("Should not notify observers when state does not change")
    void shouldNotNotifyObserversWhenStateDoesNotChange() {
        Book book = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Available");

        when(bookRepositoryPort.findById(1)).thenReturn(book);

        service.registerObserver(observer);

        Book result = service.updateBookState(1, "Available");

        assertEquals("Available", result.state());
        verify(observer, never()).onBookStateChanged(any(), any(), any());
    }

    @Test
    @DisplayName("Should get list of observers")
    void shouldGetListOfObservers() {
        service.registerObserver(observer);

        var observers = service.getObservers();

        assertNotNull(observers);
        assertEquals(1, observers.size());
        assertTrue(observers.contains(observer));
    }

    @Test
    @DisplayName("Should notify multiple observers")
    void shouldNotifyMultipleObservers() {
        BookStateObserver observer2 = mock(BookStateObserver.class);

        Book book = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Available");
        Book updatedBook = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Loaned");

        when(bookRepositoryPort.findById(1)).thenReturn(book);
        when(bookRepositoryPort.update(any(Book.class))).thenReturn(updatedBook);

        service.registerObserver(observer);
        service.registerObserver(observer2);

        service.updateBookState(1, "Loaned");

        verify(observer).onBookStateChanged(any(Book.class), eq("Available"), eq("Loaned"));
        verify(observer2).onBookStateChanged(any(Book.class), eq("Available"), eq("Loaned"));
    }
}

