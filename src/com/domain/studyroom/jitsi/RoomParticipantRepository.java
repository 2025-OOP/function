package com.domain.studyroom.jitsi;

import com.domain.studyroom.db.DB;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomParticipantRepository {

    /**
     * 방 참가자 추가
     */
    public void addParticipant(int roomId, String username, String RoomName) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO room_participants (room_id, username, jitsi_room_name) VALUES (?, ?, ?)";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setString(2, username);
            ps.setString(3, RoomName);
            ps.executeUpdate();
        }
    }

    /**
     * 방 참가자 제거
     */
    public void removeParticipant(int roomId, String username) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM room_participants WHERE room_id = ? AND username = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }

    /**
     * 방의 모든 참가자 username 조회
     */
    public List<String> getParticipantUsernames(int roomId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT username FROM room_participants WHERE room_id = ? ORDER BY id";
        List<String> usernames = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                usernames.add(rs.getString("username"));
            }
        }
        return usernames;
    }

    /**
     * 특정 방의 Jitsi 방 이름 조회 (첫 번째 참가자가 생성한 것 사용)
     */
    public String getJitsiRoomName(int roomId) throws SQLException, ClassNotFoundException {
        String sql = "SELECT jitsi_room_name FROM room_participants WHERE room_id = ? LIMIT 1";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("jitsi_room_name");
            }
        }
        return null;
    }

    /**
     * 사용자가 특정 방에 참가중인지 확인
     */
    public boolean isUserInRoom(int roomId, String username) throws SQLException, ClassNotFoundException {
        String sql = "SELECT COUNT(*) FROM room_participants WHERE room_id = ? AND username = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    /**
     * 방의 모든 참가자 제거 (방 삭제 시 사용)
     */
    public void removeAllParticipants(int roomId) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM room_participants WHERE room_id = ?";
        try (Connection conn = DB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, roomId);
            ps.executeUpdate();
        }
    }
}
