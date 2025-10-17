package com.pocs.designpatterns.designpattersonjava.domain.adapter;

/**
 * Legacy book class from an old system with incompatible method names and structure.
 * This class represents books from a legacy library system that needs to be integrated
 * into the new system without modifying the legacy code.
 *
 * Note: This class uses old naming conventions and different method signatures.
 */
public class LegacyBook {

    private int bookId;
    private String bookName;
    private String writerName;
    private String category;        // "F" for Fiction, "NF" for Non-Fiction
    private String mediaType;       // "P" for Physical, "D" for Digital
    private int availabilityStatus; // 1 = Available, 0 = Not Available

    public LegacyBook(int bookId, String bookName, String writerName,
                     String category, String mediaType, int availabilityStatus) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.writerName = writerName;
        this.category = category;
        this.mediaType = mediaType;
        this.availabilityStatus = availabilityStatus;
    }

    // Legacy getter methods with non-standard names

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public int getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(int availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    /**
     * Legacy method to print book information.
     */
    public void printBookDetails() {
        System.out.println("Legacy Book: " + bookName + " by " + writerName);
    }

    /**
     * Legacy method to check availability with different logic.
     *
     * @return 1 if available, 0 if not
     */
    public int checkAvailability() {
        return availabilityStatus;
    }
}

