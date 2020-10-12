package com.gb.lesson_3.server.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection getInstance() {
        try {
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_app?serverTimezone=Europe/Moscow", "mysql", "mysql");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
