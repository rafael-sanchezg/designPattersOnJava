package com.pocs.designpatterns.designpattersonjava.infrastructure.adapters.web;

import com.pocs.designpatterns.designpattersonjava.application.services.BookLoanService;
import com.pocs.designpatterns.designpattersonjava.domain.decorator.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST controller for demonstrating the Decorator pattern with book loans.
 * Provides endpoints for loaning, returning, and managing books with decorations.
 */
@RestController
@RequestMapping("/books/decorator")
public class BookDecoratorController {

    private final BookLoanService bookLoanService;

    public BookDecoratorController(BookLoanService bookLoanService) {
        this.bookLoanService = bookLoanService;
    }

    /**
     * Loans a book to a borrower using the Decorator pattern.
     *
     * @param bookId the ID of the book to loan
     * @param borrowerName the name of the borrower
     * @return loan details
     */
    @PostMapping("/{bookId}/loan")
    public Map<String, Object> loanBook(@PathVariable int bookId, @RequestParam String borrowerName) {
        LoanDecorator loanedBook = bookLoanService.loanBook(bookId, borrowerName);
        return buildLoanResponse(loanedBook);
    }

    /**
     * Returns a loaned book.
     *
     * @param bookId the ID of the book to return
     * @return return confirmation
     */
    @PostMapping("/{bookId}/return")
    public Map<String, Object> returnBook(@PathVariable int bookId) {
        var book = bookLoanService.returnBook(bookId);
        return Map.of(
            "message", "Book returned successfully",
            "bookId", book.id(),
            "title", book.title(),
            "state", book.state()
        );
    }

    /**
     * Renews a book loan.
     *
     * @param bookId the ID of the book to renew
     * @return renewed loan details
     */
    @PostMapping("/{bookId}/renew")
    public Map<String, Object> renewLoan(@PathVariable int bookId) {
        LoanDecorator renewedLoan = bookLoanService.renewLoan(bookId);
        return buildLoanResponse(renewedLoan);
    }

    /**
     * Gets loan details for a specific book.
     *
     * @param bookId the book ID
     * @return loan details
     */
    @GetMapping("/{bookId}/loan")
    public Map<String, Object> getLoanDetails(@PathVariable int bookId) {
        LoanDecorator loan = bookLoanService.getLoanDetails(bookId);
        if (loan == null) {
            return Map.of("message", "Book is not currently on loan");
        }
        return buildLoanResponse(loan);
    }

    /**
     * Gets all active loans.
     *
     * @return list of all active loans
     */
    @GetMapping("/loans/active")
    public Map<String, Object> getAllActiveLoans() {
        List<LoanDecorator> activeLoans = bookLoanService.getAllActiveLoans();
        return Map.of(
            "totalActiveLoans", activeLoans.size(),
            "loans", activeLoans.stream()
                .map(this::buildLoanSummary)
                .collect(Collectors.toList())
        );
    }

    /**
     * Gets all overdue loans.
     *
     * @return list of overdue loans
     */
    @GetMapping("/loans/overdue")
    public Map<String, Object> getOverdueLoans() {
        List<LoanDecorator> overdueLoans = bookLoanService.getOverdueLoans();
        double totalFines = bookLoanService.calculateTotalFines();

        return Map.of(
            "totalOverdueLoans", overdueLoans.size(),
            "totalFines", String.format("$%.2f", totalFines),
            "overdueLoans", overdueLoans.stream()
                .map(this::buildOverdueLoanSummary)
                .collect(Collectors.toList())
        );
    }

