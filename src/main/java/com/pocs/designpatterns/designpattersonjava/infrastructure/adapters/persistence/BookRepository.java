package com.pocs.designpatterns.designpattersonjava.infrastructure.adapters.persistence;

import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.infrastructure.config.H2DatabaseSingleton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for accessing books from the H2 database.
 */
public class BookRepository {
    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        try (Connection conn = H2DatabaseSingleton.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, title, author, type, format, state FROM book")) {
            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("type"),
                        rs.getString("format"),
                        rs.getString("state")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch books", e);
        }
        return books;
    }
}

