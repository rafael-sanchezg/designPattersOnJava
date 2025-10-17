# Abstract Factory Pattern Implementation

## Overview
The Abstract Factory Pattern provides an interface for creating families of related or dependent objects without specifying their concrete classes. In this project, it creates books with different formats (Physical/Digital) for each book type.

## Pattern Architecture

### Component Hierarchy
```
BookAbstractFactory (interface)
    ├── FictionBookAbstractFactory (concrete factory)
    └── NonFictionBookAbstractFactory (concrete factory)

Each factory creates:
    ├── Physical Books
    └── Digital Books
```

## Key Difference: Factory Method vs Abstract Factory

### Factory Method
Creates **one product** per factory:
- FictionBookFactory → Fiction books (any format)

### Abstract Factory
Creates **families of products** per factory:
- FictionBookAbstractFactory → Fiction Physical books + Fiction Digital books
- NonFictionBookAbstractFactory → Non-Fiction Physical books + Non-Fiction Digital books

## Implementation Details

### 1. **BookAbstractFactory (Interface)**

**Location:** `domain/factory/BookAbstractFactory.java`

**Purpose:** Defines the contract for creating book families

**Methods:**
```java
Book createPhysicalBook(int id, String title, String author, String state);
Book createDigitalBook(int id, String title, String author, String state);
```

**Key Feature:** Format parameter is **not needed** - the factory method determines it!

### 2. **FictionBookAbstractFactory**

**Location:** `domain/factory/FictionBookAbstractFactory.java`

**Creates:**
- Fiction Physical books
- Fiction Digital books

**Implementation:**
```java
@Override
public Book createPhysicalBook(int id, String title, String author, String state) {
    return new Book(id, title, author, "Fiction", "Physical", state);
}

@Override
public Book createDigitalBook(int id, String title, String author, String state) {
    return new Book(id, title, author, "Fiction", "Digital", state);
}
```

### 3. **NonFictionBookAbstractFactory**

**Location:** `domain/factory/NonFictionBookAbstractFactory.java`

**Creates:**
- Non-Fiction Physical books
- Non-Fiction Digital books

**Implementation:**
```java
@Override
public Book createPhysicalBook(int id, String title, String author, String state) {
    return new Book(id, title, author, "No Fiction", "Physical", state);
}

@Override
public Book createDigitalBook(int id, String title, String author, String state) {
    return new Book(id, title, author, "No Fiction", "Digital", state);
}
```

### 4. **BookFactoryProvider Integration**

**Purpose:** Provides unified access to both Factory Method and Abstract Factory

**Methods:**
```java
// Get Abstract Factory by type
public static BookAbstractFactory getAbstractFactory(String bookType)

// Convenience method
public static Book createBookWithFormat(String bookType, String format, ...)
```

## REST API Endpoints

### Abstract Factory Demonstrations

```bash
# Get books created with Abstract Factory pattern
GET /books/abstract-factory

# Create Fiction physical book
POST /books/abstract-factory?bookType=Fiction&format=Physical&title=Harry Potter&author=Rowling&state=Available

# Create Non-Fiction digital book
POST /books/abstract-factory?bookType=No Fiction&format=Digital&title=Sapiens&author=Harari&state=Available

# Dynamic creation with abstract factory
POST /books/dynamic?bookType=Fiction&useAbstractFactory=true

# Get pattern information
GET /books/patterns-info
```

## Usage Examples

### Example 1: Creating Physical and Digital Fiction Books
```java
BookAbstractFactory fictionFactory = BookFactoryProvider.getAbstractFactory("Fiction");

// Create physical edition
Book physicalBook = fictionFactory.createPhysicalBook(0, "1984", "Orwell", "Available");

// Create digital edition
Book digitalBook = fictionFactory.createDigitalBook(0, "1984", "Orwell", "Available");
```

### Example 2: Creating Non-Fiction Book Family
```java
BookAbstractFactory nonFictionFactory = BookFactoryProvider.getAbstractFactory("No Fiction");

Book physicalBook = nonFictionFactory.createPhysicalBook(
    0, "Clean Code", "Robert Martin", "Available"
);

Book digitalBook = nonFictionFactory.createDigitalBook(
    0, "Clean Code", "Robert Martin", "Available"
);
```

### Example 3: Using Convenience Method
```java
// Create Fiction Physical book
Book book1 = BookFactoryProvider.createBookWithFormat(
    "Fiction", "Physical", 0, "Dune", "Herbert", "Available"
);

// Create Non-Fiction Digital book
Book book2 = BookFactoryProvider.createBookWithFormat(
    "No Fiction", "Digital", 0, "Refactoring", "Fowler", "Available"
);
```

### Example 4: Service Layer Usage
```java
@Service
public class BookManagementService {
    public List<Book> createBooksWithAbstractFactory() {
        List<Book> books = new ArrayList<>();
        
        // Fiction family
        BookAbstractFactory fictionFactory = 
            BookFactoryProvider.getAbstractFactory("Fiction");
        books.add(fictionFactory.createPhysicalBook(...));
        books.add(fictionFactory.createDigitalBook(...));
        
        // Non-Fiction family
        BookAbstractFactory nonFictionFactory = 
            BookFactoryProvider.getAbstractFactory("No Fiction");
        books.add(nonFictionFactory.createPhysicalBook(...));
        books.add(nonFictionFactory.createDigitalBook(...));
        
        return books;
    }
}
```

## Benefits of Abstract Factory Pattern

### 1. **Product Family Consistency** ✅
Ensures books from the same factory belong to the same type:
- Fiction factory only creates Fiction books
- Non-Fiction factory only creates Non-Fiction books

