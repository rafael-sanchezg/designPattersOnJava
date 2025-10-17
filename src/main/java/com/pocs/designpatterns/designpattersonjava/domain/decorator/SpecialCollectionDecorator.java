package com.pocs.designpatterns.designpattersonjava.domain.decorator;

/**
 * Concrete decorator that adds premium/special collection status to a book.
 * Books in special collections may have restricted access or special handling requirements.
 */
public class SpecialCollectionDecorator extends BookDecorator {

    private final String collectionName;
    private final boolean requiresApproval;
    private final String location;

    /**
     * Constructor for special collection decoration.
     *
     * @param wrappedBook the book to decorate
     * @param collectionName the name of the special collection
     * @param requiresApproval whether borrowing requires approval
     * @param location the physical location of the book
     */
    public SpecialCollectionDecorator(BookComponent wrappedBook, String collectionName,
                                     boolean requiresApproval, String location) {
        super(wrappedBook);
        this.collectionName = collectionName;
        this.requiresApproval = requiresApproval;
        this.location = location;
    }

    @Override
    public String getDescription() {
        return wrappedBook.getDescription() + " [SPECIAL COLLECTION: " + collectionName + "]";
    }

    @Override
    public String getAdditionalInfo() {
        String baseInfo = wrappedBook.getAdditionalInfo();
        String collectionInfo = String.format(
            "SPECIAL COLLECTION: %s, Location: %s%s",
            collectionName,
            location,
            requiresApproval ? ", Requires approval for loan" : ""
        );

        if (!baseInfo.isEmpty()) {
            return baseInfo + " | " + collectionInfo;
        }
        return collectionInfo;
    }

    /**
     * Gets the collection name.
     *
     * @return collection name
     */
    public String getCollectionName() {
        return collectionName;
    }

    /**
     * Checks if approval is required for borrowing.
     *
     * @return true if approval required, false otherwise
     */
    public boolean requiresApproval() {
        return requiresApproval;
    }

    /**
     * Gets the physical location of the book.
     *
     * @return location
     */
    public String getLocation() {
        return location;
    }
}

