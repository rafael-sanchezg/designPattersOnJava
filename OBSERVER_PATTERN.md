# Observer Pattern Implementation

## Overview
The Observer Pattern has been successfully implemented to notify observers when a book's state changes (e.g., from "Available" to "Loaned").

## Pattern Components

### 1. Subject Interface (`BookStateSubject`)
Defines methods for registering, removing, and notifying observers.

### 2. Observer Interface (`BookStateObserver`)
Defines the contract for observers that want to be notified of book state changes.

### 3. Concrete Observers

#### EmailNotificationObserver
- Sends email notifications when a book's state changes
- Logs notification details (in production, would send actual emails)
- Can be registered with custom email addresses

#### InventoryLogObserver
- Maintains an audit trail of all state changes
- Logs with timestamp, book details, and state transition
- Useful for compliance and tracking purposes

#### StatisticsObserver
- Tracks statistics about state transitions
- Maintains counters for different types of transitions
- Provides metrics about total transitions

### 4. Service (`BookStateService`)
- Implements `BookStateSubject`
- Manages observer registration and notification
- Updates book states in the database
- Notifies all registered observers when state changes occur

## API Endpoints

### Update Book State
```
PUT /books/observer/{bookId}/state?newState=Available|Loaned
```
Updates a book's state and triggers all observer notifications.

### Add Email Observer
```
POST /books/observer/observers/email?email=user@example.com
```
Registers a new email notification observer.

### Get Statistics
```
GET /books/observer/statistics
```
Returns statistics about state transitions and active observers.

### Reset Statistics
```
DELETE /books/observer/statistics
```
Resets all statistics counters.

### Observer Pattern Info
```
GET /books/observer/info
```
Returns information about the Observer pattern implementation.

### Demonstration
```
POST /books/observer/demo
```
Demonstrates the Observer pattern by changing multiple book states.

## Usage Example

1. **Start the application**
```bash
./gradlew bootRun
```

2. **Update a book state** (triggers all observers)
```bash
curl -X PUT "http://localhost:8080/books/observer/1/state?newState=Loaned"
```

3. **Add a custom email observer**
```bash
curl -X POST "http://localhost:8080/books/observer/observers/email?email=admin@library.com"
```

4. **Check statistics**
```bash
curl http://localhost:8080/books/observer/statistics
```

5. **Run the demonstration**
```bash
curl -X POST http://localhost:8080/books/observer/demo
```

## Benefits of Observer Pattern

1. **Loose Coupling**: The subject doesn't need to know the concrete classes of observers
2. **Dynamic Relationships**: Observers can be added/removed at runtime
3. **Broadcast Communication**: One state change notifies all interested parties
4. **Open/Closed Principle**: New observers can be added without modifying existing code

## Architecture Integration

The Observer pattern fits perfectly within the hexagonal architecture:
- **Domain Layer**: Contains observer interfaces and concrete observers
- **Application Layer**: `BookStateService` orchestrates the pattern
- **Infrastructure Layer**: REST controller exposes the functionality

## Default Observers

By default, three observers are registered when the application starts:
1. `EmailNotificationObserver` - with library@example.com
2. `InventoryLogObserver` - for audit trail
3. `StatisticsObserver` - for metrics tracking

## Valid Book States

- `Available` - Book is available for loan
- `Loaned` - Book is currently loaned out

Any other state will result in a validation error.

