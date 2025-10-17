# Chain of Responsibility Pattern Implementation

## Overview
The Chain of Responsibility Pattern passes a request along a chain of handlers. Each handler decides either to process the request or to pass it to the next handler in the chain. In this project, it's implemented for validating book data through a series of validators.

## Pattern Architecture

### Component Hierarchy
```
BookValidator (abstract handler)
    ├── TitleValidator (concrete handler)
    ├── AuthorValidator (concrete handler)
    ├── TypeValidator (concrete handler)
    ├── FormatValidator (concrete handler)
    └── StateValidator (concrete handler)

ValidationChainFactory (factory for creating chains)
ValidationException (custom exception)
BookValidationService (uses validation chains)
```

## Implementation Details

### 1. **BookValidator (Abstract Handler)**

**Location:** `domain/validator/BookValidator.java`

**Purpose:** Defines the base structure for all validators in the chain

**Key Methods:**
- `setNext(BookValidator)` - Links validators together
- `validate(String, String)` - Validates and passes to next validator
- `doValidate(String, String)` - Abstract method for specific validation logic
- `getValidatorName()` - Returns validator identifier

**Chain Mechanism:**
```java
public void validate(String title, String author) throws ValidationException {
    doValidate(title, author);  // Validate in current handler
    
    if (nextValidator != null) {
        nextValidator.validate(title, author);  // Pass to next handler
    }
}
```

### 2. **Concrete Validators**

#### **TitleValidator**
Validates book titles with the following rules:
- ✅ Not null
- ✅ Not empty or blank
- ✅ Minimum length: 1 character
- ✅ Maximum length: 255 characters
- ✅ No invalid characters: `< > { } [ ]`

**Example Validation:**
```java
// Valid
"Clean Code" ✓
"The Pragmatic Programmer" ✓

// Invalid
"" ✗ (empty)
null ✗ (null)
"A".repeat(300) ✗ (too long)
"Clean Code <script>" ✗ (invalid characters)
```

#### **AuthorValidator**
Validates author names with the following rules:
- ✅ Not null
- ✅ Not empty or blank
- ✅ Minimum length: 2 characters
- ✅ Maximum length: 255 characters
- ✅ Only letters, spaces, dots, hyphens, apostrophes
- ✅ Must contain at least one letter

**Example Validation:**
```java
// Valid
"Robert Martin" ✓
"J.R.R. Tolkien" ✓
"Martin O'Brien" ✓
"Jean-Paul Sartre" ✓

// Invalid
"M" ✗ (too short)
"Robert@Martin" ✗ (invalid characters)
"123" ✗ (no letters)
".-'" ✗ (only special characters)
```

#### **TypeValidator**
Validates book type:
- ✅ Must be "Fiction" or "No Fiction"
- ✅ Case-insensitive

#### **FormatValidator**
Validates book format:
- ✅ Must be "Physical" or "Digital"
- ✅ Case-insensitive

#### **StateValidator**
Validates book state:
- ✅ Must be "Available" or "Loaned"
- ✅ Case-insensitive

### 3. **ValidationException**

**Location:** `domain/validator/ValidationException.java`

**Purpose:** Custom exception that includes validator name for debugging

**Features:**
- Stores the validator that failed
- Enhanced error messages with validator context
- Extends standard Exception class

**Example Error Messages:**
```
[TitleValidator] Title cannot be empty or blank
[AuthorValidator] Author name must be at least 2 characters long
[TypeValidator] Type must be either 'Fiction' or 'No Fiction'
```

### 4. **ValidationChainFactory**

**Location:** `domain/validator/ValidationChainFactory.java`

**Purpose:** Creates pre-configured validation chains

**Available Chains:**

#### Complete Chain
```java
Title → Author → Type → Format → State
```

#### Basic Chain
```java
Title → Author
```

#### Custom Chain
```java
// Build your own chain
ValidationChainFactory.createCustomChain(
    new TitleValidator(),
    new AuthorValidator(),
    new TypeValidator()
);
```

## Service Layer Integration

### **BookValidationService**

**Location:** `application/services/BookValidationService.java`

**Key Methods:**

#### `validateAndCreateBook()`
Validates all fields and creates a book if validation passes:
```java
Book book = bookValidationService.validateAndCreateBook(
    "Clean Code",
    "Robert Martin",
    "No Fiction",
    "Physical",
    "Available"
);
```

#### `validateBasicFields()`
Validates only title and author:
```java
ValidationResult result = bookValidationService.validateBasicFields(
    "Clean Code",
    "Robert Martin"
);
```

