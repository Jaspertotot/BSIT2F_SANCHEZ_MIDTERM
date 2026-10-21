package com.library.dao;

import com.library.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** All SQL for library books lives here. */
public class ScenarioDao {
    private static final String INSERT_SQL =
            "INSERT INTO books (title, author, isbn, status) VALUES (?, ?, ?, ?)";
    private static final String SELECT_ALL_SQL =
            "SELECT id, title, author, isbn, status FROM books ORDER BY id";

    public ScenarioDao() {
        DatabaseConnection.initializeDatabase();
    }

    /** Inserts a new book using a PreparedStatement. */
    public void insert(Book book) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            ps.setString(4, book.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Could not save book: " + e.getMessage(), e);
        }
    }

    /** Returns every book, ordered by ID. */
    public List<Book> findAll() {
        List<Book> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("status")));
            }
        } catch (SQLException e) {
            throw new DaoException("Could not load books: " + e.getMessage(), e);
        }
        return list;
    }
}
