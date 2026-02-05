package com.smartcommerce.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/ecommerce_db";
    private static final String USER = "root";
    private static final String PASSWORD = "noah_1@23.Djanor";

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✓ Database connected!");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("✗ Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null || !isConnectionValid()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private static boolean isConnectionValid() {
        try {
            return instance != null && instance.connection != null &&
                    !instance.connection.isClosed() && instance.connection.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    // FIXED: Now properly references the instance's connection
    public static Connection getConnection() {
        DatabaseConnection dbInstance = getInstance();
        try {
            if (dbInstance.connection == null || dbInstance.connection.isClosed()) {
                dbInstance.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dbInstance.connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Database closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
