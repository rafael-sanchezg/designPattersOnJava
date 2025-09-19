package com.pocs.designpatterns.designpattersonjava.infrastructure.adapters.persistence;

import com.pocs.designpatterns.designpattersonjava.application.ports.out.BookRepositoryPort;
import com.pocs.designpatterns.designpattersonjava.domain.model.Book;
import com.pocs.designpatterns.designpattersonjava.infrastructure.config.H2DatabaseSingleton;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository adapter for accessing books from the H2 database.
 * Implements the BookRepositoryPort for hexagonal architecture.
 */
@Repository
public class BookRepositoryAdapter implements BookRepositoryPort {

    @Override
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

    @Override
    public Book save(Book book) {
        String sql = "INSERT INTO book (title, author, type, format, state) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = H2DatabaseSingleton.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, book.title());
            stmt.setString(2, book.author());
            stmt.setString(3, book.type());
            stmt.setString(4, book.format());
            stmt.setString(5, book.state());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating book failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    return new Book(generatedId, book.title(), book.author(), book.type(), book.format(), book.state());
                } else {
                    throw new SQLException("Creating book failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save book", e);
        }
    }

    @Override
    public Book findById(int id) {
        String sql = "SELECT id, title, author, type, format, state FROM book WHERE id = ?";
        try (Connection conn = H2DatabaseSingleton.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("type"),
                            rs.getString("format"),
                            rs.getString("state")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find book by id", e);
        }
        return null;
    }

    @Override
    public void deleteById(int id) {
        String sql = "DELETE FROM book WHERE id = ?";
        try (Connection conn = H2DatabaseSingleton.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete book", e);
        }
    }
}
