# Decorator Pattern Implementation - Book Loan System

## Overview
The Decorator Pattern has been successfully implemented to add book loan functionality dynamically without modifying the Book class. This allows adding responsibilities to books at runtime in a flexible and reusable way.

## Pattern Architecture

### Component Hierarchy

```
BookComponent (interface)
    ├── BasicBook (concrete component)
    └── BookDecorator (abstract decorator)
        ├── LoanDecorator
        ├── ReservationDecorator
        └── SpecialCollectionDecorator
```

## Components Explained

### 1. **BookComponent Interface**
The base component interface that defines the contract for both concrete components and decorators.

**Methods:**
- `getBook()` - Returns the underlying Book entity
- `getDescription()` - Returns a description with all decorations
- `getState()` - Returns the current state
- `getAdditionalInfo()` - Returns additional information from decorators

### 2. **BasicBook (Concrete Component)**
The base implementation that wraps a Book record without any decorations.

### 3. **BookDecorator (Abstract Decorator)**
The abstract base class for all decorators. Maintains a reference to the wrapped BookComponent and delegates calls by default.

### 4. **Concrete Decorators**

#### **LoanDecorator** 🎯 Main Feature
Adds comprehensive loan functionality to books:

**Features:**
- **Borrower tracking** - Records who borrowed the book
- **Date management** - Tracks loan date and due date (14-day standard period)
- **Renewal system** - Supports up to 3 renewals (if not overdue)
- **Overdue detection** - Automatically calculates if a book is overdue
- **Fine calculation** - $0.50 per day for overdue books
- **Days tracking** - Shows days until due or days overdue

**Methods:**
- `getBorrowerName()` - Get the borrower's name
- `getLoanDate()` / `getDueDate()` - Get loan dates
- `isOverdue()` - Check if book is overdue
- `getDaysUntilDue()` - Calculate days remaining
- `getDaysOverdue()` - Calculate overdue days
- `canRenew()` - Check if loan can be renewed
- `renew()` - Create a renewed loan (adds 14 days)
- `calculateFine()` - Calculate overdue fines

#### **ReservationDecorator**
Adds reservation functionality:
- Tracks who reserved the book
- Manages queue position
- Identifies if reservation is next in queue

#### **SpecialCollectionDecorator**
Adds special collection status:
- Collection name (e.g., "Rare Books", "First Editions")
- Approval requirements for borrowing
- Physical location information

## Integration with Observer Pattern

The Decorator pattern is **fully integrated** with the Observer pattern:

1. When a book is loaned → state changes to "Loaned" → observers notified
2. When a book is returned → state changes to "Available" → observers notified
3. Email notifications, inventory logs, and statistics are automatically updated

## Service Layer

### **BookLoanService**
Manages the entire loan lifecycle:

**Main Operations:**
- `loanBook(bookId, borrowerName)` - Loans a book and decorates it
- `returnBook(bookId)` - Returns a book and removes decoration
- `renewLoan(bookId)` - Renews an existing loan
- `getLoanDetails(bookId)` - Gets current loan information
- `getAllActiveLoans()` - Lists all active loans
- `getOverdueLoans()` - Lists overdue loans
- `calculateTotalFines()` - Calculates total fines owed

**Advanced Features:**
- `loanBookWithDecorations()` - Demonstrates decorator stacking
- `reserveBook()` - Creates reserved books
- `addToSpecialCollection()` - Adds books to special collections

## REST API Endpoints

### Loan Management

```bash
# Loan a book
POST /books/decorator/{bookId}/loan?borrowerName=John Doe

# Return a book
POST /books/decorator/{bookId}/return

# Renew a loan
POST /books/decorator/{bookId}/renew

# Get loan details
GET /books/decorator/{bookId}/loan
```

### Query Operations

```bash
# Get all active loans
GET /books/decorator/loans/active

# Get overdue loans with fines
GET /books/decorator/loans/overdue
```

### Advanced Decorations

```bash
# Loan with multiple decorations (decorator stacking)
POST /books/decorator/{bookId}/loan-with-decorations?borrowerName=Jane&collectionName=Rare Books

# Reserve a book
POST /books/decorator/{bookId}/reserve?reservedBy=Alice&queuePosition=1

# Add to special collection
POST /books/decorator/{bookId}/special-collection?collectionName=First Editions&requiresApproval=true
```

### Information & Demo

```bash
# Get pattern information
GET /books/decorator/info

# Run demonstration
POST /books/decorator/demo
```

## Usage Examples

### Example 1: Simple Loan
```bash
curl -X POST "http://localhost:8080/books/decorator/1/loan?borrowerName=Alice Johnson"
```

