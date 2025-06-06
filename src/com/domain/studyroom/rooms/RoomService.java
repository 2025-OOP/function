package com.domain.studyroom.rooms;

import com.domain.studyroom.db.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomService {
    private final RoomRepository repository = new RoomRepository();

    public int createRoom(String name, String password) throws SQLException {
        return repository.createRoom(name, password);
    }

    public boolean enterRoom(int roomId, String inputPassword) throws SQLException {
        String storedPassword = repository.getRoomPassword(roomId);

        // 비밀번호가 설정돼 있으면 검증
        if (storedPassword != null && !storedPassword.isEmpty()) {
            if (inputPassword == null || !storedPassword.equals(inputPassword)) {
                return false; // 비밀번호 틀림
            }
        }

        return repository.enterRoom(roomId);
    }

    public boolean leaveRoom(int roomId) throws SQLException {
        return repository.leaveRoom(roomId);
    }

    public boolean autoDeleteRoom(int roomId) throws SQLException {
        return repository.autoDeleteRoom(roomId);
    }

    public List<Room> getAllRooms() throws Exception {
        List<Room> rooms = new ArrayList<>();
        try (Connection conn = DB.getConnection()) {
            String sql = "SELECT id, name FROM rooms";  // password는 보내지 않음
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                rooms.add(new Room(
                        rs.getInt("id"),
                        rs.getString("name")
                ));
            }
        }
        return rooms;
    }


}
