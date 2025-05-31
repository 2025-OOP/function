package com.domain.studyroom.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/java_studyroom_project?serverTimezone=UTC&characterEncoding=UTF-8";
        String user = "root";
        String password = "Dktjdl9817#";
        return DriverManager.getConnection(url, user, password);
    }
}
