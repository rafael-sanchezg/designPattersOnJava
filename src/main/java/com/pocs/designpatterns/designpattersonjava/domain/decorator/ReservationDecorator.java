package com.pocs.designpatterns.designpattersonjava.domain.decorator;

/**
 * Concrete decorator that adds reservation functionality to a book.
 * Decorates a book with reservation-specific information.
 */
public class ReservationDecorator extends BookDecorator {

    private final String reservedBy;
    private final int queuePosition;

    /**
     * Constructor for a book reservation.
     *
     * @param wrappedBook the book to decorate
     * @param reservedBy the name of the person who reserved the book
     * @param queuePosition the position in the reservation queue
     */
    public ReservationDecorator(BookComponent wrappedBook, String reservedBy, int queuePosition) {
        super(wrappedBook);
        this.reservedBy = reservedBy;
        this.queuePosition = queuePosition;
    }

    @Override
    public String getDescription() {
        return wrappedBook.getDescription() + " [RESERVED]";
    }

    @Override
    public String getAdditionalInfo() {
        String baseInfo = wrappedBook.getAdditionalInfo();
        String reservationInfo = String.format("RESERVATION: Reserved by %s, Queue position: %d",
            reservedBy, queuePosition);

        if (!baseInfo.isEmpty()) {
            return baseInfo + " | " + reservationInfo;
        }
        return reservationInfo;
    }

    /**
     * Gets the name of the person who reserved the book.
     *
     * @return reserved by name
     */
    public String getReservedBy() {
        return reservedBy;
    }

    /**
     * Gets the queue position.
     *
     * @return queue position
     */
    public int getQueuePosition() {
        return queuePosition;
    }

    /**
     * Checks if this is the next reservation in queue.
     *
     * @return true if next in queue, false otherwise
     */
    public boolean isNextInQueue() {
        return queuePosition == 1;
    }
}

