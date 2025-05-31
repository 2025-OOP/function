package com.domain.studyroom.rooms;

import com.domain.studyroom.db.DB;

import java.sql.*;

public class RoomRepository {

    public int createRoom(String name, String password) throws SQLException {
        String sql = "INSERT INTO rooms (name, password) VALUES (?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, password);
            ps.executeUpdate();

            ResultSet keys = ps.getGeneratedKeys();
            keys.next();
            return keys.getInt(1);
        }
    }

    public boolean incrementUser(int roomId) throws SQLException {
        String sql = "UPDATE rooms SET user_count = user_count + 1 WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean decrementUser(int roomId) throws SQLException {
        String sql = "UPDATE rooms SET user_count = user_count - 1 WHERE id = ? AND user_count > 0";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;
        }
    }

    public int getUserCount(int roomId) throws SQLException {
        String sql = "SELECT user_count FROM rooms WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("user_count");
        }
        return -1;
    }

    public boolean deleteRoom(int roomId) throws SQLException {
        String sql = "DELETE FROM rooms WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            return ps.executeUpdate() > 0;
        }
    }
}
