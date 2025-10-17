package com.pocs.designpatterns.designpattersonjava.domain.observer;

/**
 * Subject interface for managing observers.
 * Classes that want to notify observers about state changes should implement this interface.
 */
public interface BookStateSubject {

    /**
     * Registers an observer to receive notifications.
     *
     * @param observer the observer to register
     */
    void registerObserver(BookStateObserver observer);

    /**
     * Removes an observer from the notification list.
     *
     * @param observer the observer to remove
     */
    void removeObserver(BookStateObserver observer);

    /**
     * Notifies all registered observers about a state change.
     */
    void notifyObservers();
}

