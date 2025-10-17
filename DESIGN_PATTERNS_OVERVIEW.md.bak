# Design Patterns Overview - Book Management System

## Project Summary

This Java 21 Spring Boot application demonstrates **7 major design patterns** working together in a hexagonal architecture to create a comprehensive library book management system.

## Implemented Design Patterns

### 1. **Singleton Pattern** 🔒
**File:** `SINGLETON_PATTERN.md`

**Purpose:** Ensures single database connection instance

**Implementation:**
- `H2DatabaseSingleton` - Thread-safe database connection management
- Double-checked locking for performance
- Automatic schema initialization

**Key Benefit:** Controlled access to shared resource (database connection)

---

### 2. **Factory Method Pattern** 🏭
**File:** `FACTORY_METHOD_PATTERN.md`

**Purpose:** Creates books of different types without specifying exact classes

**Implementation:**
- `BookFactory` - Abstract factory with template method
- `FictionBookFactory` - Creates Fiction books
- `NonFictionBookFactory` - Creates Non-Fiction books
- `BookFactoryProvider` - Centralized factory access

**Key Benefit:** Flexible object creation with validation and logging

---

### 3. **Abstract Factory Pattern** 🏗️
**File:** `ABSTRACT_FACTORY_PATTERN.md`

**Purpose:** Creates families of related objects (Physical & Digital books)

**Implementation:**
- `BookAbstractFactory` - Interface for product families
- `FictionBookAbstractFactory` - Creates Fiction Physical + Digital
- `NonFictionBookAbstractFactory` - Creates Non-Fiction Physical + Digital

**Key Benefit:** Ensures related products work together consistently

---

### 4. **Builder Pattern** 🔨
**File:** `BUILDER_PATTERN.md`

**Purpose:** Constructs complex objects step-by-step

**Implementation:**
- `ComplexBook.Builder` - Fluent API for 18-field object construction
- `ComplexBookBuilderFactory` - Combines Builder + Factory Method
- `FictionBookBuilderFactory` - Pre-configured Fiction builders
- `NonFictionBookBuilderFactory` - Pre-configured Non-Fiction builders

**Key Benefit:** Readable construction of complex objects with many optional fields

---

### 5. **Strategy Pattern** 🔍
**File:** `STRATEGY_PATTERN.md`

**Purpose:** Defines interchangeable search algorithms

**Implementation:**
- `BookSearchStrategy` - Strategy interface
- `TitleSearchStrategy` - Search by title
- `AuthorSearchStrategy` - Search by author
- `TypeSearchStrategy` - Search by type (Fiction/Non-Fiction)
- `FormatSearchStrategy` - Search by format (Physical/Digital)
- `StateSearchStrategy` - Search by state (Available/Loaned)
- `BookSearchContext` - Strategy context
- `BookSearchStrategyFactory` - Strategy selection

**Key Benefit:** Runtime algorithm selection without conditionals

---

### 6. **Observer Pattern** 👁️
**File:** `OBSERVER_PATTERN.md`

**Purpose:** Notifies multiple objects about state changes

**Implementation:**
- `BookStateObserver` - Observer interface
- `BookStateSubject` - Subject interface
- `EmailNotificationObserver` - Sends email notifications
- `InventoryLogObserver` - Logs state changes with timestamps
- `StatisticsObserver` - Tracks metrics and counters
- `BookStateService` - Subject implementation

**Key Benefit:** Automatic notifications when book states change (Available ↔ Loaned)

---

### 7. **Decorator Pattern** 🎨
**File:** `DECORATOR_PATTERN.md`

**Purpose:** Adds functionality to objects dynamically

**Implementation:**
- `BookComponent` - Component interface
- `BasicBook` - Concrete component
- `BookDecorator` - Abstract decorator
- `LoanDecorator` - Adds loan functionality (due dates, renewals, fines)
- `ReservationDecorator` - Adds reservation with queue
- `SpecialCollectionDecorator` - Adds special collection status
- `BookLoanService` - Manages decorations

**Key Benefit:** Dynamic feature addition without modifying Book class

---

## Pattern Interactions

The patterns work together seamlessly:

```
┌─────────────────────────────────────────────────────────────┐
│                    Client Layer (REST API)                   │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                    Application Layer                         │
│  BookManagementService | BookLoanService | BookSearchService │
│  BookStateService | ComplexBookCreationService              │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                      Domain Layer                            │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │  Factory    │  │  Builder    │  │  Strategy   │         │
│  │  Patterns   │  │  Pattern    │  │  Pattern    │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│  ┌─────────────┐  ┌─────────────┐                          │
│  │  Observer   │  │  Decorator  │                          │
│  │  Pattern    │  │  Pattern    │                          │
│  └─────────────┘  └─────────────┘                          │
└─────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────┐
│                  Infrastructure Layer                        │
│  BookRepositoryAdapter | BookController | BookObserverController │
│  BookDecoratorController | H2DatabaseSingleton (Singleton)  │
└─────────────────────────────────────────────────────────────┘
```

## Pattern Synergy Examples

### Example 1: Creating and Loaning a Book
```
1. Factory Pattern → Creates book
2. Observer Pattern → Monitors state changes
3. Decorator Pattern → Adds loan functionality
4. Repository → Persists changes
5. Singleton → Provides database connection
```

### Example 2: Complex Book Search
```
1. Builder Pattern → Creates complex search criteria
2. Strategy Pattern → Executes search algorithm
3. Repository → Fetches data
4. Singleton → Database access
```

### Example 3: Book State Management
```
1. Decorator Pattern → Adds loan details
2. Observer Pattern → Notifies state change
3. Repository → Updates database
4. Singleton → Connection management
```

## API Endpoint Overview

