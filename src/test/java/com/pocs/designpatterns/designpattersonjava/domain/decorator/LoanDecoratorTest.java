package com.pocs.designpatterns.designpattersonjava.domain.decorator;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LoanDecorator.
 */
@DisplayName("LoanDecorator Tests")
class LoanDecoratorTest {

    private Book book;
    private BasicBook basicBook;
    private LoanDecorator loanDecorator;

    @BeforeEach
    void setUp() {
        book = new Book(1, "Clean Code", "Robert Martin", "No Fiction", "Physical", "Available");
        basicBook = new BasicBook(book);
        loanDecorator = new LoanDecorator(basicBook, "Alice Johnson");
    }

    @Test
    @DisplayName("Should create loan with default 14-day period")
    void shouldCreateLoanWithDefault14DayPeriod() {
        LocalDate expectedDueDate = LocalDate.now().plusDays(14);

        assertEquals("Alice Johnson", loanDecorator.getBorrowerName());
        assertEquals(LocalDate.now(), loanDecorator.getLoanDate());
        assertEquals(expectedDueDate, loanDecorator.getDueDate());
        assertEquals(0, loanDecorator.getRenewalCount());
    }

    @Test
    @DisplayName("Should modify description to indicate loan")
    void shouldModifyDescriptionToIndicateLoan() {
        String description = loanDecorator.getDescription();

        assertTrue(description.contains("Clean Code"));
        assertTrue(description.contains("[ON LOAN]"));
    }

    @Test
    @DisplayName("Should return Loaned state")
    void shouldReturnLoanedState() {
        assertEquals("Loaned", loanDecorator.getState());
    }

    @Test
    @DisplayName("Should provide detailed loan information")
    void shouldProvideDetailedLoanInformation() {
        String info = loanDecorator.getAdditionalInfo();

        assertTrue(info.contains("LOAN DETAILS"));
        assertTrue(info.contains("Alice Johnson"));
        assertTrue(info.contains("Days remaining: 14"));
        assertTrue(info.contains("Renewals: 0/3"));
    }

    @Test
    @DisplayName("Should not be overdue initially")
    void shouldNotBeOverdueInitially() {
        assertFalse(loanDecorator.isOverdue());
        assertEquals(0, loanDecorator.getDaysOverdue());
    }

    @Test
    @DisplayName("Should calculate days until due correctly")
    void shouldCalculateDaysUntilDueCorrectly() {
        assertEquals(14, loanDecorator.getDaysUntilDue());
    }

    @Test
    @DisplayName("Should detect overdue books")
    void shouldDetectOverdueBooks() {
        LocalDate pastDate = LocalDate.now().minusDays(20);
        LocalDate pastDueDate = LocalDate.now().minusDays(5);

        LoanDecorator overdueLoan = new LoanDecorator(
            basicBook, "Bob Smith", pastDate, pastDueDate, 0
        );

        assertTrue(overdueLoan.isOverdue());
        assertEquals(5, overdueLoan.getDaysOverdue());
        assertTrue(overdueLoan.getDaysUntilDue() < 0);
    }

    @Test
    @DisplayName("Should calculate fines for overdue books")
    void shouldCalculateFinesForOverdueBooks() {
        LocalDate pastDate = LocalDate.now().minusDays(20);
        LocalDate pastDueDate = LocalDate.now().minusDays(5);

        LoanDecorator overdueLoan = new LoanDecorator(
            basicBook, "Bob Smith", pastDate, pastDueDate, 0
        );

        double expectedFine = 5 * 0.50; // 5 days * $0.50
        assertEquals(expectedFine, overdueLoan.calculateFine(), 0.01);
    }

    @Test
    @DisplayName("Should return zero fine for non-overdue books")
    void shouldReturnZeroFineForNonOverdueBooks() {
        assertEquals(0.0, loanDecorator.calculateFine(), 0.01);
    }

    @Test
    @DisplayName("Should allow renewal when not overdue and under max renewals")
    void shouldAllowRenewalWhenNotOverdueAndUnderMaxRenewals() {
        assertTrue(loanDecorator.canRenew());
    }

    @Test
    @DisplayName("Should not allow renewal when overdue")
    void shouldNotAllowRenewalWhenOverdue() {
        LocalDate pastDate = LocalDate.now().minusDays(20);
        LocalDate pastDueDate = LocalDate.now().minusDays(5);

        LoanDecorator overdueLoan = new LoanDecorator(
            basicBook, "Bob Smith", pastDate, pastDueDate, 0
        );

        assertFalse(overdueLoan.canRenew());
    }

    @Test
    @DisplayName("Should not allow renewal when max renewals reached")
    void shouldNotAllowRenewalWhenMaxRenewalsReached() {
        LoanDecorator maxRenewed = new LoanDecorator(
            basicBook, "Charlie Brown",
            LocalDate.now(), LocalDate.now().plusDays(14), 3
        );

        assertFalse(maxRenewed.canRenew());
    }

    @Test
    @DisplayName("Should renew loan successfully")
    void shouldRenewLoanSuccessfully() {
        LoanDecorator renewed = loanDecorator.renew();

        assertEquals("Alice Johnson", renewed.getBorrowerName());
        assertEquals(loanDecorator.getLoanDate(), renewed.getLoanDate());
        assertEquals(loanDecorator.getDueDate().plusDays(14), renewed.getDueDate());
        assertEquals(1, renewed.getRenewalCount());
    }

    @Test
    @DisplayName("Should fail to renew when overdue")
    void shouldFailToRenewWhenOverdue() {
        LocalDate pastDate = LocalDate.now().minusDays(20);
        LocalDate pastDueDate = LocalDate.now().minusDays(5);

        LoanDecorator overdueLoan = new LoanDecorator(
            basicBook, "Bob Smith", pastDate, pastDueDate, 0
        );

        assertThrows(IllegalStateException.class, overdueLoan::renew);
    }

    @Test
    @DisplayName("Should fail to renew when max renewals reached")
    void shouldFailToRenewWhenMaxRenewalsReached() {
        LoanDecorator maxRenewed = new LoanDecorator(
            basicBook, "Charlie Brown",
            LocalDate.now(), LocalDate.now().plusDays(14), 3
        );

        assertThrows(IllegalStateException.class, maxRenewed::renew);
    }

    @Test
    @DisplayName("Should maintain underlying book reference")
    void shouldMaintainUnderlyingBookReference() {
        assertEquals(book, loanDecorator.getBook());
    }

    @Test
    @DisplayName("Should show overdue information in additional info")
    void shouldShowOverdueInformationInAdditionalInfo() {
        LocalDate pastDate = LocalDate.now().minusDays(20);
        LocalDate pastDueDate = LocalDate.now().minusDays(5);

        LoanDecorator overdueLoan = new LoanDecorator(
            basicBook, "Bob Smith", pastDate, pastDueDate, 0
        );

        String info = overdueLoan.getAdditionalInfo();
        assertTrue(info.contains("[OVERDUE"));
        assertTrue(info.contains("5 days]"));
    }
}
