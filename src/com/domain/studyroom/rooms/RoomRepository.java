package com.domain.studyroom.rooms;

import com.domain.studyroom.db.DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RoomRepository {

    private int generateRandomRoomId() {
        Random rand = new Random();
        return 100000 + rand.nextInt(900000);
    }

    private int generateUniqueRoomId() throws SQLException, ClassNotFoundException {
        int roomId;
        do {
            roomId = generateRandomRoomId();
        } while (roomExists(roomId));
        return roomId;
    }

    // 비밀번호 조회 (throws로 예외 전파, 로깅 추가)
    public String getRoomPassword(int roomId) throws SQLException, ClassNotFoundException {
        System.out.println("> [Repo] getRoomPassword called with roomId=" + roomId);
        String sql = "SELECT password FROM rooms WHERE id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("> [Repo] no room found for id=" + roomId);
                    return null;
                }
                String pw = rs.getString("password");
                System.out.println("> [Repo] fetched password: " + (pw != null ? "***" : "null"));
                return pw;
            }
        }
    }

    // (이하 기존 메소드는 모두 throws 선언을 맞춰주세요)

    public String getRoomName(int roomId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT name FROM rooms WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        }
        return null;
    }

    public String getRoomCreator(int roomId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT creator FROM rooms WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("creator");
            }
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
    public int createRoom(String name, String password, String creator) throws SQLException, ClassNotFoundException {
        try (Connection conn = DB.getConnection()) {
            int roomId = generateUniqueRoomId();
            String sql = "INSERT INTO rooms (id, name, password, creator, user_count) VALUES (?, ?, ?, ?, 0)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            pstmt.setString(2, name);
            pstmt.setString(3, password);
            pstmt.setString(4, creator);
            pstmt.executeUpdate();
            return roomId;
        }
    }

    public int createRoom(String name, String password) throws SQLException, ClassNotFoundException {
        return createRoom(name, password, "익명");
    }

    public boolean enterRoom(int roomId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DB.getConnection()) {
            String sql = "UPDATE rooms SET user_count = user_count + 1 WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public boolean leaveRoom(int roomId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DB.getConnection()) {
            String sql = "UPDATE rooms SET user_count = user_count - 1 WHERE id = ? AND user_count > 0";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public int getUserCount(int roomId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT user_count FROM rooms WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("user_count");
        }
        return -1;
    }

    public boolean autoDeleteRoom(int roomId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DB.getConnection()) {
            String sql = "DELETE FROM rooms WHERE id = ? AND user_count = 0";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            return pstmt.executeUpdate() > 0;
        }
    }

    public Room getRoomById(int roomId) throws SQLException, ClassNotFoundException {
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT * FROM rooms WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Room(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("user_count"),
                        rs.getString("creator")
                );
            }
        }
        return null;
    }

    public List<Room> getAllRooms() throws SQLException, ClassNotFoundException {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT * FROM rooms ORDER BY id DESC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("password"),
                        rs.getInt("user_count"),
                        rs.getString("creator")
                ));
            }
        }
        return rooms;
    }
}
