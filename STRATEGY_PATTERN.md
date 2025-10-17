public class BookSearchContext {
    private BookSearchStrategy strategy;
    
    public void setStrategy(BookSearchStrategy strategy) {
        this.strategy = strategy;
    }
    
    public List<Book> executeSearch(List<Book> books, String criteria) {
        if (strategy == null) {
            throw new IllegalStateException("Search strategy not set");
        }
        return strategy.search(books, criteria);
    }
}
```

### 4. **BookSearchStrategyFactory**

**Location:** `domain/strategy/BookSearchStrategyFactory.java`

**Purpose:** Creates appropriate strategy based on search type

**Factory Method:**
```java
public static BookSearchStrategy getStrategy(String searchType) {
    return switch (searchType.toLowerCase()) {
        case "title" -> new TitleSearchStrategy();
        case "author" -> new AuthorSearchStrategy();
        case "type" -> new TypeSearchStrategy();
        case "format" -> new FormatSearchStrategy();
        case "state" -> new StateSearchStrategy();
        default -> throw new IllegalArgumentException("Unknown strategy: " + searchType);
    };
}
```

## Service Layer Integration

### **BookSearchService**

**Location:** `application/services/BookSearchService.java`

**Purpose:** Provides convenient search methods using strategies

**Key Methods:**
```java
// Search by type (Fiction/Non-Fiction)
public List<Book> searchBooksByType(String type) {
    BookSearchStrategy strategy = new TypeSearchStrategy();
    return strategy.search(bookRepository.findAll(), type);
}

// Search by format (Physical/Digital)
public List<Book> searchBooksByFormat(String format) {
    BookSearchStrategy strategy = new FormatSearchStrategy();
    return strategy.search(bookRepository.findAll(), format);
}

// Search by state (Available/Loaned)
public List<Book> searchBooksByState(String state) {
    BookSearchStrategy strategy = new StateSearchStrategy();
    return strategy.search(bookRepository.findAll(), state);
}

// Dynamic strategy selection
public List<Book> searchBooksWithStrategy(String strategyType, String criteria) {
    BookSearchStrategy strategy = BookSearchStrategyFactory.getStrategy(strategyType);
    return strategy.search(bookRepository.findAll(), criteria);
}
```

## REST API Endpoints

### Search Operations

```bash
# Search by type
GET /books/search/type/{type}
# Example: GET /books/search/type/Fiction

# Search by format
GET /books/search/format/{format}
# Example: GET /books/search/format/Digital

# Search by state
GET /books/search/state/{state}
# Example: GET /books/search/state/Available

# Search by title
GET /books/search/title?title=Hobbit

# Search by author
GET /books/search/author?author=Tolkien

# Dynamic strategy search
GET /books/search/dynamic?strategy=type&criteria=Fiction

# Get available strategies
GET /books/search/strategies

# Get strategy information
GET /books/search/info
```

## Usage Examples

### Example 1: Search Fiction Books
```bash
curl http://localhost:8080/books/search/type/Fiction
```

**Response:**
```json
[
  {
    "id": 1,
    "title": "The Hobbit",
    "author": "J.R.R. Tolkien",
    "type": "Fiction",
    "format": "Physical",
    "state": "Available"
  },
  {
    "id": 3,
    "title": "1984",
    "author": "George Orwell",
    "type": "Fiction",
    "format": "Digital",
    "state": "Loaned"
  }
]
```

### Example 2: Search Digital Books
```bash
curl http://localhost:8080/books/search/format/Digital
```

### Example 3: Search Available Books
```bash
curl http://localhost:8080/books/search/state/Available
```

### Example 4: Dynamic Strategy Selection
```bash
curl "http://localhost:8080/books/search/dynamic?strategy=author&criteria=Martin"
```

### Example 5: Programmatic Usage
```java
// Create context
BookSearchContext context = new BookSearchContext();

// Search Fiction books
context.setStrategy(new TypeSearchStrategy());
List<Book> fictionBooks = context.executeSearch(allBooks, "Fiction");

// Switch strategy - search by author
context.setStrategy(new AuthorSearchStrategy());
List<Book> martinBooks = context.executeSearch(allBooks, "Martin");

