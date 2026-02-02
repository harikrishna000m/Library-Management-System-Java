package com.library;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                    "root",
                    "v1i2j3i4"
            );
            System.out.println("Database Connected Successfully!");
            return con;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}