package com.pocs.designpatterns.designpattersonjava.infrastructure.adapters.web;

import com.pocs.designpatterns.designpattersonjava.application.ports.in.BookManagementUseCase;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller adapter for book management operations.
 * Delegates to the application layer through the input port.
 */
@RestController
@RequestMapping("/books")
public class BookController {

    private final BookManagementUseCase bookManagementUseCase;

    public BookController(BookManagementUseCase bookManagementUseCase) {
        this.bookManagementUseCase = bookManagementUseCase;
    }

    @GetMapping
    public List<Book> getAllBooks() {
        return bookManagementUseCase.getAllBooks();
    }

    /**
     * Demonstrates Factory Method pattern.
     * Creates books using specific factories for Fiction and Non-Fiction.
     */
    @GetMapping("/factory-method")
    public List<Book> getBooksCreatedWithFactoryMethod() {
        return bookManagementUseCase.createBooksWithFactoryMethod();
    }

    /**
     * Demonstrates Abstract Factory pattern.
     * Creates books using abstract factories that handle both physical and digital formats.
     */
    @GetMapping("/abstract-factory")
    public List<Book> getBooksCreatedWithAbstractFactory() {
        return bookManagementUseCase.createBooksWithAbstractFactory();
    }

    /**
     * Demonstrates convenience methods that combine both patterns.
     */
    @GetMapping("/convenience-methods")
    public List<Book> getBooksCreatedWithConvenienceMethods() {
        return bookManagementUseCase.createBooksWithConvenienceMethods();
    }

    /**
     * Creates a book dynamically using Factory Method pattern.
     */
    @PostMapping("/factory-method")
    public Book createBookWithFactoryMethod(
            @RequestParam String bookType,
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String format,
            @RequestParam String state) {
        return bookManagementUseCase.createBookWithFactoryMethod(bookType, title, author, format, state);
    }

    /**
     * Creates a book dynamically using Abstract Factory pattern.
     */
    @PostMapping("/abstract-factory")
    public Book createBookWithAbstractFactory(
            @RequestParam String bookType,
            @RequestParam String format,
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String state) {
        return bookManagementUseCase.createBookWithAbstractFactory(bookType, format, title, author, state);
    }

    /**
     * Demonstrates dynamic factory selection.
     */
    @PostMapping("/dynamic")
    public Book createBookDynamically(
            @RequestParam String bookType,
            @RequestParam boolean useAbstractFactory) {
        return bookManagementUseCase.createBookDynamically(bookType, useAbstractFactory);
    }

    /**
     * Provides information about available factory patterns and their usage.
     */
    @GetMapping("/patterns-info")
    public Map<String, Object> getPatternsInfo() {
        return Map.of(
            "Factory Method", Map.of(
                "description", "Creates books using specific factories for each book type",
                "endpoints", List.of("/books/factory-method", "POST /books/factory-method"),
                "bookTypes", List.of("Fiction", "No Fiction")
            ),
            "Abstract Factory", Map.of(
                "description", "Creates books using factories that handle multiple formats",
                "endpoints", List.of("/books/abstract-factory", "POST /books/abstract-factory"),
                "formats", List.of("Physical", "Digital")
            ),
            "Dynamic Creation", Map.of(
                "description", "Demonstrates runtime factory selection",
                "endpoint", "POST /books/dynamic",
                "parameters", Map.of(
                    "bookType", "Fiction or No Fiction",
                    "useAbstractFactory", "true for Abstract Factory, false for Factory Method"
                )
            )
        );
    }
}