    /**
     * Demonstrates decorator stacking with multiple decorations.
     *
     * @param bookId the book ID
     * @param borrowerName the borrower name
     * @param collectionName optional special collection name
     * @return decorated book details
     */
    @PostMapping("/{bookId}/loan-with-decorations")
    public Map<String, Object> loanBookWithDecorations(
            @PathVariable int bookId,
            @RequestParam String borrowerName,
            @RequestParam(required = false) String collectionName) {

        BookComponent decoratedBook = bookLoanService.loanBookWithDecorations(bookId, borrowerName, collectionName);

        Map<String, Object> response = new HashMap<>();
        response.put("bookId", decoratedBook.getBook().id());
        response.put("description", decoratedBook.getDescription());
        response.put("state", decoratedBook.getState());
        response.put("additionalInfo", decoratedBook.getAdditionalInfo());
        response.put("decorations", collectionName != null ?
            List.of("BasicBook", "SpecialCollectionDecorator", "LoanDecorator") :
            List.of("BasicBook", "LoanDecorator"));

        return response;
    }

    /**
     * Reserves a book (demonstrates decorator pattern).
     *
     * @param bookId the book ID
     * @param reservedBy the name of the person reserving
     * @param queuePosition the position in the queue
     * @return reservation details
     */
    @PostMapping("/{bookId}/reserve")
    public Map<String, Object> reserveBook(
            @PathVariable int bookId,
            @RequestParam String reservedBy,
            @RequestParam(defaultValue = "1") int queuePosition) {

        BookComponent reservedBook = bookLoanService.reserveBook(bookId, reservedBy, queuePosition);

        if (reservedBook instanceof ReservationDecorator reservation) {
            return Map.of(
                "bookId", reservation.getBook().id(),
                "title", reservation.getBook().title(),
                "description", reservation.getDescription(),
                "reservedBy", reservation.getReservedBy(),
                "queuePosition", reservation.getQueuePosition(),
                "isNextInQueue", reservation.isNextInQueue(),
                "additionalInfo", reservation.getAdditionalInfo()
            );
        }

        return Map.of("error", "Failed to create reservation");
    }

    /**
     * Adds a book to a special collection (demonstrates decorator pattern).
     *
     * @param bookId the book ID
     * @param collectionName the collection name
     * @param requiresApproval whether approval is required
     * @param location the physical location
     * @return special collection details
     */
    @PostMapping("/{bookId}/special-collection")
    public Map<String, Object> addToSpecialCollection(
            @PathVariable int bookId,
            @RequestParam String collectionName,
            @RequestParam(defaultValue = "true") boolean requiresApproval,
            @RequestParam(defaultValue = "Special Collections Room") String location) {

        BookComponent specialBook = bookLoanService.addToSpecialCollection(
            bookId, collectionName, requiresApproval, location);

        if (specialBook instanceof SpecialCollectionDecorator special) {
            return Map.of(
                "bookId", special.getBook().id(),
                "title", special.getBook().title(),
                "description", special.getDescription(),
                "collectionName", special.getCollectionName(),
                "requiresApproval", special.requiresApproval(),
                "location", special.getLocation(),
                "additionalInfo", special.getAdditionalInfo()
            );
        }

        return Map.of("error", "Failed to add to special collection");
    }

    /**
     * Gets information about the Decorator pattern implementation.
     *
     * @return pattern information
     */
    @GetMapping("/info")
    public Map<String, Object> getDecoratorPatternInfo() {
        return Map.of(
            "pattern", "Decorator Pattern",
            "description", "Adds functionality to books dynamically without modifying the Book class",
            "decorators", Map.of(
                "LoanDecorator", "Adds loan functionality with due dates, renewals, and fine calculation",
                "ReservationDecorator", "Adds reservation functionality with queue management",
                "SpecialCollectionDecorator", "Adds special collection status with restricted access"
            ),
            "features", List.of(
                "Dynamic decoration - add functionality at runtime",
                "Decorator stacking - multiple decorators can be applied to the same book",
                "Transparent to clients - decorated objects implement the same interface",
                "Integrated with Observer pattern - state changes trigger notifications"
            ),
            "endpoints", Map.of(
                "loanBook", "POST /books/decorator/{bookId}/loan?borrowerName=John",
                "returnBook", "POST /books/decorator/{bookId}/return",
                "renewLoan", "POST /books/decorator/{bookId}/renew",
                "getLoanDetails", "GET /books/decorator/{bookId}/loan",
                "activeLoans", "GET /books/decorator/loans/active",
                "overdueLoans", "GET /books/decorator/loans/overdue",
                "reserveBook", "POST /books/decorator/{bookId}/reserve?reservedBy=Jane&queuePosition=1",
                "specialCollection", "POST /books/decorator/{bookId}/special-collection?collectionName=Rare Books"
            )
        );
    }

