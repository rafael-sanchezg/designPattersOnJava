package com.pocs.designpatterns.designpattersonjava;

import com.pocs.designpatterns.designpattersonjava.domain.Book;
import com.pocs.designpatterns.designpattersonjava.infrastructure.BookRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookRepository bookRepository = new BookRepository();

    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
}

