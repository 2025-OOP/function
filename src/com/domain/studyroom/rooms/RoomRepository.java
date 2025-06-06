package com.domain.studyroom.rooms;

import com.domain.studyroom.db.DB;

import java.sql.*;
import java.util.Random;

public class RoomRepository {

    private int generateRandomRoomId() {
        Random rand = new Random();
        return 100000 + rand.nextInt(900000); // 100000 ~ 999999
    }

    private int generateUniqueRoomId() throws SQLException, ClassNotFoundException {
        int roomId;
        do {
            roomId = generateRandomRoomId();
        } while (roomExists(roomId));
        return roomId;
    }

    // 비밀번호 조회
    public String getRoomPassword(int roomId) {
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT password FROM rooms WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 방 존재 여부 확인
    private boolean roomExists(int roomId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT COUNT(*) FROM rooms WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    // 방 생성
    public int createRoom(String name, String password) {
        try (Connection conn = DB.getConnection()) {
            int roomId = generateUniqueRoomId();
            String sql = "INSERT INTO rooms (id, name, password) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.executeUpdate();
            return roomId;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 방 입장 (user_count 증가)
    public boolean enterRoom(int roomId) {
        try (Connection conn = DB.getConnection()) {
            String sql = "UPDATE rooms SET user_count = user_count + 1 WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 방 나가기 (user_count 감소)
    public boolean leaveRoom(int roomId) {
        try (Connection conn = DB.getConnection()) {
            String sql = "UPDATE rooms SET user_count = user_count - 1 WHERE id = ? AND user_count > 0";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 현재 사용자 수 확인
    public int getUserCount(int roomId) {
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT user_count FROM rooms WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("user_count");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // 사용자 0명일 때 방 자동 삭제
    public boolean autoDeleteRoom(int roomId) {
        try (Connection conn = DB.getConnection()) {
            String sql = "DELETE FROM rooms WHERE id = ? AND user_count = 0";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
