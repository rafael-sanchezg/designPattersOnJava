# Builder Pattern Implementation

## Overview
The Builder Pattern separates the construction of a complex object from its representation, allowing the same construction process to create different representations. In this project, it's implemented for creating complex books with many optional fields.

## Pattern Architecture

### Component Hierarchy
```
ComplexBook (record with 18 fields)
    └── ComplexBook.Builder (inner static class)
        └── Fluent interface for step-by-step construction

ComplexBookBuilderFactory (combines Builder + Factory Method)
    ├── FictionBookBuilderFactory
    └── NonFictionBookBuilderFactory
```

## Why Builder Pattern?

### The Problem: Complex Object Construction

**ComplexBook has 18 fields:**
- Required: title, author, type
- Optional: isbn, publicationDate, publisher, pageCount, language, genre, price, description, tags, location, condition, etc.

**Without Builder:**
```java
// Unreadable, error-prone, requires remembering parameter order
ComplexBook book = new ComplexBook(
    0, "Title", "Author", "Fiction", "Physical", "Available",
    "ISBN", LocalDate.now(), "Publisher", 350, "English",
    "Fantasy", 29.99, "Description", List.of("tag1"), true, "Shelf A", "Good"
);
```

**With Builder:**
```java
// Readable, flexible, self-documenting
ComplexBook book = ComplexBook.builder("Title", "Author", "Fiction")
    .isbn("978-3-16-148410-0")
    .publisher("Penguin Books")
    .pageCount(350)
    .price(29.99)
    .genre("Fantasy")
    .condition("Excellent")
    .build();
```

## Implementation Details

### 1. **ComplexBook Record**

**Location:** `domain/model/ComplexBook.java`

**Fields (18 total):**
```java
public record ComplexBook(
    int id,
    String title,           // Required
    String author,          // Required
    String type,            // Required
    String format,
    String state,
    String isbn,
    LocalDate publicationDate,
    String publisher,
    Integer pageCount,
    String language,
    String genre,
    Double price,
    String description,
    List<String> tags,
    boolean isAvailableForLoan,
    String location,
    String condition
) { }
```

### 2. **ComplexBook.Builder (Inner Static Class)**

**Purpose:** Provides fluent API for constructing ComplexBook

**Key Features:**
- **Fluent interface** - Each method returns `this`
- **Required fields** - Passed to constructor
- **Optional fields** - Set via methods with defaults
- **Validation** - In `build()` method

**Construction Pattern:**
```java
public static class Builder {
    // Required fields (final)
    private final String title;
    private final String author;
    private final String type;
    
    // Optional fields (with defaults)
    private String format = "Physical";
    private String state = "Available";
    private String isbn = "";
    // ... more optional fields
    
    // Constructor requires only mandatory fields
    private Builder(String title, String author, String type) {
        this.title = title;
        this.author = author;
        this.type = type;
    }
    
    // Fluent setter methods
    public Builder isbn(String isbn) {
        this.isbn = isbn;
        return this;
    }
    
    // Build method with validation
    public ComplexBook build() {
        validateRequiredFields();
        return new ComplexBook(...all fields...);
    }
}
```

### 3. **ComplexBookBuilderFactory**

**Location:** `domain/factory/ComplexBookBuilderFactory.java`

**Purpose:** Combines Builder + Factory Method patterns

**Abstract Methods:**
```java
// Factory method - subclasses provide pre-configured builders
public abstract ComplexBook.Builder createBookBuilder(String title, String author);
```

**Template Methods:**
```java
// Creates standard book
public final ComplexBook createDefaultBook(String title, String author)

// Creates premium book with enhanced features
public final ComplexBook createPremiumBook(String title, String author, ...)

// Creates digital-only book
public final ComplexBook createDigitalBook(String title, String author, ...)
```

### 4. **Concrete Builder Factories**

#### FictionBookBuilderFactory
Pre-configures builders for Fiction books:
```java
@Override
public ComplexBook.Builder createBookBuilder(String title, String author) {
    return ComplexBook.builder(title, author, "Fiction")
        .genre("Fiction")
        .tags(List.of("fiction", "literature"))
        .location("Fiction Section")
        .language("English");
}
```