    /**
     * Demonstrates the full decorator pattern with multiple scenarios.
     *
     * @return demonstration results
     */
    @PostMapping("/demo")
    public Map<String, Object> demonstrateDecoratorPattern() {
        Map<String, Object> results = new HashMap<>();

        try {
            // Demo 1: Simple loan
            LoanDecorator loan1 = bookLoanService.loanBook(1, "Alice Johnson");
            results.put("demo1_simpleLoan", buildLoanSummary(loan1));

            // Demo 2: Loan with special collection
            BookComponent loan2 = bookLoanService.loanBookWithDecorations(2, "Bob Smith", "First Editions");
            results.put("demo2_decoratedLoan", Map.of(
                "description", loan2.getDescription(),
                "additionalInfo", loan2.getAdditionalInfo()
            ));

            // Demo 3: Reservation
            BookComponent reservation = bookLoanService.reserveBook(4, "Charlie Brown", 2);
            results.put("demo3_reservation", Map.of(
                "description", reservation.getDescription(),
                "additionalInfo", reservation.getAdditionalInfo()
            ));

            // Demo 4: Special collection
            BookComponent special = bookLoanService.addToSpecialCollection(
                5, "Rare Manuscripts", true, "Vault Level 3");
            results.put("demo4_specialCollection", Map.of(
                "description", special.getDescription(),
                "additionalInfo", special.getAdditionalInfo()
            ));

            results.put("message", "Decorator pattern demonstration completed successfully");
            results.put("activeLoans", bookLoanService.getAllActiveLoans().size());

        } catch (Exception e) {
            results.put("error", e.getMessage());
        }

        return results;
    }

    // Helper methods

    private Map<String, Object> buildLoanResponse(LoanDecorator loan) {
        Map<String, Object> response = new HashMap<>();
        response.put("bookId", loan.getBook().id());
        response.put("title", loan.getBook().title());
        response.put("author", loan.getBook().author());
        response.put("description", loan.getDescription());
        response.put("state", loan.getState());
        response.put("borrowerName", loan.getBorrowerName());
        response.put("loanDate", loan.getLoanDate().toString());
        response.put("dueDate", loan.getDueDate().toString());
        response.put("daysUntilDue", loan.getDaysUntilDue());
        response.put("renewalCount", loan.getRenewalCount());
        response.put("canRenew", loan.canRenew());
        response.put("isOverdue", loan.isOverdue());

        if (loan.isOverdue()) {
            response.put("daysOverdue", loan.getDaysOverdue());
            response.put("fine", String.format("$%.2f", loan.calculateFine()));
        }

        response.put("additionalInfo", loan.getAdditionalInfo());
        return response;
    }

    private Map<String, Object> buildLoanSummary(LoanDecorator loan) {
        return Map.of(
            "bookId", loan.getBook().id(),
            "title", loan.getBook().title(),
            "borrowerName", loan.getBorrowerName(),
            "dueDate", loan.getDueDate().toString(),
            "daysUntilDue", loan.getDaysUntilDue(),
            "isOverdue", loan.isOverdue()
        );
    }

    private Map<String, Object> buildOverdueLoanSummary(LoanDecorator loan) {
        return Map.of(
            "bookId", loan.getBook().id(),
            "title", loan.getBook().title(),
            "borrowerName", loan.getBorrowerName(),
            "dueDate", loan.getDueDate().toString(),
            "daysOverdue", loan.getDaysOverdue(),
            "fine", String.format("$%.2f", loan.calculateFine())
        );
    }
}

