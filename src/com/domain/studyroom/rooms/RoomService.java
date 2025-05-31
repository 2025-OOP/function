package com.domain.studyroom.rooms;

import java.sql.SQLException;

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
}
