package com.pocs.designpatterns.designpattersonjava.domain.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for LegacyBookAdapter.
 */
@DisplayName("LegacyBookAdapter Tests")
class LegacyBookAdapterTest {

    private LegacyBook legacyBook;
    private LegacyBookAdapter adapter;

    @BeforeEach
    void setUp() {
        legacyBook = new LegacyBook(
            101,
            "Introduction to Algorithms",
            "Cormen, Leiserson",
            "NF",
            "P",
            1
        );
        adapter = new LegacyBookAdapter(legacyBook);
    }

    @Test
    @DisplayName("Should fail when legacy book is null")
    void shouldFailWhenLegacyBookIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new LegacyBookAdapter(null));
    }

    @Test
    @DisplayName("Should adapt book ID correctly")
    void shouldAdaptBookIdCorrectly() {
        assertEquals(101, adapter.getId());
    }

    @Test
    @DisplayName("Should adapt book name to title")
    void shouldAdaptBookNameToTitle() {
        assertEquals("Introduction to Algorithms", adapter.getTitle());
    }

    @Test
    @DisplayName("Should adapt writer name to author")
    void shouldAdaptWriterNameToAuthor() {
        assertEquals("Cormen, Leiserson", adapter.getAuthor());
    }

    @Test
    @DisplayName("Should adapt Fiction category")
    void shouldAdaptFictionCategory() {
        LegacyBook fiction = new LegacyBook(1, "1984", "Orwell", "F", "D", 1);
        LegacyBookAdapter fictionAdapter = new LegacyBookAdapter(fiction);

        assertEquals("Fiction", fictionAdapter.getType());
    }

    @Test
    @DisplayName("Should adapt Non-Fiction category")
    void shouldAdaptNonFictionCategory() {
        assertEquals("No Fiction", adapter.getType());
    }

    @Test
    @DisplayName("Should return Unknown for invalid category")
    void shouldReturnUnknownForInvalidCategory() {
        LegacyBook invalid = new LegacyBook(1, "Test", "Author", "X", "P", 1);
        LegacyBookAdapter invalidAdapter = new LegacyBookAdapter(invalid);

        assertEquals("Unknown", invalidAdapter.getType());
    }

    @Test
    @DisplayName("Should adapt Physical media type")
    void shouldAdaptPhysicalMediaType() {
        assertEquals("Physical", adapter.getFormat());
    }

    @Test
    @DisplayName("Should adapt Digital media type")
    void shouldAdaptDigitalMediaType() {
        LegacyBook digital = new LegacyBook(1, "Book", "Author", "F", "D", 1);
        LegacyBookAdapter digitalAdapter = new LegacyBookAdapter(digital);

        assertEquals("Digital", digitalAdapter.getFormat());
    }

    @Test
    @DisplayName("Should return Unknown for invalid media type")
    void shouldReturnUnknownForInvalidMediaType() {
        LegacyBook invalid = new LegacyBook(1, "Test", "Author", "F", "X", 1);
        LegacyBookAdapter invalidAdapter = new LegacyBookAdapter(invalid);

        assertEquals("Unknown", invalidAdapter.getFormat());
    }

    @Test
    @DisplayName("Should adapt availability status 1 to Available")
    void shouldAdaptAvailabilityStatus1ToAvailable() {
        assertEquals("Available", adapter.getState());
        assertTrue(adapter.isAvailable());
    }

    @Test
    @DisplayName("Should adapt availability status 0 to Loaned")
    void shouldAdaptAvailabilityStatus0ToLoaned() {
        LegacyBook loaned = new LegacyBook(1, "Book", "Author", "F", "P", 0);
        LegacyBookAdapter loanedAdapter = new LegacyBookAdapter(loaned);

        assertEquals("Loaned", loanedAdapter.getState());
        assertFalse(loanedAdapter.isAvailable());
    }

    @Test
    @DisplayName("Should set state from Available to Loaned")
    void shouldSetStateFromAvailableToLoaned() {
        adapter.setState("Loaned");

        assertEquals("Loaned", adapter.getState());
        assertEquals(0, legacyBook.getAvailabilityStatus());
        assertFalse(adapter.isAvailable());
    }

    @Test
    @DisplayName("Should set state from Loaned to Available")
    void shouldSetStateFromLoanedToAvailable() {
        adapter.setState("Loaned");
        adapter.setState("Available");

        assertEquals("Available", adapter.getState());
        assertEquals(1, legacyBook.getAvailabilityStatus());
        assertTrue(adapter.isAvailable());
    }

    @Test
    @DisplayName("Should provide formatted display info")
    void shouldProvideFormattedDisplayInfo() {
        String displayInfo = adapter.getDisplayInfo();

        assertTrue(displayInfo.contains("101"));
        assertTrue(displayInfo.contains("Introduction to Algorithms"));
        assertTrue(displayInfo.contains("Cormen, Leiserson"));
        assertTrue(displayInfo.contains("No Fiction"));
        assertTrue(displayInfo.contains("Physical"));
        assertTrue(displayInfo.contains("Available"));
    }

    @Test
    @DisplayName("Should return underlying legacy book")
    void shouldReturnUnderlyingLegacyBook() {
        assertEquals(legacyBook, adapter.getLegacyBook());
    }

    @Test
    @DisplayName("Should implement equals correctly")
    void shouldImplementEqualsCorrectly() {
        LegacyBook anotherLegacy = new LegacyBook(101, "Different", "Different", "F", "D", 0);
        LegacyBookAdapter anotherAdapter = new LegacyBookAdapter(anotherLegacy);

        assertEquals(adapter, anotherAdapter); // Same ID

        LegacyBook differentIdLegacy = new LegacyBook(102, "Test", "Test", "F", "P", 1);
        LegacyBookAdapter differentAdapter = new LegacyBookAdapter(differentIdLegacy);

        assertNotEquals(adapter, differentAdapter); // Different ID
    }

    @Test
    @DisplayName("Should implement hashCode correctly")
    void shouldImplementHashCodeCorrectly() {
        LegacyBook anotherLegacy = new LegacyBook(101, "Different", "Different", "F", "D", 0);
        LegacyBookAdapter anotherAdapter = new LegacyBookAdapter(anotherLegacy);

        assertEquals(adapter.hashCode(), anotherAdapter.hashCode());
    }

    @Test
    @DisplayName("Should provide meaningful toString")
    void shouldProvideMeaningfulToString() {
        String toString = adapter.toString();

        assertTrue(toString.contains("101"));
        assertTrue(toString.contains("Introduction to Algorithms"));
    }
}

