# Adapter Pattern Implementation

## Overview
The Adapter Pattern converts the interface of a class into another interface that clients expect. It allows classes with incompatible interfaces to work together. In this project, it integrates legacy books from an old system into the new book management system without modifying the legacy code.

## Pattern Architecture

### Component Hierarchy
```
IBook (Target Interface)
    ├── LegacyBookAdapter → wraps → LegacyBook (Adaptee)
    └── ModernBookAdapter → wraps → Book (Modern class)

BookAdapterService (uses adapters)
```

## The Problem

### Legacy System Incompatibility
The old library system uses:
- **Different field names**: `bookName` instead of `title`, `writerName` instead of `author`
- **Different data types**: `int availabilityStatus` (1/0) instead of `String state` ("Available"/"Loaned")
- **Coded values**: `"F"/"NF"` for type, `"P"/"D"` for format
- **Different method names**: `getBookId()` instead of `getId()`

### New System Requirements
The new system expects:
- Standard field names: `title`, `author`, `type`, `format`, `state`
- Full text values: "Fiction", "Physical", "Available"
- Consistent interface: `IBook` with standard methods

**The Adapter bridges this gap!**

## Implementation Details

### 1. **IBook (Target Interface)**

**Location:** `domain/adapter/IBook.java`

**Purpose:** Defines the interface that all books in the new system must implement

**Key Methods:**
```java
int getId();
String getTitle();
String getAuthor();
String getType();
String getFormat();
String getState();
void setState(String state);
String getDisplayInfo();
boolean isAvailable();
```

### 2. **LegacyBook (Adaptee)**

**Location:** `domain/adapter/LegacyBook.java`

**Purpose:** Represents the old system's book class (cannot be modified)

**Legacy Structure:**
```java
int bookId;           // Instead of 'id'
String bookName;      // Instead of 'title'
String writerName;    // Instead of 'author'
String category;      // "F" or "NF" instead of full names
String mediaType;     // "P" or "D" instead of full names
int availabilityStatus; // 1 or 0 instead of "Available"/"Loaned"
```

**Legacy Methods:**
- `getBookId()` → needs to be adapted to `getId()`
- `getBookName()` → needs to be adapted to `getTitle()`
- `getWriterName()` → needs to be adapted to `getAuthor()`
- `checkAvailability()` returns int → needs to be adapted to boolean

### 3. **LegacyBookAdapter (Adapter)**

**Location:** `domain/adapter/LegacyBookAdapter.java`

**Purpose:** Translates legacy book interface to IBook interface

**Adaptation Examples:**

#### Field Name Translation
```java
@Override
public String getTitle() {
    return legacyBook.getBookName(); // bookName → title
}

@Override
public String getAuthor() {
    return legacyBook.getWriterName(); // writerName → author
}
```

#### Code Translation
```java
@Override
public String getType() {
    String category = legacyBook.getCategory();
    return switch (category) {
        case "F" -> "Fiction";      // Translate code to full name
        case "NF" -> "No Fiction";
        default -> "Unknown";
    };
}

@Override
public String getFormat() {
    String mediaType = legacyBook.getMediaType();
    return switch (mediaType) {
        case "P" -> "Physical";     // Translate code to full name
        case "D" -> "Digital";
        default -> "Unknown";
    };
}
```

#### Data Type Conversion
```java
@Override
public String getState() {
    // Convert int (1/0) to String ("Available"/"Loaned")
    return legacyBook.getAvailabilityStatus() == 1 ? "Available" : "Loaned";
}

@Override
public void setState(String state) {
    // Convert String to int
    int status = "Available".equalsIgnoreCase(state) ? 1 : 0;
    legacyBook.setAvailabilityStatus(status);
}

@Override
public boolean isAvailable() {
    // Convert int check to boolean
    return legacyBook.checkAvailability() == 1;
}
```

### 4. **ModernBookAdapter**

**Location:** `domain/adapter/ModernBookAdapter.java`

**Purpose:** Wraps modern Book records to implement IBook interface

**Why needed?** Demonstrates that both legacy and modern books can work through the same unified interface.

### 5. **BookAdapterService**

**Location:** `application/services/BookAdapterService.java`

**Purpose:** Manages book adapters and provides unified operations

**Key Features:**
- Creates adapters for legacy and modern books
- Provides unified operations (search, filter, statistics)
- Works with mixed lists of legacy and modern books seamlessly

## REST API Endpoints

### Adapter Pattern Operations

```bash
# Get all books (legacy + modern) through unified interface
GET /books/adapter/all

# Get only available books
GET /books/adapter/available

# Search across all books (legacy + modern)
GET /books/adapter/search?title=Algorithm

# Create sample legacy book and show adaptation
POST /books/adapter/legacy/sample

# Get statistics from unified book list
GET /books/adapter/statistics

# Get adapter pattern information
GET /books/adapter/info

# Run demonstration
GET /books/adapter/demo
```

