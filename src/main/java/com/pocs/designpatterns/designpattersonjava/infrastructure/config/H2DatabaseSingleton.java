package com.pocs.designpatterns.designpattersonjava.infrastructure.config;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Singleton for managing H2 database connection.
 * Uses JDBC to connect to the in-memory H2 database.
 */
public final class H2DatabaseSingleton {
    private static final String JDBC_URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE;MODE=MySQL";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static volatile Connection connection;
    private static volatile boolean schemaInitialized = false;

    // Private constructor to prevent instantiation
    private H2DatabaseSingleton() {}

    /**
     * Returns the singleton Connection instance.
     * @return Connection to H2 database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            synchronized (H2DatabaseSingleton.class) {
                if (connection == null || connection.isClosed()) {
                    connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
                    if (!schemaInitialized) {
                        initializeSchema();
                        schemaInitialized = true;
                    }
                }
            }
        }
        return connection;
    }

    /**
     * Initializes the database schema by executing the schema.sql file.
     * @throws SQLException if a database access error occurs
     */
    private static void initializeSchema() throws SQLException {
        try (InputStream inputStream = H2DatabaseSingleton.class.getClassLoader()
                .getResourceAsStream("schema.sql")) {

            if (inputStream == null) {
                throw new SQLException("Could not find schema.sql in classpath");
            }

            String schemaScript = new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .collect(Collectors.joining("\n"));

            // Split by semicolons and execute each statement
            String[] statements = schemaScript.split(";");
            try (Statement stmt = connection.createStatement()) {
                for (String sql : statements) {
                    String trimmed = sql.trim();
                    if (!trimmed.isEmpty() && !trimmed.startsWith("//") && !trimmed.startsWith("--")) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (Exception e) {
            throw new SQLException("Failed to initialize database schema", e);
        }
    }
}
