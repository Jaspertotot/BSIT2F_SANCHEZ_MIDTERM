package com.library.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Opens SQLite connections and creates the table on startup. */
public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:library.db";

    private static final String CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS books ("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "title TEXT NOT NULL, "
            + "author TEXT NOT NULL, "
            + "isbn TEXT NOT NULL, "
            + "status TEXT NOT NULL)";

    private DatabaseConnection() { }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Creates the books table if it does not exist yet. */
    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(CREATE_TABLE);
        } catch (SQLException e) {
            throw new DaoException("Could not initialize database: " + e.getMessage(), e);
        }
    }
}
