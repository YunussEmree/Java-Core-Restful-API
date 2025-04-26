package com.yunussemree.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    private static final String BASE_DB_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "userdb";
    private static final String DB_URL = BASE_DB_URL + DB_NAME;
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Sunuyerme120*";

    private static final Connection connection;

    static {
        try {
            Connection initialConnection = java.sql.DriverManager.getConnection(BASE_DB_URL, DB_USER, DB_PASSWORD);

            try (Statement stmt = initialConnection.createStatement()) {
                stmt.execute("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
                logger.info("Database created or already exists");
            }
            initialConnection.close();

            connection = java.sql.DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            initDatabase();
            logger.info("Database initialized successfully");
        } catch (Exception e) {
            logger.error("Error initializing database", e);
            throw new RuntimeException("Failed to initialize database", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return java.sql.DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    private static void initDatabase() {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY, "
                    + "name VARCHAR(100) NOT NULL, "
                    + "email VARCHAR(100) NOT NULL, "
                    + "phone VARCHAR(20), "
                    + "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP"
                    + ")");

        } catch (SQLException e) {
            logger.error("Error creating database tables", e);
            throw new RuntimeException("Failed to create database tables", e);
        }
    }

}
