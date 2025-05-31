package com.domain.studyroom.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/studyroom_db";
        String user = "root";
        String password = "msLsw/1026";
        return DriverManager.getConnection(url, user, password);
    }
}