### Factory Patterns
```bash
GET  /books/factory-method          # Factory Method demo
POST /books/factory-method          # Create with Factory Method
GET  /books/abstract-factory        # Abstract Factory demo
POST /books/abstract-factory        # Create with Abstract Factory
GET  /books/patterns-info           # Pattern information
```

### Builder Pattern
```bash
POST /books/complex                 # Create complex book
GET  /books/complex                 # Get complex books
POST /books/complex/factory         # Create with builder factory
```

### Strategy Pattern
```bash
GET /books/search/type/{type}       # Search by type
GET /books/search/format/{format}   # Search by format
GET /books/search/state/{state}     # Search by state
GET /books/search/title             # Search by title
GET /books/search/author            # Search by author
GET /books/search/strategies        # Available strategies
```

### Observer Pattern
```bash
PUT    /books/observer/{id}/state   # Update state (triggers observers)
POST   /books/observer/observers/email  # Add email observer
GET    /books/observer/statistics   # Get statistics
DELETE /books/observer/statistics   # Reset statistics
POST   /books/observer/demo         # Observer demo
```

### Decorator Pattern
```bash
POST /books/decorator/{id}/loan     # Loan book (decorator)
POST /books/decorator/{id}/return   # Return book
POST /books/decorator/{id}/renew    # Renew loan
GET  /books/decorator/loans/active  # Active loans
GET  /books/decorator/loans/overdue # Overdue loans
POST /books/decorator/{id}/reserve  # Reserve book
POST /books/decorator/demo          # Decorator demo
```

## Quick Start Guide

### 1. Start Application
```bash
./gradlew bootRun
```

### 2. Test Factory Patterns
```bash
# Factory Method
curl http://localhost:8080/books/factory-method

# Abstract Factory
curl http://localhost:8080/books/abstract-factory
```

### 3. Test Builder Pattern
```bash
curl -X POST http://localhost:8080/books/complex \
  -H "Content-Type: application/json" \
  -d '{"title":"Clean Code","author":"Martin","type":"No Fiction"}'
```

### 4. Test Strategy Pattern
```bash
# Search Fiction books
curl http://localhost:8080/books/search/type/Fiction

# Search Digital books
curl http://localhost:8080/books/search/format/Digital
```

### 5. Test Observer Pattern
```bash
# Loan a book (triggers observers)
curl -X PUT "http://localhost:8080/books/observer/1/state?newState=Loaned"

# Check statistics
curl http://localhost:8080/books/observer/statistics
```

### 6. Test Decorator Pattern
```bash
# Loan a book
curl -X POST "http://localhost:8080/books/decorator/1/loan?borrowerName=Alice"

# Check active loans
curl http://localhost:8080/books/decorator/loans/active

# Return book
curl -X POST http://localhost:8080/books/decorator/1/return
```

## Architecture Principles

### Hexagonal Architecture (Ports & Adapters)
- **Domain Layer:** Pure business logic, design patterns
- **Application Layer:** Use cases, orchestration
- **Infrastructure Layer:** External systems (DB, REST, etc.)

### SOLID Principles
All patterns demonstrate SOLID principles:
- **S**ingle Responsibility
- **O**pen/Closed
- **L**iskov Substitution
- **I**nterface Segregation
- **D**ependency Inversion

### Clean Code Practices
- Meaningful names
- Small, focused classes
- Comprehensive documentation
- Proper error handling
- Testable design

## Technology Stack

- **Java 21** - Modern Java features (records, pattern matching)
- **Spring Boot** - Application framework
- **Gradle** - Build tool
- **H2 Database** - In-memory database
- **JDBC** - Database access
- **REST** - API architecture

## Learning Resources

Each pattern has a dedicated markdown file with:
- ✅ Detailed explanation
- ✅ UML diagrams (textual)
- ✅ Code examples
- ✅ API endpoints
- ✅ Usage scenarios
- ✅ Benefits and trade-offs
- ✅ When to use/avoid
- ✅ Real-world examples

## Documentation Files

1. `SINGLETON_PATTERN.md` - Database connection management
2. `FACTORY_METHOD_PATTERN.md` - Book type creation
3. `ABSTRACT_FACTORY_PATTERN.md` - Product families
4. `BUILDER_PATTERN.md` - Complex object construction
5. `STRATEGY_PATTERN.md` - Search algorithms
6. `OBSERVER_PATTERN.md` - State change notifications
7. `DECORATOR_PATTERN.md` - Dynamic feature addition
8. `DESIGN_PATTERNS_OVERVIEW.md` - This file

## Key Takeaways

### Pattern Selection Guide

**Creational Patterns** (How to create objects):
- Singleton → Single instance
- Factory Method → Create by type
- Abstract Factory → Create families
- Builder → Complex construction

**Behavioral Patterns** (How objects interact):
- Strategy → Interchangeable algorithms
- Observer → Event notification

**Structural Patterns** (How to compose objects):
- Decorator → Add responsibilities dynamically

### Project Benefits

✅ **Educational** - Complete pattern implementations  
✅ **Practical** - Real-world book management scenario  
✅ **Integrated** - Patterns working together  
✅ **Clean** - SOLID principles and clean code  
✅ **Documented** - Comprehensive markdown files  
✅ **Testable** - REST API for easy testing  

## Next Steps

1. **Read individual pattern documentation** - Start with any pattern file
2. **Run the application** - Test via REST API
3. **Explore the code** - See how patterns integrate
4. **Extend the system** - Add new patterns or features
5. **Experiment** - Modify patterns to learn

## Summary

This project demonstrates how design patterns solve real-world problems in a clean, maintainable architecture. Each pattern has a specific purpose and they all work together to create a robust book management system! 🎯📚

Happy Learning! 🚀

