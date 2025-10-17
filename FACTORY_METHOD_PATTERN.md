# Factory Method Pattern Implementation

## Overview
The Factory Method Pattern defines an interface for creating objects but lets subclasses decide which class to instantiate. This project implements it for creating different types of books (Fiction and Non-Fiction).

## Pattern Architecture

### Component Hierarchy
```
BookFactory (abstract class)
    ├── FictionBookFactory (concrete factory)
    └── NonFictionBookFactory (concrete factory)

BookFactoryProvider (factory provider)
    └── Manages factory selection
```

## Implementation Details

### 1. **BookFactory (Abstract Factory)**

**Location:** `domain/factory/BookFactory.java`

**Purpose:** Defines the contract for book creation with common logic.

**Key Methods:**
- `createBook()` - Abstract factory method (implemented by subclasses)
- `processBookCreation()` - Template method with validation and logging

**Template Method Pattern Integration:**
```java
public final Book processBookCreation(...) {
    validateBookData(...);      // Common validation
    Book book = createBook(...); // Factory method (subclass)
    logBookCreation(book);       // Common logging
    return book;
}
```

### 2. **FictionBookFactory**

**Location:** `domain/factory/FictionBookFactory.java`

**Purpose:** Creates Fiction books with type "Fiction"

**Implementation:**
```java
@Override
public Book createBook(int id, String title, String author, String format, String state) {
    return new Book(id, title, author, "Fiction", format, state);
}
```

### 3. **NonFictionBookFactory**

**Location:** `domain/factory/NonFictionBookFactory.java`

**Purpose:** Creates Non-Fiction books with type "No Fiction"

**Implementation:**
```java
@Override
public Book createBook(int id, String title, String author, String format, String state) {
    return new Book(id, title, author, "No Fiction", format, state);
}
```

### 4. **BookFactoryProvider**

**Location:** `domain/factory/BookFactoryProvider.java`

**Purpose:** Centralized factory selection and convenience methods

**Key Features:**
- Factory selection by book type
- Convenience methods for common operations
- Integration point for client code

## REST API Endpoints

### Factory Method Demonstrations

```bash
# Get books created with Factory Method pattern
GET /books/factory-method

# Create a Fiction book
POST /books/factory-method?bookType=Fiction&title=1984&author=Orwell&format=Digital&state=Available

# Create a Non-Fiction book
POST /books/factory-method?bookType=No Fiction&title=Clean Code&author=Martin&format=Physical&state=Available

# Get pattern information
GET /books/patterns-info
```

## Usage Examples

### Example 1: Using Factory Directly
```java
BookFactory fictionFactory = BookFactoryProvider.getBookFactory("Fiction");
Book book = fictionFactory.processBookCreation(0, "1984", "George Orwell", "Digital", "Available");
```

### Example 2: Using Convenience Method
```java
Book book = BookFactoryProvider.createBook("Fiction", 0, "1984", "Orwell", "Digital", "Available");
```

### Example 3: Service Layer Usage
```java
@Service
public class BookManagementService {
    public List<Book> createBooksWithFactoryMethod() {
        BookFactory fictionFactory = BookFactoryProvider.getBookFactory("Fiction");
        Book book1 = fictionFactory.processBookCreation(...);
        
        BookFactory nonFictionFactory = BookFactoryProvider.getBookFactory("No Fiction");
        Book book2 = nonFictionFactory.processBookCreation(...);
        
        return List.of(book1, book2);
    }
}
```

## Benefits of Factory Method Pattern

### 1. **Open/Closed Principle** ✅
- Open for extension: Add new book types by creating new factory classes
- Closed for modification: Existing code doesn't need to change

### 2. **Single Responsibility** ✅
- Each factory has one job: create its specific book type
- Validation and logging separated in template method

### 3. **Loose Coupling** ✅
- Client code depends on abstract BookFactory, not concrete classes
- Easy to swap implementations

### 4. **Encapsulation** ✅
- Book creation logic encapsulated in factories
- Complex initialization hidden from clients

## Validation Features

The template method `processBookCreation()` validates:
- **Title** - Cannot be null or empty
- **Author** - Cannot be null or empty
- **Format** - Must be "Physical" or "Digital"
- **State** - Must be "Available" or "Loaned"

**Exception Handling:**
```java
throw new IllegalArgumentException("Book title cannot be null or empty");
```

## Integration with Service Layer

### BookManagementService
```java
@Override
public List<Book> createBooksWithFactoryMethod() {
    List<Book> books = new ArrayList<>();
    
    // Using Fiction factory
    BookFactory fictionFactory = BookFactoryProvider.getBookFactory("Fiction");
    Book book1 = fictionFactory.processBookCreation(...);
    books.add(bookRepositoryPort.save(book1));
    
    // Using Non-Fiction factory
    BookFactory nonFictionFactory = BookFactoryProvider.getBookFactory("No Fiction");
    Book book2 = nonFictionFactory.processBookCreation(...);
    books.add(bookRepositoryPort.save(book2));
    
    return books;
}
```

## Comparison: Factory Method vs Direct Instantiation

### ❌ Without Factory Method
```java
// Tight coupling, repeated validation, no flexibility
if (type.equals("Fiction")) {
    book = new Book(id, title, author, "Fiction", format, state);
} else if (type.equals("No Fiction")) {
    book = new Book(id, title, author, "No Fiction", format, state);
}
```

### ✅ With Factory Method
```java
// Loose coupling, centralized validation, extensible
BookFactory factory = BookFactoryProvider.getBookFactory(type);
Book book = factory.processBookCreation(id, title, author, format, state);
```

## Design Principles Applied

### SOLID Principles
- **S**ingle Responsibility: Each factory creates one type
- **O**pen/Closed: New factories without modifying existing code
- **L**iskov Substitution: All factories can be used interchangeably
- **I**nterface Segregation: Clean, focused interface
- **D**ependency Inversion: Depend on abstraction (BookFactory)

## When to Use Factory Method

✅ **Use Factory Method When:**
- Class can't anticipate the type of objects to create
- Want subclasses to specify objects to create
- Need to delegate responsibility to helper subclasses
- Want to localize object creation logic

❌ **Avoid When:**
- Only one product type exists
- Product creation is trivial
- No need for abstraction

## Testing the Implementation

```bash
# 1. Start application
./gradlew bootRun

# 2. Create books with factory method
curl http://localhost:8080/books/factory-method

# 3. Create custom Fiction book
curl -X POST "http://localhost:8080/books/factory-method?bookType=Fiction&title=Dune&author=Herbert&format=Physical&state=Available"

# 4. Get pattern information
curl http://localhost:8080/books/patterns-info
```

## Architecture Alignment

The Factory Method pattern fits within the hexagonal architecture:

- **Domain Layer** (`domain/factory/`): Factory interfaces and implementations
- **Application Layer** (`application/services/`): Services use factories
- **Infrastructure Layer** (`infrastructure/adapters/web/`): REST endpoints

## Summary

The Factory Method pattern provides:
- ✅ Flexible book creation mechanism
- ✅ Centralized validation and logging
- ✅ Easy extensibility for new book types
- ✅ Clean separation of concerns
- ✅ SOLID principles compliance

Combined with Template Method pattern for common logic! 🎯

