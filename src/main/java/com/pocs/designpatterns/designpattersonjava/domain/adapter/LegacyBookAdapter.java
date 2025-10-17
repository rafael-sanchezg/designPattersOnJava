package com.pocs.designpatterns.designpattersonjava.domain.adapter;

/**
 * Adapter that makes LegacyBook compatible with the IBook interface.
 * This adapter translates legacy method calls and data formats to the new system's interface.
 *
 * Adapter Pattern: Converts the interface of a class into another interface that clients expect.
 * Allows classes with incompatible interfaces to work together.
 */
public class LegacyBookAdapter implements IBook {

    private final LegacyBook legacyBook;

    /**
     * Constructor that wraps a legacy book.
     *
     * @param legacyBook the legacy book to adapt
     */
    public LegacyBookAdapter(LegacyBook legacyBook) {
        if (legacyBook == null) {
            throw new IllegalArgumentException("Legacy book cannot be null");
        }
        this.legacyBook = legacyBook;
    }

    @Override
    public int getId() {
        // Adapt: bookId → id
        return legacyBook.getBookId();
    }

    @Override
    public String getTitle() {
        // Adapt: bookName → title
        return legacyBook.getBookName();
    }

    @Override
    public String getAuthor() {
        // Adapt: writerName → author
        return legacyBook.getWriterName();
    }

    @Override
    public String getType() {
        // Adapt: category codes to full names
        // "F" → "Fiction", "NF" → "No Fiction"
        String category = legacyBook.getCategory();
        return switch (category) {
            case "F" -> "Fiction";
            case "NF" -> "No Fiction";
            default -> "Unknown";
        };
    }

    @Override
    public String getFormat() {
        // Adapt: mediaType codes to full names
        // "P" → "Physical", "D" → "Digital"
        String mediaType = legacyBook.getMediaType();
        return switch (mediaType) {
            case "P" -> "Physical";
            case "D" -> "Digital";
            default -> "Unknown";
        };
    }

    @Override
    public String getState() {
        // Adapt: availabilityStatus (int) to state (String)
        // 1 → "Available", 0 → "Loaned"
        return legacyBook.getAvailabilityStatus() == 1 ? "Available" : "Loaned";
    }

    @Override
    public void setState(String state) {
        // Adapt: state (String) to availabilityStatus (int)
        // "Available" → 1, "Loaned" → 0
        int status = "Available".equalsIgnoreCase(state) ? 1 : 0;
        legacyBook.setAvailabilityStatus(status);
    }

    @Override
    public String getDisplayInfo() {
        // Provide formatted information using new system's format
        return String.format("Book[id=%d, title='%s', author='%s', type='%s', format='%s', state='%s']",
            getId(), getTitle(), getAuthor(), getType(), getFormat(), getState());
    }

    @Override
    public boolean isAvailable() {
        // Adapt: checkAvailability() returns 1/0 → convert to boolean
        return legacyBook.checkAvailability() == 1;
    }

    /**
     * Gets the wrapped legacy book instance.
     * Useful for scenarios where direct access to legacy book is needed.
     *
     * @return the underlying legacy book
     */
    public LegacyBook getLegacyBook() {
        return legacyBook;
    }

    @Override
    public String toString() {
        return getDisplayInfo();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof IBook other)) return false;
        return getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(getId());
    }
}