#### NonFictionBookBuilderFactory
Pre-configures builders for Non-Fiction books:
```java
@Override
public ComplexBook.Builder createBookBuilder(String title, String author) {
    return ComplexBook.builder(title, author, "No Fiction")
        .genre("Non-Fiction")
        .tags(List.of("non-fiction", "educational"))
        .location("Non-Fiction Section")
        .language("English");
}
```

## Usage Examples

### Example 1: Simple Builder Usage
```java
ComplexBook book = ComplexBook.builder("Clean Code", "Robert Martin", "No Fiction")
    .isbn("978-0132350884")
    .publisher("Prentice Hall")
    .pageCount(464)
    .price(47.99)
    .genre("Programming")
    .description("A handbook of agile software craftsmanship")
    .tags(List.of("programming", "software", "clean code"))
    .condition("Excellent")
    .build();
```

### Example 2: Minimal Builder (Required Fields Only)
```java
ComplexBook book = ComplexBook.builder("1984", "George Orwell", "Fiction")
    .build(); // Uses all defaults for optional fields
```

### Example 3: Using Builder Factory
```java
ComplexBookBuilderFactory factory = new FictionBookBuilderFactory();

// Create default Fiction book with pre-configured settings
ComplexBook book = factory.createDefaultBook("The Hobbit", "J.R.R. Tolkien");
```

### Example 4: Using Template Methods
```java
ComplexBookBuilderFactory factory = new NonFictionBookBuilderFactory();

// Create premium book
ComplexBook premiumBook = factory.createPremiumBook(
    "Effective Java",
    "Joshua Bloch",
    "978-0134685991",
    "Addison-Wesley",
    54.99
);

// Create digital book
ComplexBook digitalBook = factory.createDigitalBook(
    "Java Concurrency",
    "Brian Goetz",
    "2.5 MB"
);
```

### Example 5: Service Layer Usage
```java
@Service
public class ComplexBookCreationService {
    public ComplexBook createComplexBook(BookRequest request) {
        return ComplexBook.builder(request.title(), request.author(), request.type())
            .isbn(request.isbn())
            .publisher(request.publisher())
            .pageCount(request.pageCount())
            .price(request.price())
            .description(request.description())
            .tags(request.tags())
            .build();
    }
}
```

## REST API Endpoints

### Builder Pattern Demonstrations

```bash
# Create complex book with builder
POST /books/complex

# Get complex books
GET /books/complex

# Create with builder factory
POST /books/complex/factory
```

**Example Request Body:**
```json
{
  "title": "Clean Architecture",
  "author": "Robert C. Martin",
  "type": "No Fiction",
  "isbn": "978-0134494166",
  "publisher": "Prentice Hall",
  "pageCount": 432,
  "price": 32.99,
  "language": "English",
  "genre": "Software Engineering",
  "description": "A Craftsman's Guide to Software Structure and Design",
  "tags": ["architecture", "software", "design"],
  "condition": "New"
}
```

## Benefits of Builder Pattern

### 1. **Readability** ✅
```java
// Clear, self-documenting code
ComplexBook book = ComplexBook.builder("Title", "Author", "Type")
    .isbn("978-...")
    .pageCount(350)
    .price(29.99)
    .build();
```

### 2. **Flexibility** ✅
```java
// Only set fields you need
ComplexBook minimalBook = ComplexBook.builder("Title", "Author", "Type")
    .build();

ComplexBook fullBook = ComplexBook.builder("Title", "Author", "Type")
    .isbn("...").publisher("...").pageCount(350)... // many more
    .build();
```

### 3. **Immutability** ✅
- ComplexBook is a record (immutable)
- Builder constructs it once
- No setters, no modification after creation

### 4. **Validation** ✅
```java
public ComplexBook build() {
    validateRequiredFields();
    validateISBN();
    validatePrice();
    return new ComplexBook(...);
}
```