#### `validateBook()`
Validates a complete Book object:
```java
Book book = new Book(...);
ValidationResult result = bookValidationService.validateBook(book);
```

#### `demonstrateValidationChain()`
Runs 10 test cases showing chain behavior:
- Valid inputs
- Empty/null values
- Too long/short values
- Invalid characters

## REST API Endpoints

### Validation Operations

```bash
# Create a book with validation
POST /books/validation/create?title=Clean Code&author=Robert Martin&type=No Fiction&format=Physical&state=Available

# Validate title and author only
GET /books/validation/validate-basic?title=Clean Code&author=Robert Martin

# Validate a complete book
POST /books/validation/validate-book
Content-Type: application/json
{
  "title": "Clean Code",
  "author": "Robert Martin",
  "type": "No Fiction",
  "format": "Physical",
  "state": "Available"
}

# Run validation demonstration
GET /books/validation/demo

# Get validator information
GET /books/validation/info
```

## Usage Examples

### Example 1: Building a Validation Chain
```java
// Create individual validators
BookValidator titleValidator = new TitleValidator();
BookValidator authorValidator = new AuthorValidator();

// Link them together
titleValidator.setNext(authorValidator);

// Use the chain
try {
    titleValidator.validate("Clean Code", "Robert Martin");
    System.out.println("Validation passed!");
} catch (ValidationException e) {
    System.out.println("Validation failed: " + e.getMessage());
}
```

### Example 2: Using Factory for Pre-configured Chains
```java
// Get basic validation chain (Title → Author)
BookValidator chain = ValidationChainFactory.createBasicValidationChain();

try {
    chain.validate("1984", "George Orwell");
} catch (ValidationException e) {
    System.out.println("Failed at: " + e.getValidatorName());
}
```

### Example 3: Creating a Custom Chain
```java
BookValidator customChain = ValidationChainFactory.createCustomChain(
    new TitleValidator(),
    new AuthorValidator(),
    new TypeValidator()
);

customChain.validate("The Hobbit", "J.R.R. Tolkien");
```

### Example 4: Service Layer Usage
```java
@Service
public class BookManagementService {
    private final BookValidationService validationService;
    
    public Book createBook(String title, String author, ...) {
        try {
            return validationService.validateAndCreateBook(
                title, author, type, format, state
            );
        } catch (ValidationException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
```

### Example 5: REST API Usage
```bash
# Valid book creation
curl -X POST "http://localhost:8080/books/validation/create?title=Clean Code&author=Robert Martin&type=No Fiction&format=Physical&state=Available"

# Response:
{
  "success": true,
  "message": "Book validated and created successfully",
  "book": {
    "id": 6,
    "title": "Clean Code",
    "author": "Robert Martin",
    "type": "No Fiction",
    "format": "Physical",
    "state": "Available"
  }
}

# Invalid title
curl -X POST "http://localhost:8080/books/validation/create?title=&author=Robert Martin&type=Fiction&format=Digital&state=Available"

# Response:
{
  "success": false,
  "error": "[TitleValidator] Title cannot be empty or blank",
  "validator": "TitleValidator"
}
```

## Benefits of Chain of Responsibility Pattern

### 1. **Decoupling** ✅
Each validator is independent and doesn't know about other validators:
```java
// Validators don't know about each other
TitleValidator title = new TitleValidator();
AuthorValidator author = new AuthorValidator();
// Linked externally
title.setNext(author);
```

### 2. **Single Responsibility** ✅
Each validator has one specific validation responsibility:
- TitleValidator → Only validates titles
- AuthorValidator → Only validates authors
- No mixed concerns

### 3. **Flexibility** ✅
Easy to reorder, add, or remove validators:
```java
// Original chain
Title → Author → Type

// Modified chain
Title → Type → Author

// Extended chain
Title → Author → Type → Format → State → Custom
```

### 4. **Open/Closed Principle** ✅
Add new validators without modifying existing ones:
```java
public class ISBNValidator extends BookValidator {
    @Override
    protected void doValidate(String title, String author) throws ValidationException {
        // New validation logic
    }
}
```

### 5. **Easy Testing** ✅
Each validator can be tested independently:
```java
@Test
void testTitleValidator() {
    TitleValidator validator = new TitleValidator();
    assertDoesNotThrow(() -> validator.validate("Valid Title", "Author"));
    assertThrows(ValidationException.class, 
        () -> validator.validate("", "Author"));
}
```

