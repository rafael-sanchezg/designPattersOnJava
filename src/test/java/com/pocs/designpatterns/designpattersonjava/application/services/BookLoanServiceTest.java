package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.decorator.LoanDecorator;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookLoanService using Decorator pattern.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookLoanService Tests")
class BookLoanServiceTest {

    @Mock
    private BookRepositoryPort bookRepositoryPort;

    @Mock
    private BookStateService bookStateService;

    private BookLoanService service;

    @BeforeEach
    void setUp() {
        service = new BookLoanService(bookRepositoryPort, bookStateService);
    }

    @Test
    @DisplayName("Should loan available book successfully")
    void shouldLoanAvailableBookSuccessfully() {
        Book availableBook = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Available");

        when(bookRepositoryPort.findById(1)).thenReturn(availableBook);
        when(bookStateService.updateBookState(1, "Loaned")).thenReturn(
            new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Loaned"));

        LoanDecorator loan = service.loanBook(1, "Alice Johnson");

        assertNotNull(loan);
        assertEquals("Alice Johnson", loan.getBorrowerName());
        assertEquals("Clean Code", loan.getBook().title());
        verify(bookStateService).updateBookState(1, "Loaned");
    }

    @Test
    @DisplayName("Should fail to loan book that is not available")
    void shouldFailToLoanBookThatIsNotAvailable() {
        Book loanedBook = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Loaned");

        when(bookRepositoryPort.findById(1)).thenReturn(loanedBook);

        assertThrows(IllegalStateException.class, () -> service.loanBook(1, "Alice"));
        verify(bookStateService, never()).updateBookState(anyInt(), anyString());
    }

    @Test
    @DisplayName("Should fail to loan non-existent book")
    void shouldFailToLoanNonExistentBook() {
        when(bookRepositoryPort.findById(999)).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> service.loanBook(999, "Alice"));
    }

    @Test
    @DisplayName("Should return book successfully")
    void shouldReturnBookSuccessfully() {
        Book availableBook = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Available");

        when(bookRepositoryPort.findById(1)).thenReturn(availableBook);
        when(bookStateService.updateBookState(1, "Loaned")).thenReturn(
            new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Loaned"));
        when(bookStateService.updateBookState(1, "Available")).thenReturn(availableBook);

        // First loan the book
        service.loanBook(1, "Alice");

        // Then return it
        Book returned = service.returnBook(1);

        assertNotNull(returned);
        assertEquals("Available", returned.state());
        verify(bookStateService).updateBookState(1, "Available");
    }

    @Test
    @DisplayName("Should fail to return book not on loan")
    void shouldFailToReturnBookNotOnLoan() {
        assertThrows(IllegalStateException.class, () -> service.returnBook(999));
    }

    @Test
    @DisplayName("Should renew loan successfully")
    void shouldRenewLoanSuccessfully() {
        Book availableBook = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Available");

        when(bookRepositoryPort.findById(1)).thenReturn(availableBook);
        when(bookStateService.updateBookState(1, "Loaned")).thenReturn(
            new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Loaned"));

        // Loan the book first
        service.loanBook(1, "Alice");

        // Then renew it
        LoanDecorator renewed = service.renewLoan(1);

        assertNotNull(renewed);
        assertEquals(1, renewed.getRenewalCount());
    }

    @Test
    @DisplayName("Should get active loans")
    void shouldGetActiveLoans() {
        Book book1 = new Book(1, "Book1", "Author1", "Fiction", "Physical", "Available");
        Book book2 = new Book(2, "Book2", "Author2", "Fiction", "Physical", "Available");

        when(bookRepositoryPort.findById(1)).thenReturn(book1);
        when(bookRepositoryPort.findById(2)).thenReturn(book2);
        when(bookStateService.updateBookState(anyInt(), anyString())).thenReturn(book1, book2);

        service.loanBook(1, "Alice");
        service.loanBook(2, "Bob");

        var activeLoans = service.getAllActiveLoans();

        assertEquals(2, activeLoans.size());
    }

    @Test
    @DisplayName("Should get overdue loans")
    void shouldGetOverdueLoans() {
        var overdueLoans = service.getOverdueLoans();

        assertNotNull(overdueLoans);
        // Initially should be empty
        assertTrue(overdueLoans.isEmpty());
    }

    @Test
    @DisplayName("Should calculate total fines")
    void shouldCalculateTotalFines() {
        double totalFines = service.calculateTotalFines();

        assertTrue(totalFines >= 0);
    }

    @Test
    @DisplayName("Should get loan details")
    void shouldGetLoanDetails() {
        Book book = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Available");

        when(bookRepositoryPort.findById(1)).thenReturn(book);
        when(bookStateService.updateBookState(1, "Loaned")).thenReturn(
            new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Loaned"));

        service.loanBook(1, "Alice");

        LoanDecorator loan = service.getLoanDetails(1);

        assertNotNull(loan);
        assertEquals("Alice", loan.getBorrowerName());
    }

    @Test
    @DisplayName("Should return null for loan details when book not loaned")
    void shouldReturnNullForLoanDetailsWhenBookNotLoaned() {
        LoanDecorator loan = service.getLoanDetails(999);

        assertNull(loan);
    }
}