### 5. **Step-by-Step Construction** ✅
```java
ComplexBook.Builder builder = ComplexBook.builder("Title", "Author", "Type");

// Build progressively
if (hasISBN) builder.isbn(isbn);
if (hasPrice) builder.price(price);
if (hasTags) builder.tags(tags);

ComplexBook book = builder.build();
```

## Builder Pattern Variations

### Current Implementation: Fluent Builder
```java
book = ComplexBook.builder(required, fields, here)
    .optionalField1(value1)
    .optionalField2(value2)
    .build();
```

### Alternative: Telescoping Constructor (Anti-Pattern)
```java
// DON'T DO THIS - Hard to read, error-prone
ComplexBook(title, author)
ComplexBook(title, author, type)
ComplexBook(title, author, type, isbn)
ComplexBook(title, author, type, isbn, publisher)
// ... many more constructors
```

### Alternative: JavaBean Pattern (Anti-Pattern for Immutable Objects)
```java
// DON'T DO THIS - Mutable, not thread-safe
ComplexBook book = new ComplexBook();
book.setTitle("Title");
book.setAuthor("Author");
book.setType("Fiction");
// ... many setters
```

## Design Principles Applied

### SOLID Principles
- **Single Responsibility:** Builder only constructs objects
- **Open/Closed:** Add new optional fields without breaking existing code
- **Liskov Substitution:** Builder produces valid ComplexBook
- **Interface Segregation:** Builder interface focused on construction
- **Dependency Inversion:** Depends on Builder abstraction

### Additional Principles
- **Immutability:** ComplexBook record is immutable
- **Encapsulation:** Construction logic hidden in builder
- **Fluent Interface:** Improves readability
- **Self-Documenting Code:** Method names explain purpose

## When to Use Builder Pattern

✅ **Use Builder When:**
- Object has many fields (>4-5)
- Many fields are optional
- Want immutable objects
- Want readable object construction
- Object creation is complex

❌ **Avoid When:**
- Object is simple (few fields)
- All fields are required
- No need for step-by-step construction
- Simple constructor suffices

## Integration with Factory Pattern

### Pattern Combination Benefits

**Combining Builder + Factory Method:**
```java
// Factory provides pre-configured builders
ComplexBookBuilderFactory factory = new FictionBookBuilderFactory();
ComplexBook.Builder builder = factory.createBookBuilder("Title", "Author");

// Further customize
ComplexBook book = builder
    .pageCount(500)
    .price(39.99)
    .build();
```

**Why Combine?**
1. Factory provides **type-specific defaults**
2. Builder allows **further customization**
3. Best of both patterns!

## Testing the Implementation

```bash
# 1. Start application
./gradlew bootRun

# 2. Create complex book via REST API
curl -X POST http://localhost:8080/books/complex \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Design Patterns",
    "author": "Gang of Four",
    "type": "No Fiction",
    "isbn": "978-0201633610",
    "publisher": "Addison-Wesley",
    "pageCount": 395,
    "price": 54.99,
    "genre": "Software Engineering"
  }'

# 3. Get all complex books
curl http://localhost:8080/books/complex
```

## Architecture Alignment

The Builder pattern fits within hexagonal architecture:

- **Domain Layer** (`domain/model/`, `domain/factory/`): ComplexBook and Builder
- **Application Layer** (`application/services/`): ComplexBookCreationService uses builders
- **Infrastructure Layer** (`infrastructure/adapters/web/`): REST API accepts builder-created objects

## Real-World Examples

### Java Standard Library
```java
StringBuilder sb = new StringBuilder()
    .append("Hello")
    .append(" ")
    .append("World");
```

### Stream API
```java
Stream.builder()
    .add("item1")
    .add("item2")
    .build();
```

### Lombok @Builder
```java
@Builder
public class Person {
    private String name;
    private int age;
}

Person person = Person.builder()
    .name("John")
    .age(30)
    .build();
```

## Summary

The Builder pattern provides:
- ✅ Readable construction of complex objects (18 fields)
- ✅ Flexible, fluent API
- ✅ Immutable ComplexBook records
- ✅ Validation during construction
- ✅ Integration with Factory Method pattern
- ✅ Self-documenting code

Perfect for creating complex books with many optional fields! 🔨

