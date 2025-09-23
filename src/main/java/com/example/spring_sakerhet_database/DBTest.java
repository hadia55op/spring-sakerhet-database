package com.example.spring_sakerhet_database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTest {
    public static void main(String[] args) throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection conn = DriverManager.getConnection("jdbc:sqlite:C:/Users/hakh7/OneDrive/Desktop/bibblan.db");
        System.out.println(" Connected to SQLite!");
        conn.close();
    }
}