**Response:**
```json
{
  "bookId": 1,
  "title": "The Hobbit",
  "author": "J.R.R. Tolkien",
  "description": "Book: 'The Hobbit' by J.R.R. Tolkien [Fiction, Physical] [ON LOAN]",
  "state": "Loaned",
  "borrowerName": "Alice Johnson",
  "loanDate": "2025-10-17",
  "dueDate": "2025-10-31",
  "daysUntilDue": 14,
  "renewalCount": 0,
  "canRenew": true,
  "isOverdue": false,
  "additionalInfo": "LOAN DETAILS: Borrower: Alice Johnson, Loaned: 17/10/2025, Due: 31/10/2025, Days remaining: 14, Renewals: 0/3"
}
```

### Example 2: Check Overdue Loans
```bash
curl http://localhost:8080/books/decorator/loans/overdue
```

**Response:**
```json
{
  "totalOverdueLoans": 1,
  "totalFines": "$2.50",
  "overdueLoans": [
    {
      "bookId": 3,
      "title": "1984",
      "borrowerName": "Bob Smith",
      "dueDate": "2025-10-12",
      "daysOverdue": 5,
      "fine": "$2.50"
    }
  ]
}
```

### Example 3: Decorator Stacking
```bash
curl -X POST "http://localhost:8080/books/decorator/2/loan-with-decorations?borrowerName=Charlie&collectionName=First Editions"
```

This creates a book with **multiple decorators stacked**:
1. BasicBook (base)
2. SpecialCollectionDecorator (adds collection info)
3. LoanDecorator (adds loan info)

## Benefits of This Implementation

### 1. **Open/Closed Principle** ✅
- Book class is closed for modification
- Open for extension through decorators
- Add new decorators without changing existing code

### 2. **Single Responsibility** ✅
- Each decorator has one clear responsibility
- LoanDecorator only handles loan logic
- ReservationDecorator only handles reservations

### 3. **Flexibility** ✅
- Add/remove decorations at runtime
- Stack multiple decorators
- Different combinations for different needs

### 4. **Reusability** ✅
- Decorators can be reused across different books
- No code duplication
- Clean separation of concerns

### 5. **Integration** ✅
- Works seamlessly with Observer pattern
- Integrated with hexagonal architecture
- Repository pattern for persistence

## Design Pattern Synergy

The implementation demonstrates how multiple patterns work together:

```
Decorator Pattern (adds loan functionality)
    ↓ triggers state change
Observer Pattern (notifies EmailObserver, InventoryLogObserver, StatisticsObserver)
    ↓ persists changes via
Repository Pattern (BookRepositoryAdapter)
    ↓ stored in
Singleton Pattern (H2DatabaseSingleton)
```

## Testing the Implementation

### Quick Test Sequence
```bash
# 1. Start application
./gradlew bootRun

# 2. Loan a book (triggers observers)
curl -X POST "http://localhost:8080/books/decorator/1/loan?borrowerName=Alice"

# 3. Check active loans
curl http://localhost:8080/books/decorator/loans/active

# 4. Check statistics (observer pattern)
curl http://localhost:8080/books/decorator/statistics

# 5. Renew the loan
curl -X POST http://localhost:8080/books/decorator/1/renew

# 6. Return the book (triggers observers again)
curl -X POST http://localhost:8080/books/decorator/1/return

# 7. Run full demonstration
curl -X POST http://localhost:8080/books/decorator/demo
```

## Architecture Alignment

The implementation follows **Hexagonal Architecture**:

- **Domain Layer** (`domain/decorator/`): Pattern interfaces and implementations
- **Application Layer** (`application/services/`): BookLoanService orchestrates business logic
- **Infrastructure Layer** (`infrastructure/adapters/web/`): REST API exposes functionality

## Clean Code Principles Applied

✅ **Meaningful Names** - Clear, descriptive class and method names  
✅ **Single Responsibility** - Each decorator has one job  
✅ **DRY** - No code duplication, shared logic in abstract decorator  
✅ **Error Handling** - Proper exceptions with clear messages  
✅ **Documentation** - Comprehensive JavaDoc comments  
✅ **Immutability** - Book record is immutable, decorators don't modify it

## Next Steps

You now have a complete **Decorator Pattern** implementation for book loans that:
- ✅ Adds loan functionality dynamically
- ✅ Integrates with Observer pattern for notifications
- ✅ Supports decorator stacking
- ✅ Calculates fines for overdue books
- ✅ Manages renewals with business rules
- ✅ Provides comprehensive REST API

The system is ready to use! 🎉