// Switch again - search available books
context.setStrategy(new StateSearchStrategy());
List<Book> availableBooks = context.executeSearch(allBooks, "Available");
```

### Example 6: Using Service Layer
```java
@Service
public class BookSearchService {
    public List<Book> findBooks(String searchType, String criteria) {
        // Strategy selected at runtime
        BookSearchStrategy strategy = BookSearchStrategyFactory.getStrategy(searchType);
        return strategy.search(bookRepository.findAll(), criteria);
    }
}
```

## Benefits of Strategy Pattern

### 1. **Algorithm Encapsulation** ✅
Each search algorithm is encapsulated in its own class:
- TitleSearchStrategy - title search logic
- AuthorSearchStrategy - author search logic
- TypeSearchStrategy - type filtering logic

### 2. **Runtime Algorithm Selection** ✅
```java
// Change algorithm at runtime
BookSearchStrategy strategy;
if (searchByTitle) {
    strategy = new TitleSearchStrategy();
} else if (searchByAuthor) {
    strategy = new AuthorSearchStrategy();
}
List<Book> results = strategy.search(books, criteria);
```

### 3. **Eliminates Conditional Statements** ✅

**Without Strategy (Bad):**
```java
public List<Book> search(String type, String criteria) {
    if (type.equals("title")) {
        // title search logic
    } else if (type.equals("author")) {
        // author search logic
    } else if (type.equals("type")) {
        // type search logic
    } // ... many more conditionals
}
```

**With Strategy (Good):**
```java
public List<Book> search(String type, String criteria) {
    BookSearchStrategy strategy = BookSearchStrategyFactory.getStrategy(type);
    return strategy.search(books, criteria);
}
```

### 4. **Easy to Extend** ✅
Add new search strategies without modifying existing code:
```java
// New strategy - no changes to existing code
public class ISBNSearchStrategy implements BookSearchStrategy {
    @Override
    public List<Book> search(List<Book> books, String isbn) {
        return books.stream()
            .filter(book -> book.isbn().equals(isbn))
            .toList();
    }
}
```

### 5. **Testability** ✅
Each strategy can be tested independently:
```java
@Test
void testTitleSearchStrategy() {
    BookSearchStrategy strategy = new TitleSearchStrategy();
    List<Book> results = strategy.search(testBooks, "Hobbit");
    assertEquals(1, results.size());
}
```

## Design Principles Applied

### SOLID Principles
- **Single Responsibility:** Each strategy has one search algorithm
- **Open/Closed:** Open for extension (new strategies), closed for modification
- **Liskov Substitution:** All strategies can be used interchangeably
- **Interface Segregation:** Clean, focused BookSearchStrategy interface
- **Dependency Inversion:** Depend on BookSearchStrategy abstraction

### Additional Principles
- **Encapsulation:** Algorithm details hidden in strategy classes
- **Polymorphism:** Different strategies, same interface
- **Composition over Inheritance:** Context delegates to strategy

## Strategy Pattern vs Other Patterns

### Strategy vs State Pattern
| Aspect | Strategy | State |
|--------|----------|-------|
| **Purpose** | Choose algorithm | Change behavior based on state |
| **Who changes** | Client chooses | Object changes internally |
| **Example** | Search strategies | Order states (pending, shipped) |

### Strategy vs Command Pattern
| Aspect | Strategy | Command |
|--------|----------|---------|
| **Purpose** | Choose how to do something | What to do |
| **Focus** | Algorithm variation | Action encapsulation |
| **Example** | Sort strategies | Undo/redo operations |

## Advanced Usage: Strategy Composition

### Combining Multiple Strategies
```java
public class CompositeSearchStrategy implements BookSearchStrategy {
    private final List<BookSearchStrategy> strategies;
    
    @Override
    public List<Book> search(List<Book> books, String criteria) {
        List<Book> results = books;
        for (BookSearchStrategy strategy : strategies) {
            results = strategy.search(results, criteria);
        }
        return results;
    }
}

// Usage: Search Fiction books that are Available
CompositeSearchStrategy composite = new CompositeSearchStrategy(
    List.of(new TypeSearchStrategy(), new StateSearchStrategy())
);
```

## When to Use Strategy Pattern

✅ **Use Strategy When:**
- Many related classes differ only in behavior
- Need different variants of an algorithm
- Algorithm uses data clients shouldn't know about
- Class has multiple conditional statements for behavior selection

❌ **Avoid When:**
- Only one algorithm exists
- Algorithms rarely change
- Overhead of extra classes not justified

## Real-World Examples

### Java Standard Library
```java
// Comparator is a strategy
Collections.sort(list, new Comparator<String>() {
    public int compare(String s1, String s2) {
        return s1.length() - s2.length();
    }
});

// Lambda expression (strategy)
list.sort((s1, s2) -> s1.length() - s2.length());
```

### Payment Processing
```java
interface PaymentStrategy {
    void pay(double amount);
}

class CreditCardStrategy implements PaymentStrategy { }
class PayPalStrategy implements PaymentStrategy { }
class BitcoinStrategy implements PaymentStrategy { }
```

### Compression Algorithms
```java
interface CompressionStrategy {
    byte[] compress(byte[] data);
}

