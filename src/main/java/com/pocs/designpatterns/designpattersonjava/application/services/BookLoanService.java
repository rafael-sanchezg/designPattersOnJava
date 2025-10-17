package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.decorator.*;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.domain.observer.BookStateObserver;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for managing book loans using the Decorator pattern.
 * Integrates with Observer pattern to notify state changes.
 */
@Service
public class BookLoanService {

    private final BookRepositoryPort bookRepositoryPort;
    private final BookStateService bookStateService;

    // Store active loans (in a real application, this would be in the database)
    private final Map<Integer, LoanDecorator> activeLoans = new ConcurrentHashMap<>();

    public BookLoanService(BookRepositoryPort bookRepositoryPort, BookStateService bookStateService) {
        this.bookRepositoryPort = bookRepositoryPort;
        this.bookStateService = bookStateService;
    }

    /**
     * Loans a book to a borrower using the Decorator pattern.
     *
     * @param bookId the ID of the book to loan
     * @param borrowerName the name of the borrower
     * @return the decorated book with loan information
     * @throws IllegalStateException if book is not available
     */
    public LoanDecorator loanBook(int bookId, String borrowerName) {
        Book book = bookRepositoryPort.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found with ID: " + bookId);
        }

        if (!"Available".equals(book.state())) {
            throw new IllegalStateException("Book is not available for loan. Current state: " + book.state());
        }

        // Create decorated book with loan functionality
        BookComponent basicBook = new BasicBook(book);
        LoanDecorator loanedBook = new LoanDecorator(basicBook, borrowerName);

        // Update book state in database (triggers observers)
        bookStateService.updateBookState(bookId, "Loaned");

        // Store the loan
        activeLoans.put(bookId, loanedBook);

        return loanedBook;
    }

    /**
     * Returns a loaned book.
     *
     * @param bookId the ID of the book to return
     * @return the returned book
     * @throws IllegalStateException if book is not on loan
     */
    public Book returnBook(int bookId) {
        LoanDecorator loan = activeLoans.get(bookId);
        if (loan == null) {
            throw new IllegalStateException("Book is not currently on loan");
        }

        // Remove from active loans
        activeLoans.remove(bookId);

        // Update book state in database (triggers observers)
        Book returnedBook = bookStateService.updateBookState(bookId, "Available");

        return returnedBook;
    }

    /**
     * Renews a book loan.
     *
     * @param bookId the ID of the book to renew
     * @return the renewed loan decorator
     * @throws IllegalStateException if book cannot be renewed
     */
    public LoanDecorator renewLoan(int bookId) {
        LoanDecorator currentLoan = activeLoans.get(bookId);
        if (currentLoan == null) {
            throw new IllegalStateException("No active loan found for book ID: " + bookId);
        }

        LoanDecorator renewedLoan = currentLoan.renew();
        activeLoans.put(bookId, renewedLoan);

        return renewedLoan;
    }

    /**
     * Gets the loan details for a book.
     *
     * @param bookId the book ID
     * @return the loan decorator, or null if not on loan
     */
    public LoanDecorator getLoanDetails(int bookId) {
        return activeLoans.get(bookId);
    }

    /**
     * Gets all active loans.
     *
     * @return list of all active loans
     */
    public List<LoanDecorator> getAllActiveLoans() {
        return new ArrayList<>(activeLoans.values());
    }

    /**
     * Gets all overdue loans.
     *
     * @return list of overdue loans
     */
    public List<LoanDecorator> getOverdueLoans() {
        return activeLoans.values().stream()
                .filter(LoanDecorator::isOverdue)
                .toList();
    }

    /**
     * Calculates total fines for all overdue books.
     *
     * @return total fine amount
     */
    public double calculateTotalFines() {
        return getOverdueLoans().stream()
                .mapToDouble(LoanDecorator::calculateFine)
                .sum();
    }

    /**
     * Demonstrates decorator stacking by adding multiple decorations to a book.
     *
     * @param bookId the book ID
     * @param borrowerName the borrower name
     * @param collectionName optional special collection name
     * @return decorated book with multiple functionalities
     */
    public BookComponent loanBookWithDecorations(int bookId, String borrowerName, String collectionName) {
        Book book = bookRepositoryPort.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found with ID: " + bookId);
        }

        // Start with basic book
        BookComponent decorated = new BasicBook(book);

        // Add special collection decorator if specified
        if (collectionName != null && !collectionName.isEmpty()) {
            decorated = new SpecialCollectionDecorator(decorated, collectionName, true, "Special Collections Room");
        }

        // Add loan decorator
        LoanDecorator loanDecorator = new LoanDecorator(decorated, borrowerName);

        // Update state
        bookStateService.updateBookState(bookId, "Loaned");
        activeLoans.put(bookId, loanDecorator);

        return loanDecorator;
    }

    /**
     * Creates a reserved book (demonstrates decorator pattern).
     *
     * @param bookId the book ID
     * @param reservedBy the name of the person reserving
     * @param queuePosition the position in the queue
     * @return decorated book with reservation
     */
    public BookComponent reserveBook(int bookId, String reservedBy, int queuePosition) {
        Book book = bookRepositoryPort.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found with ID: " + bookId);
        }

        BookComponent basicBook = new BasicBook(book);
        return new ReservationDecorator(basicBook, reservedBy, queuePosition);
    }

    /**
     * Adds a book to a special collection (demonstrates decorator pattern).
     *
     * @param bookId the book ID
     * @param collectionName the collection name
     * @param requiresApproval whether approval is required
     * @param location the physical location
     * @return decorated book with special collection status
     */
    public BookComponent addToSpecialCollection(int bookId, String collectionName,
                                               boolean requiresApproval, String location) {
        Book book = bookRepositoryPort.findById(bookId);
        if (book == null) {
            throw new IllegalArgumentException("Book not found with ID: " + bookId);
        }

        BookComponent basicBook = new BasicBook(book);
        return new SpecialCollectionDecorator(basicBook, collectionName, requiresApproval, location);
    }
}

