# Singleton Pattern Implementation - H2 Database Connection

## Overview
The Singleton Pattern ensures that a class has only one instance and provides a global point of access to it. In this project, it's implemented for managing the H2 database connection.

## Pattern Architecture

### Component Structure
```
H2DatabaseSingleton (Singleton)
    ├── Private constructor (prevents instantiation)
    ├── Static volatile Connection instance
    ├── Thread-safe getInstance method
    └── Schema initialization logic
```

## Implementation Details

### **H2DatabaseSingleton Class**

**Location:** `infrastructure/config/H2DatabaseSingleton.java`

**Key Features:**
- **Thread-safe double-checked locking** - Ensures only one instance even in multi-threaded environments
- **Lazy initialization** - Connection is created only when first needed
- **Automatic schema initialization** - Loads and executes `schema.sql` on first connection
- **Connection pooling** - Maintains a single connection for the entire application lifecycle

**Core Components:**

```java
private static volatile Connection connection;
private static volatile boolean schemaInitialized = false;
```

**Thread-Safety Mechanism:**
- Uses `volatile` keyword to ensure visibility across threads
- Double-checked locking pattern in `getConnection()` method
- Synchronized block prevents race conditions during initialization

## Key Methods

### `getConnection()`
Returns the singleton database connection. If the connection doesn't exist or is closed:
1. Enters synchronized block (thread-safe)
2. Creates new connection
3. Initializes schema if not already done
4. Returns the connection

### `initializeSchema()`
Private method that:
1. Reads `schema.sql` from classpath
2. Parses SQL statements (splits by semicolon)
3. Executes each statement to create tables and insert initial data
4. Handles comments (lines starting with `//` or `--`)

## Configuration

### Connection Parameters
```java
JDBC_URL: jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL
USER: sa
PASSWORD: (empty)
```

### Database Mode
- **In-memory database** - Data exists only while application runs
- **MySQL compatibility mode** - Syntax compatible with MySQL
- **Close delay** - Database remains open between connections
- **No auto-close** - Database doesn't close when last connection closes

## Schema Initialization

The singleton automatically executes the `schema.sql` file which:
1. Creates the `book` table with appropriate columns
2. Inserts sample data (5 books)
3. Sets up constraints and checks for data integrity

**Table Structure:**
```sql
CREATE TABLE book (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    type VARCHAR(20) CHECK (type IN ('Fiction', 'No Fiction')),
    format VARCHAR(20) CHECK (format IN ('Physical', 'Digital')),
    state VARCHAR(20) CHECK (state IN ('Available', 'Loaned'))
);
```

## Benefits of Singleton Pattern

### 1. **Controlled Access** ✅
- Single point of access to database connection
- Prevents multiple concurrent connections
- Reduces resource consumption

### 2. **Thread Safety** ✅
- Double-checked locking ensures thread safety
- Volatile keyword ensures memory visibility
- Synchronized blocks prevent race conditions

### 3. **Lazy Initialization** ✅
- Connection created only when needed
- Reduces startup time
- Efficient resource usage

### 4. **Global Access** ✅
- Accessible from anywhere in the application
- No need to pass connection references
- Simplified architecture

## Usage in the Project

### BookRepositoryAdapter
All repository operations use the singleton:

```java
try (Connection conn = H2DatabaseSingleton.getConnection();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT * FROM book")) {
    // Process results
}
```

### Why Singleton for Database?
1. **Resource management** - Avoids creating multiple connections
2. **Performance** - Connection reuse is more efficient
3. **Consistency** - All operations use the same database instance
4. **Simplicity** - No complex connection pool configuration needed

## Pattern Variations

### Current Implementation: Thread-Safe Lazy Initialization
```java
public static Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
        synchronized (H2DatabaseSingleton.class) {
            if (connection == null || connection.isClosed()) {
                // Create connection
            }
        }
    }
    return connection;
}
```

### Why This Approach?
- **Double-checked locking** - Minimizes synchronization overhead
- **Lazy initialization** - Creates instance only when needed
- **Thread-safe** - Works correctly in multi-threaded Spring Boot environment

## Best Practices Applied

✅ **Private constructor** - Prevents external instantiation  
✅ **Volatile keyword** - Ensures thread visibility  
✅ **Double-checked locking** - Optimizes performance  
✅ **Exception handling** - Proper SQLException handling  
✅ **Resource management** - Proper cleanup with try-with-resources  
✅ **Documentation** - Clear JavaDoc comments

## Integration with Other Patterns

The Singleton pattern works with other patterns in the project:

```
Singleton Pattern (H2DatabaseSingleton)
    ↓ provides connection to
Repository Pattern (BookRepositoryAdapter)
    ↓ used by
Service Layer (BookManagementService, BookLoanService, BookStateService)
    ↓ triggers
Observer Pattern (state change notifications)
```

## Testing Considerations

### For Unit Tests
- Singleton makes testing challenging
- Consider using dependency injection in production systems
- For this educational project, the simplicity is acceptable

### Alternative for Production
In a real-world application, consider:
- **Connection pools** (HikariCP, Apache DBCP)
- **Dependency injection** for testability
- **Spring's DataSource management**

## Configuration in Spring Boot

The singleton works alongside Spring Boot's configuration:

**application.properties:**
```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
spring.jpa.hibernate.ddl-auto=none
spring.sql.init.mode=always
```

The Singleton provides direct JDBC access while Spring Boot manages the web console and application lifecycle.

## Common Issues and Solutions

### Issue: Connection Closed
**Solution:** The getConnection() method checks if connection is closed and recreates it

### Issue: Schema Not Initialized
**Solution:** Schema initialization happens automatically on first connection

### Issue: Thread Safety
**Solution:** Double-checked locking with volatile ensures thread safety

## When to Use Singleton Pattern

✅ **Good Use Cases:**
- Database connections (development/demos)
- Configuration managers
- Logging systems
- Caching mechanisms

❌ **Avoid When:**
- Multiple instances might be needed
- Testing requires mocking
- State needs to be isolated

## Summary

The Singleton pattern in this project provides:
- ✅ Single H2 database connection for the entire application
- ✅ Thread-safe access in Spring Boot environment
- ✅ Automatic schema initialization from SQL file
- ✅ Efficient resource management
- ✅ Simple, educational implementation

This implementation serves as an excellent example of the Singleton pattern's practical application in database connection management! 🎯