### 2. **Isolation of Concrete Classes** ✅
Clients work with interfaces, not concrete book classes:
```java
BookAbstractFactory factory = getAbstractFactory(type);
Book book = factory.createPhysicalBook(...); // Don't know/care about concrete class
```

### 3. **Easy Product Family Switching** ✅
Change entire product family by changing factory:
```java
// Switch from Fiction to Non-Fiction
BookAbstractFactory factory = 
    wantFiction ? getAbstractFactory("Fiction") : getAbstractFactory("No Fiction");
```

### 4. **Support for Variations** ✅
Each factory can create multiple product variants:
- Physical books (printed, bound)
- Digital books (ebook, audiobook potential)

## Pattern Comparison Table

| Aspect | Factory Method | Abstract Factory |
|--------|---------------|------------------|
| **Products** | Single product | Family of products |
| **Methods** | One factory method | Multiple factory methods |
| **Parameters** | Includes format | Format determined by method |
| **Example** | `createBook(format)` | `createPhysicalBook()`, `createDigitalBook()` |
| **Flexibility** | Simple, direct | Complex, family-oriented |

## Real-World Analogy

### Factory Method (Restaurant)
- Italian Restaurant → Creates Italian dishes
- Chinese Restaurant → Creates Chinese dishes

### Abstract Factory (Furniture Store)
- Modern Furniture Factory → Creates modern chairs, modern tables, modern sofas
- Victorian Furniture Factory → Creates Victorian chairs, Victorian tables, Victorian sofas

Each factory creates a **family** of related products!

## Integration with Service Layer

### BookManagementService Methods

```java
// Factory Method - creates books of any format
public List<Book> createBooksWithFactoryMethod() {
    BookFactory factory = BookFactoryProvider.getBookFactory("Fiction");
    return List.of(
        factory.createBook(0, "Title", "Author", "Physical", "Available"),
        factory.createBook(0, "Title", "Author", "Digital", "Available")
    );
}

// Abstract Factory - creates product families
public List<Book> createBooksWithAbstractFactory() {
    BookAbstractFactory factory = BookFactoryProvider.getAbstractFactory("Fiction");
    return List.of(
        factory.createPhysicalBook(0, "Title", "Author", "Available"),
        factory.createDigitalBook(0, "Title", "Author", "Available")
    );
}
```

## When to Use Abstract Factory

✅ **Use Abstract Factory When:**
- System needs to be independent of product creation
- System configured with multiple families of products
- Family of products designed to work together
- Want to reveal interfaces, not implementations

❌ **Use Factory Method Instead When:**
- Only need single product variants
- Product families not necessary
- Simpler solution suffices

## Design Principles Applied

### SOLID Principles
- **Single Responsibility:** Each factory creates one family
- **Open/Closed:** Add new factories without changing existing code
- **Liskov Substitution:** All factories interchangeable
- **Interface Segregation:** Clean, focused interface
- **Dependency Inversion:** Depend on BookAbstractFactory interface

### Additional Principles
- **Encapsulation:** Product creation logic hidden
- **Polymorphism:** Runtime factory selection
- **Consistency:** Product families guaranteed compatible

## Testing the Implementation

```bash
# 1. Start application
./gradlew bootRun

# 2. Create books with abstract factory
curl http://localhost:8080/books/abstract-factory

# 3. Create Fiction physical book
curl -X POST "http://localhost:8080/books/abstract-factory?bookType=Fiction&format=Physical&title=The Hobbit&author=Tolkien&state=Available"

# 4. Create Non-Fiction digital book
curl -X POST "http://localhost:8080/books/abstract-factory?bookType=No Fiction&format=Digital&title=Sapiens&author=Harari&state=Available"

# 5. Compare with factory method
curl http://localhost:8080/books/factory-method

# 6. Get pattern comparison info
curl http://localhost:8080/books/patterns-info
```

## Pattern Evolution in Project

### Level 1: Direct Instantiation
```java
new Book(id, title, author, "Fiction", "Physical", state);
```

### Level 2: Factory Method
```java
BookFactory factory = new FictionBookFactory();
Book book = factory.createBook(id, title, author, "Physical", state);
```

### Level 3: Abstract Factory
```java
BookAbstractFactory factory = new FictionBookAbstractFactory();
Book physicalBook = factory.createPhysicalBook(id, title, author, state);
Book digitalBook = factory.createDigitalBook(id, title, author, state);
```

## Architecture Alignment

The Abstract Factory pattern integrates with hexagonal architecture:

- **Domain Layer** (`domain/factory/`): Factory interfaces and implementations
- **Application Layer** (`application/services/`): Services use factories for book creation
- **Infrastructure Layer** (`infrastructure/adapters/web/`): REST API exposes factory operations

## Common Use Cases

### 1. UI Themes
- LightThemeFactory → Light buttons, light panels, light dialogs
- DarkThemeFactory → Dark buttons, dark panels, dark dialogs

### 2. Database Drivers
- MySQLFactory → MySQL connection, MySQL commands, MySQL queries
- PostgreSQLFactory → PostgreSQL connection, PostgreSQL commands, PostgreSQL queries

### 3. Document Formats
- PDFFactory → PDF headers, PDF content, PDF footers
- HTMLFactory → HTML headers, HTML content, HTML footers

## Summary

The Abstract Factory pattern provides:
- ✅ Consistent product families (Fiction/Non-Fiction books)
- ✅ Multiple product variants (Physical/Digital formats)
- ✅ Strong encapsulation of creation logic
- ✅ Easy family switching
- ✅ Scalable architecture for new product types

Perfect for creating related objects that should be used together! 🏭

