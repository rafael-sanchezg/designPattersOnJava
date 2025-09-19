package com.pocs.designpatterns.designpattersonjava.application.services;

import com.pocs.designpatterns.designpattersonjava.application.ports.in.BookManagementUseCase;
import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.domain.factory.BookFactory;
import com.pocs.designpatterns.designpattersonjava.domain.factory.BookAbstractFactory;
import com.pocs.designpatterns.designpattersonjava.domain.factory.BookFactoryProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

/**
 * Application service implementing book management use cases.
 * This service demonstrates the usage of Factory Method and Abstract Factory patterns
 * for book creation within the hexagonal architecture.
 */
@Service
public class BookManagementService implements BookManagementUseCase {

    private final BookRepositoryPort bookRepositoryPort;

    public BookManagementService(BookRepositoryPort bookRepositoryPort) {
        this.bookRepositoryPort = bookRepositoryPort;
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepositoryPort.findAll();
    }

    @Override
    public List<Book> createBooksWithFactoryMethod() {
        List<Book> books = new ArrayList<>();

        // Using Fiction factory
        BookFactory fictionFactory = BookFactoryProvider.getBookFactory("Fiction");
        Book book1 = fictionFactory.processBookCreation(0, "The Lord of the Rings", "J.R.R. Tolkien", "Physical", "Available");
        Book book2 = fictionFactory.processBookCreation(0, "Dune", "Frank Herbert", "Digital", "Available");

        // Save to database and add to result list
        books.add(bookRepositoryPort.save(book1));
        books.add(bookRepositoryPort.save(book2));

        // Using Non-Fiction factory
        BookFactory nonFictionFactory = BookFactoryProvider.getBookFactory("No Fiction");
        Book book3 = nonFictionFactory.processBookCreation(0, "Clean Architecture", "Robert C. Martin", "Physical", "Available");
        Book book4 = nonFictionFactory.processBookCreation(0, "Design Patterns", "Gang of Four", "Digital", "Loaned");

        // Save to database and add to result list
        books.add(bookRepositoryPort.save(book3));
        books.add(bookRepositoryPort.save(book4));

        return books;
    }

    @Override
    public List<Book> createBooksWithAbstractFactory() {
        List<Book> books = new ArrayList<>();

        // Using Fiction Abstract Factory
        BookAbstractFactory fictionAbstractFactory = BookFactoryProvider.getAbstractFactory("Fiction");
        Book book1 = fictionAbstractFactory.createPhysicalBook(0, "Harry Potter", "J.K. Rowling", "Available");
        Book book2 = fictionAbstractFactory.createDigitalBook(0, "The Hobbit", "J.R.R. Tolkien", "Loaned");

        // Save to database and add to result list
        books.add(bookRepositoryPort.save(book1));
        books.add(bookRepositoryPort.save(book2));

        // Using Non-Fiction Abstract Factory
        BookAbstractFactory nonFictionAbstractFactory = BookFactoryProvider.getAbstractFactory("No Fiction");
        Book book3 = nonFictionAbstractFactory.createPhysicalBook(0, "Effective Java", "Joshua Bloch", "Available");
        Book book4 = nonFictionAbstractFactory.createDigitalBook(0, "Java Concurrency in Practice", "Brian Goetz", "Available");

        // Save to database and add to result list
        books.add(bookRepositoryPort.save(book3));
        books.add(bookRepositoryPort.save(book4));

        return books;
    }

    @Override
    public List<Book> createBooksWithConvenienceMethods() {
        List<Book> books = new ArrayList<>();

        // Using Factory Method convenience method
        Book book1 = BookFactoryProvider.createBook("Fiction", 0, "1984", "George Orwell", "Digital", "Available");
        Book book2 = BookFactoryProvider.createBook("No Fiction", 0, "Refactoring", "Martin Fowler", "Physical", "Loaned");

        // Save to database and add to result list
        books.add(bookRepositoryPort.save(book1));
        books.add(bookRepositoryPort.save(book2));

        // Using Abstract Factory convenience method
        Book book3 = BookFactoryProvider.createBookWithFormat("Fiction", "Physical", 0, "Brave New World", "Aldous Huxley", "Available");
        Book book4 = BookFactoryProvider.createBookWithFormat("No Fiction", "Digital", 0, "Code Complete", "Steve McConnell", "Available");

        // Save to database and add to result list
        books.add(bookRepositoryPort.save(book3));
        books.add(bookRepositoryPort.save(book4));

        return books;
    }

    @Override
    public Book createBookDynamically(String bookType, boolean useAbstractFactory) {
        Book book;
        if (useAbstractFactory) {
            BookAbstractFactory factory = BookFactoryProvider.getAbstractFactory(bookType);
            book = factory.createDigitalBook(0, "Dynamic Book", "Dynamic Author", "Available");
        } else {
            BookFactory factory = BookFactoryProvider.getBookFactory(bookType);
            book = factory.createBook(0, "Dynamic Book", "Dynamic Author", "Digital", "Available");
        }

        // Save to database and return the persisted book
        return bookRepositoryPort.save(book);
    }

    @Override
    public Book createBookWithFactoryMethod(String bookType, String title, String author, String format, String state) {
        Book book = BookFactoryProvider.createBook(bookType, 0, title, author, format, state);
        // Save to database and return the persisted book
        return bookRepositoryPort.save(book);
    }

    @Override
    public Book createBookWithAbstractFactory(String bookType, String format, String title, String author, String state) {
        Book book = BookFactoryProvider.createBookWithFormat(bookType, format, 0, title, author, state);
        // Save to database and return the persisted book
        return bookRepositoryPort.save(book);
    }
}