## Validation Chain Behavior

### Sequential Processing
```
Input: title="", author="Martin"

1. TitleValidator
   ├─ Check: title is empty
   └─ Result: FAIL → throw ValidationException
   
Chain stops here! AuthorValidator never executes.
```

```
Input: title="Clean Code", author="M"

1. TitleValidator
   ├─ Check: title is valid
   └─ Result: PASS → continue to next
   
2. AuthorValidator
   ├─ Check: author too short
   └─ Result: FAIL → throw ValidationException
```

```
Input: title="Clean Code", author="Robert Martin"

1. TitleValidator
   ├─ Check: title is valid
   └─ Result: PASS → continue to next
   
2. AuthorValidator
   ├─ Check: author is valid
   └─ Result: PASS → no more validators
   
All validations passed! ✓
```

## Design Principles Applied

### SOLID Principles
- **Single Responsibility:** Each validator validates one thing
- **Open/Closed:** Add new validators without changing existing code
- **Liskov Substitution:** All validators can replace each other
- **Interface Segregation:** Clean, focused validator interface
- **Dependency Inversion:** Depend on BookValidator abstraction

### Additional Principles
- **Fail Fast:** Stops at first validation failure
- **Clear Error Messages:** Includes validator name in exceptions
- **Separation of Concerns:** Validation separate from business logic
- **Reusability:** Validators can be used in different chains

## When to Use Chain of Responsibility

✅ **Use When:**
- Multiple objects can handle a request
- Handler isn't known in advance
- Want to decouple sender and receiver
- Want to add handlers dynamically

❌ **Avoid When:**
- Only one handler exists
- Request must be handled by all handlers (use Observer instead)
- Handler selection is trivial (simple if/else suffices)

## Pattern Variations

### Current Implementation: Sequential Chain
Each validator processes and passes to next:
```java
validate() {
    doValidate();        // Process
    if (next != null) {
        next.validate(); // Pass along
    }
}
```

### Alternative: Conditional Chain
Each validator decides whether to continue:
```java
validate() {
    if (canHandle()) {
        doValidate();
        return true;  // Stop here
    }
    return next != null && next.validate(); // Try next
}
```

## Real-World Examples

### 1. Event Handling (GUI Frameworks)
```
Button Click → Button Handler → Panel Handler → Window Handler
```

### 2. Logging Frameworks
```
Debug Logger → Info Logger → Warning Logger → Error Logger
```

### 3. Middleware/Filter Chains
```
Auth Filter → Logging Filter → CORS Filter → Request Handler
```

### 4. Support Ticket System
```
Level 1 Support → Level 2 Support → Manager → Director
```

## Testing the Implementation

```bash
# 1. Start application
./gradlew bootRun

# 2. Create valid book
curl -X POST "http://localhost:8080/books/validation/create?title=Clean Code&author=Robert Martin&type=No Fiction&format=Physical&state=Available"

# 3. Test invalid title
curl -X POST "http://localhost:8080/books/validation/create?title=&author=Martin&type=Fiction&format=Digital&state=Available"

# 4. Test invalid author
curl -X POST "http://localhost:8080/books/validation/create?title=Clean Code&author=M&type=Fiction&format=Digital&state=Available"

# 5. Validate basic fields
curl "http://localhost:8080/books/validation/validate-basic?title=1984&author=George Orwell"

# 6. Run demonstration
curl http://localhost:8080/books/validation/demo

# 7. Get validator info
curl http://localhost:8080/books/validation/info
```

## Integration with Other Patterns

The Chain of Responsibility pattern works with other patterns:

```
Chain of Responsibility (validates data)
    ↓ if valid
Factory Pattern (creates book)
    ↓ saves
Repository Pattern (persists)
    ↓ triggers
Observer Pattern (notifies changes)
```

## Architecture Alignment

The pattern integrates with hexagonal architecture:

- **Domain Layer** (`domain/validator/`): Validators and exception
- **Application Layer** (`application/services/`): BookValidationService
- **Infrastructure Layer** (`infrastructure/adapters/web/`): REST API

## Summary

The Chain of Responsibility pattern provides:
- ✅ Flexible validation pipeline for book data
- ✅ Decoupled, independent validators
- ✅ Easy to extend with new validation rules
- ✅ Clear error messages with validator context
- ✅ Fail-fast behavior for performance
- ✅ SOLID principles compliance

Perfect for sequential processing where each step can pass or fail! 🔗

