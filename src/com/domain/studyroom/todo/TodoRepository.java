package com.domain.studyroom.todo;

import com.domain.studyroom.db.DB;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TodoRepository {

    public void insertTodo(Todo todo) throws Exception {
        try (Connection conn = DB.getConnection()) {
            String sql = "INSERT INTO todo (user_id, date, content, completed) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, todo.getUserId());
            pstmt.setString(2, todo.getDate());
            pstmt.setString(3, todo.getContent());
            pstmt.setBoolean(4, todo.isCompleted());
            pstmt.executeUpdate();
        }
    }

    public List<Todo> selectTodos(String userId, String date) throws Exception {
        List<Todo> list = new ArrayList<>();
        try (Connection conn = DB.getConnection()) {
            String sql;
            PreparedStatement pstmt;
            if (date != null) {
                sql = "SELECT * FROM todo WHERE user_id = ? AND date = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userId);
                pstmt.setString(2, date);
            } else {
                sql = "SELECT * FROM todo WHERE user_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, userId);
            }

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Todo(
                        rs.getInt("id"),
                        rs.getString("user_id"),
                        rs.getString("date"),
                        rs.getString("content"),
                        rs.getBoolean("completed")
                ));
            }
        }
        return list;
    }

    public void updateTodo(int todoId, Todo todo) throws Exception {
        try (Connection conn = DB.getConnection()) {
            String sql = "UPDATE todo SET content = ?, completed = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, todo.getContent());
            pstmt.setBoolean(2, todo.isCompleted());
            pstmt.setInt(3, todoId);
            pstmt.executeUpdate();
        }
    }

    public void deleteTodo(int todoId) throws Exception {
        try (Connection conn = DB.getConnection()) {
            String sql = "DELETE FROM todo WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, todoId);
            pstmt.executeUpdate();
        }
    }

    // Query parsing utility (moved from QueryUtils)
    public static Map<String, String> parseQuery(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.isEmpty()) return map;

        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=");
            if (parts.length == 2) {
                map.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8),
                        URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
            }
        }
        return map;
    }
}
