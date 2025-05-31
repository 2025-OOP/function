package com.domain.studyroom.rooms;

import com.domain.studyroom.db.DB;

import java.sql.*;
import java.util.Random;

public class RoomRepository {

    private int generateRandomRoomId() {
        Random rand = new Random();
        return 100000 + rand.nextInt(900000); // 100000 ~ 999999
    }

    private int generateUniqueRoomId() throws SQLException {
        int roomId;
        do {
            roomId = generateRandomRoomId();
        } while (roomExists(roomId));
        return roomId;
    }

    // 비번 조회
    public String getRoomPassword(int roomId) throws SQLException {
        Connection conn = DB.getConnection();
        String sql = "SELECT password FROM rooms WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, roomId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getString("password");
        }
        return null;
    }

    private boolean roomExists(int roomId) throws SQLException {
        Connection conn = DB.getConnection();
        String sql = "SELECT COUNT(*) FROM rooms WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, roomId);
        ResultSet rs = pstmt.executeQuery();
        rs.next();
        return rs.getInt(1) > 0;
    }

    public int createRoom(String name, String password) throws SQLException {
        Connection conn = DB.getConnection();
        int roomId = generateUniqueRoomId();
        String sql = "INSERT INTO rooms (id, name, password) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, roomId);
        pstmt.setString(2, name);
        pstmt.setString(3, password);
        pstmt.executeUpdate();
        return roomId;
    }

    public boolean enterRoom(int roomId) throws SQLException {
        Connection conn = DB.getConnection();
        String sql = "UPDATE rooms SET user_count = user_count + 1 WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, roomId);
        return pstmt.executeUpdate() > 0;
    }

    public boolean leaveRoom(int roomId) throws SQLException {
        Connection conn = DB.getConnection();
        String sql = "UPDATE rooms SET user_count = user_count - 1 WHERE id = ? AND user_count > 0";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, roomId);
        return pstmt.executeUpdate() > 0;
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

    public boolean autoDeleteRoom(int roomId) throws SQLException {
        Connection conn = DB.getConnection();
        String sql = "DELETE FROM rooms WHERE id = ? AND user_count = 0";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, roomId);
        return pstmt.executeUpdate() > 0;
    }
}