## Usage Examples

### Example 1: Creating and Adapting a Legacy Book
```java
// Create legacy book (from old system)
LegacyBook legacyBook = new LegacyBook(
    101,                    // bookId
    "Introduction to Algorithms", // bookName
    "Cormen, Leiserson",   // writerName
    "NF",                  // category (Non-Fiction)
    "P",                   // mediaType (Physical)
    1                      // availabilityStatus (Available)
);

// Adapt it to work with new system
IBook adaptedBook = new LegacyBookAdapter(legacyBook);

// Now use with standard interface
System.out.println(adaptedBook.getTitle());    // "Introduction to Algorithms"
System.out.println(adaptedBook.getAuthor());   // "Cormen, Leiserson"
System.out.println(adaptedBook.getType());     // "No Fiction" (translated from "NF")
System.out.println(adaptedBook.getFormat());   // "Physical" (translated from "P")
System.out.println(adaptedBook.getState());    // "Available" (translated from 1)
System.out.println(adaptedBook.isAvailable()); // true
```

### Example 2: Working with Mixed Book Sources
```java
@Service
public class BookAdapterService {
    public List<IBook> getAllBooksUnified() {
        List<IBook> allBooks = new ArrayList<>();
        
        // Add legacy books
        LegacyBook legacy1 = new LegacyBook(101, "SICP", "Abelson", "NF", "P", 1);
        allBooks.add(new LegacyBookAdapter(legacy1));
        
        // Add modern books
        Book modern1 = new Book(1, "Clean Code", "Martin", "No Fiction", "Physical", "Available");
        allBooks.add(new ModernBookAdapter(modern1));
        
        // Work with unified interface - no difference!
        return allBooks;
    }
}
```

### Example 3: Unified Operations
```java
// Get all books from different sources
List<IBook> allBooks = bookAdapterService.getAllBooksUnified();

// Filter available books (works for both legacy and modern)
List<IBook> available = allBooks.stream()
    .filter(IBook::isAvailable)
    .toList();

// Search by title (works for both legacy and modern)
List<IBook> results = allBooks.stream()
    .filter(book -> book.getTitle().contains("Algorithm"))
    .toList();

// Update state (works for both legacy and modern)
for (IBook book : allBooks) {
    if (book.isAvailable()) {
        book.setState("Loaned");
    }
}
```

### Example 4: REST API Usage
```bash
# Get all books (shows both legacy and modern)
curl http://localhost:8080/books/adapter/all

# Response:
{
  "totalBooks": 4,
  "books": [
    {
      "id": 101,
      "title": "Introduction to Algorithms",
      "author": "Cormen, Leiserson, Rivest",
      "type": "No Fiction",        # Translated from "NF"
      "format": "Physical",         # Translated from "P"
      "state": "Available",         # Translated from 1
      "available": true
    },
    {
      "id": 1,
      "title": "Clean Code",
      "author": "Robert Martin",
      "type": "No Fiction",
      "format": "Physical",
      "state": "Available",
      "available": true
    }
  ]
}
```

```bash
# Search across all books
curl "http://localhost:8080/books/adapter/search?title=Algorithm"

# Get statistics from unified list
curl http://localhost:8080/books/adapter/statistics

# Response:
{
  "totalBooks": 4,
  "availability": {
    "available": 3,
    "loaned": 1
  },
  "types": {
    "fiction": 1,
    "nonFiction": 3
  },
  "formats": {
    "physical": 3,
    "digital": 1
  }
}
```

## Benefits of Adapter Pattern

### 1. **Reuses Existing Code** ✅
No need to modify legacy system:
```java
// Legacy code remains unchanged
public class LegacyBook {
    // Old field names, old structure
    private String bookName;
    private String writerName;
    // ... original code untouched
}
```

### 2. **Provides Uniform Interface** ✅
Both legacy and modern books use the same interface:
```java
// Works the same for both!
void processBook(IBook book) {
    System.out.println(book.getTitle());
    System.out.println(book.getAuthor());
    // Client code doesn't know or care if it's legacy or modern
}
```

### 3. **Facilitates Migration** ✅
Gradual migration from legacy to modern system:
- Keep legacy system running
- Slowly migrate data to new system
- Both systems work together during transition

### 4. **Maintains Backward Compatibility** ✅
Legacy system continues to work:
```java
// Legacy system code still works
legacyBook.setAvailabilityStatus(0);
legacyBook.printBookDetails();

// But can also work through adapter
IBook adapted = new LegacyBookAdapter(legacyBook);
adapted.setState("Loaned");
```

### 5. **Single Responsibility** ✅
Adapter has one job: translate interfaces
- Legacy book: manages legacy data
- Adapter: translates interface
- Service: business logic

## Design Principles Applied

### SOLID Principles
- **Single Responsibility:** Adapter only translates interface
- **Open/Closed:** Can add new adapters without changing existing code
- **Liskov Substitution:** Adapted books can replace IBook anywhere
- **Interface Segregation:** IBook interface is focused and clean
- **Dependency Inversion:** Depend on IBook interface, not concrete classes

