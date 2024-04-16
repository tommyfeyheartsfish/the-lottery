package com.anwithayi.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    private static final String URL = "jdbc:mysql://localhost:3306/gameDB";
    private static final String USER = "anwithayi";
    private static final String PASSWORD = "PLease1998!";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the MySQL server successfully.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        }
        return conn;
    }
}