class ZipCompression implements CompressionStrategy { }
class RarCompression implements CompressionStrategy { }
class GzipCompression implements CompressionStrategy { }
```

## Testing the Implementation

```bash
# 1. Start application
./gradlew bootRun

# 2. Search Fiction books
curl http://localhost:8080/books/search/type/Fiction

# 3. Search Digital books
curl http://localhost:8080/books/search/format/Digital

# 4. Search Available books
curl http://localhost:8080/books/search/state/Available

# 5. Search by title
curl "http://localhost:8080/books/search/title?title=Hobbit"

# 6. Search by author
curl "http://localhost:8080/books/search/author?author=Martin"

# 7. Dynamic strategy
curl "http://localhost:8080/books/search/dynamic?strategy=type&criteria=Fiction"

# 8. Get available strategies
curl http://localhost:8080/books/search/strategies
```

## Architecture Alignment

The Strategy pattern integrates with hexagonal architecture:

- **Domain Layer** (`domain/strategy/`): Strategy interfaces and implementations
- **Application Layer** (`application/services/`): BookSearchService uses strategies
- **Infrastructure Layer** (`infrastructure/adapters/web/`): REST API exposes search operations

## Summary

The Strategy pattern provides:
- ✅ Flexible search algorithms for books
- ✅ Runtime strategy selection
- ✅ Elimination of complex conditionals
- ✅ Easy addition of new search strategies
- ✅ Clean, maintainable code structure
- ✅ SOLID principles compliance

Perfect for implementing interchangeable algorithms! 🔍
# Strategy Pattern Implementation

## Overview
The Strategy Pattern defines a family of algorithms, encapsulates each one, and makes them interchangeable. Strategy lets the algorithm vary independently from clients that use it. In this project, it's implemented for searching books using different search strategies.

## Pattern Architecture

### Component Hierarchy
```
BookSearchStrategy (interface)
    ├── TitleSearchStrategy
    ├── AuthorSearchStrategy
    ├── TypeSearchStrategy (Fiction/Non-Fiction)
    ├── FormatSearchStrategy (Physical/Digital)
    ├── StateSearchStrategy (Available/Loaned)
    └── ... more strategies

BookSearchContext (context)
    └── Uses strategies to perform searches

BookSearchStrategyFactory (factory)
    └── Creates appropriate strategy based on criteria
```

## Implementation Details

### 1. **BookSearchStrategy (Interface)**

**Location:** `domain/strategy/BookSearchStrategy.java`

**Purpose:** Defines the contract for all search algorithms

**Methods:**
```java
List<Book> search(List<Book> books, String searchCriteria);
String getStrategyName();
String getDescription();
List<String> getSupportedCriteria();
```

### 2. **Concrete Search Strategies**

#### **TitleSearchStrategy**
Searches books by title (partial match, case-insensitive):
```java
@Override
public List<Book> search(List<Book> books, String searchCriteria) {
    return books.stream()
        .filter(book -> book.title().toLowerCase()
            .contains(searchCriteria.toLowerCase()))
        .toList();
}
```

#### **AuthorSearchStrategy**
Searches books by author name (partial match):
```java
@Override
public List<Book> search(List<Book> books, String searchCriteria) {
    return books.stream()
        .filter(book -> book.author().toLowerCase()
            .contains(searchCriteria.toLowerCase()))
        .toList();
}
```

#### **TypeSearchStrategy**
Searches books by type (Fiction/Non-Fiction):
```java
@Override
public List<Book> search(List<Book> books, String searchCriteria) {
    return books.stream()
        .filter(book -> book.type().equalsIgnoreCase(searchCriteria))
        .toList();
}
```

#### **FormatSearchStrategy**
Searches books by format (Physical/Digital):
```java
@Override
public List<Book> search(List<Book> books, String searchCriteria) {
    return books.stream()
        .filter(book -> book.format().equalsIgnoreCase(searchCriteria))
        .toList();
}
```

#### **StateSearchStrategy**
Searches books by availability state:
```java
@Override
public List<Book> search(List<Book> books, String searchCriteria) {
    return books.stream()
        .filter(book -> book.state().equalsIgnoreCase(searchCriteria))
        .toList();
}
```

### 3. **BookSearchContext**

**Location:** `domain/strategy/BookSearchContext.java`

**Purpose:** Maintains reference to strategy and delegates search operations

**Key Features:**
- Holds current search strategy
- Allows strategy switching at runtime
- Delegates search to current strategy

**Implementation:**
```java

