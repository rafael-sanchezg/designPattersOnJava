package com.pocs.designpatterns.designpattersonjava.domain.decorator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Concrete decorator that adds loan functionality to a book.
 * Decorates a book with loan-specific information and behavior.
 */
public class LoanDecorator extends BookDecorator {

    private final String borrowerName;
    private final LocalDate loanDate;
    private final LocalDate dueDate;
    private final int renewalCount;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final int STANDARD_LOAN_DAYS = 14;
    private static final int MAX_RENEWALS = 3;

    /**
     * Constructor for a new loan.
     *
     * @param wrappedBook the book to decorate
     * @param borrowerName the name of the borrower
     */
    public LoanDecorator(BookComponent wrappedBook, String borrowerName) {
        this(wrappedBook, borrowerName, LocalDate.now(), LocalDate.now().plusDays(STANDARD_LOAN_DAYS), 0);
    }

    /**
     * Constructor with full loan details.
     *
     * @param wrappedBook the book to decorate
     * @param borrowerName the name of the borrower
     * @param loanDate the date the book was loaned
     * @param dueDate the date the book is due
     * @param renewalCount the number of times the loan has been renewed
     */
    public LoanDecorator(BookComponent wrappedBook, String borrowerName,
                        LocalDate loanDate, LocalDate dueDate, int renewalCount) {
        super(wrappedBook);
        this.borrowerName = borrowerName;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.renewalCount = renewalCount;
    }

    @Override
    public String getDescription() {
        return wrappedBook.getDescription() + " [ON LOAN]";
    }

    @Override
    public String getState() {
        return "Loaned";
    }

    @Override
    public String getAdditionalInfo() {
        String baseInfo = wrappedBook.getAdditionalInfo();
        StringBuilder loanInfo = new StringBuilder();

        if (!baseInfo.isEmpty()) {
            loanInfo.append(baseInfo).append(" | ");
        }

        loanInfo.append("LOAN DETAILS: ")
                .append("Borrower: ").append(borrowerName)
                .append(", Loaned: ").append(loanDate.format(DATE_FORMATTER))
                .append(", Due: ").append(dueDate.format(DATE_FORMATTER))
                .append(", Days remaining: ").append(getDaysUntilDue())
                .append(", Renewals: ").append(renewalCount).append("/").append(MAX_RENEWALS);

        if (isOverdue()) {
            loanInfo.append(" [OVERDUE by ").append(getDaysOverdue()).append(" days]");
        }

        return loanInfo.toString();
    }

    /**
     * Gets the borrower's name.
     *
     * @return borrower name
     */
    public String getBorrowerName() {
        return borrowerName;
    }

    /**
     * Gets the loan date.
     *
     * @return loan date
     */
    public LocalDate getLoanDate() {
        return loanDate;
    }

    /**
     * Gets the due date.
     *
     * @return due date
     */
    public LocalDate getDueDate() {
        return dueDate;
    }

    /**
     * Gets the renewal count.
     *
     * @return renewal count
     */
    public int getRenewalCount() {
        return renewalCount;
    }

    /**
     * Checks if the loan is overdue.
     *
     * @return true if overdue, false otherwise
     */
    public boolean isOverdue() {
        return LocalDate.now().isAfter(dueDate);
    }

    /**
     * Gets the number of days until the book is due.
     * Returns negative number if overdue.
     *
     * @return days until due
     */
    public long getDaysUntilDue() {
        return ChronoUnit.DAYS.between(LocalDate.now(), dueDate);
    }

    /**
     * Gets the number of days the book is overdue.
     * Returns 0 if not overdue.
     *
     * @return days overdue
     */
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    /**
     * Checks if the loan can be renewed.
     *
     * @return true if renewable, false otherwise
     */
    public boolean canRenew() {
        return renewalCount < MAX_RENEWALS && !isOverdue();
    }

    /**
     * Creates a new LoanDecorator with renewed due date.
     *
     * @return renewed loan decorator
     * @throws IllegalStateException if loan cannot be renewed
     */
    public LoanDecorator renew() {
        if (!canRenew()) {
            throw new IllegalStateException(
                "Cannot renew: " +
                (isOverdue() ? "loan is overdue" : "maximum renewals reached")
            );
        }

        LocalDate newDueDate = dueDate.plusDays(STANDARD_LOAN_DAYS);
        return new LoanDecorator(wrappedBook, borrowerName, loanDate, newDueDate, renewalCount + 1);
    }

    /**
     * Calculates the fine amount for overdue books.
     * Standard rate: $0.50 per day.
     *
     * @return fine amount
     */
    public double calculateFine() {
        if (!isOverdue()) {
            return 0.0;
        }
        final double DAILY_FINE_RATE = 0.50;
        return getDaysOverdue() * DAILY_FINE_RATE;
    }
}