### Additional Principles
- **Encapsulation:** Legacy book internals hidden behind adapter
- **Composition:** Adapter wraps legacy book (has-a relationship)
- **Transparency:** Clients work with IBook, unaware of adaptation

## When to Use Adapter Pattern

✅ **Use Adapter When:**
- Need to use existing class with incompatible interface
- Want to create reusable class that cooperates with unrelated classes
- Need to use several existing subclasses, but impractical to adapt their interface by subclassing
- Integrating third-party libraries with incompatible interfaces
- Migrating from legacy system to new system

❌ **Avoid When:**
- Can modify the source class directly
- Interfaces are already compatible
- Creating too many layers of indirection
- Performance overhead is critical

## Pattern Variations

### Object Adapter (Current Implementation)
Uses composition (wraps the adaptee):
```java
public class LegacyBookAdapter implements IBook {
    private final LegacyBook legacyBook; // Composition
    
    @Override
    public String getTitle() {
        return legacyBook.getBookName();
    }
}
```

**Advantages:**
- More flexible
- Can adapt multiple classes
- Follows composition over inheritance

### Class Adapter (Alternative)
Uses inheritance (extends the adaptee):
```java
public class LegacyBookAdapter extends LegacyBook implements IBook {
    @Override
    public String getTitle() {
        return getBookName(); // Direct access
    }
}
```

**Disadvantages:**
- Less flexible (single inheritance in Java)
- Exposes adaptee's interface

## Real-World Examples

### 1. Java I/O Streams
```java
// Adapter for reading streams
Reader reader = new InputStreamReader(inputStream);
// InputStreamReader adapts InputStream to Reader interface
```

### 2. Collections Framework
```java
// Adapter for legacy collections
List<String> list = Arrays.asList(array);
// asList adapts array to List interface
```

### 3. JDBC Drivers
```java
// Different database drivers adapt to JDBC interface
Connection conn = DriverManager.getConnection(url);
// Each driver adapts its native interface to JDBC
```

### 4. Legacy System Integration
- Wrapping SOAP services for REST APIs
- Adapting old file formats to new formats
- Integrating legacy databases with ORMs

## Testing the Implementation

```bash
# 1. Start application
./gradlew bootRun

# 2. Get all books (legacy + modern unified)
curl http://localhost:8080/books/adapter/all

# 3. Create and adapt sample legacy book
curl -X POST http://localhost:8080/books/adapter/legacy/sample

# 4. Search across all books
curl "http://localhost:8080/books/adapter/search?title=Programming"

# 5. Get statistics from unified list
curl http://localhost:8080/books/adapter/statistics

# 6. Get available books only
curl http://localhost:8080/books/adapter/available

# 7. Run demonstration
curl http://localhost:8080/books/adapter/demo

# 8. Get pattern information
curl http://localhost:8080/books/adapter/info
```

## Integration with Other Patterns

The Adapter pattern works seamlessly with other patterns:

```
Legacy System (LegacyBook)
    ↓ adapted by
Adapter Pattern (LegacyBookAdapter)
    ↓ implements
IBook Interface
    ↓ used by
Strategy Pattern (search operations)
    ↓ or
Observer Pattern (state notifications)
    ↓ or
Decorator Pattern (add loan functionality)
```

## Architecture Alignment

The Adapter pattern integrates with hexagonal architecture:

- **Domain Layer** (`domain/adapter/`): IBook, adapters, legacy classes
- **Application Layer** (`application/services/`): BookAdapterService orchestrates
- **Infrastructure Layer** (`infrastructure/adapters/web/`): REST API exposes functionality

## Comparison: Adapter vs Other Patterns

### Adapter vs Facade
| Aspect | Adapter | Facade |
|--------|---------|--------|
| **Purpose** | Change interface | Simplify interface |
| **Classes** | One adaptee | Multiple subsystems |
| **Interface** | Match existing | Create new simplified |

### Adapter vs Bridge
| Aspect | Adapter | Bridge |
|--------|---------|--------|
| **Intent** | Make incompatible interfaces work | Separate abstraction from implementation |
| **When** | After classes are designed | During design |
| **Goal** | Compatibility | Flexibility |

### Adapter vs Decorator
| Aspect | Adapter | Decorator |
|--------|---------|-----------|
| **Purpose** | Change interface | Add responsibilities |
| **Interface** | Different interface | Same interface |
| **Transparency** | Changes interface | Transparent to client |

## Summary

The Adapter pattern provides:
- ✅ Integration of legacy books into modern system
- ✅ No modification to legacy code required
- ✅ Uniform interface for all book sources
- ✅ Translation of incompatible data formats
- ✅ Seamless operation across different systems
- ✅ Facilitates gradual system migration

Perfect for integrating incompatible systems! 🔌

