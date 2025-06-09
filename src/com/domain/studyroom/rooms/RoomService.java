package com.domain.studyroom.rooms;

import java.sql.SQLException;
import java.util.List;

public class RoomService {
    private final RoomRepository repository = new RoomRepository();

    public int createRoom(String name, String password, String creator)
            throws SQLException, ClassNotFoundException {
        return repository.createRoom(name, password, creator);
    }

    public int createRoom(String name, String password)
            throws SQLException, ClassNotFoundException {
        return repository.createRoom(name, password, "익명");
    }

    public boolean enterRoom(int roomId, String inputPassword)
            throws SQLException, ClassNotFoundException {
        String storedPassword = repository.getRoomPassword(roomId);
        System.out.println("🔍 DB 비밀번호: " + (storedPassword != null ? "***" : "null"));
        System.out.println("🔍 입력 비밀번호: " + (inputPassword  != null ? "***" : "null"));

        if (storedPassword != null && !storedPassword.isEmpty()) {
            if (inputPassword == null || !storedPassword.equals(inputPassword)) {
                System.out.println("❌ 비밀번호 불일치!");
                return false;
            }
        }
        System.out.println("✅ 비밀번호 검증 통과");
        return repository.enterRoom(roomId);
    }

    public boolean leaveRoom(int roomId)
            throws SQLException, ClassNotFoundException {
        return repository.leaveRoom(roomId);
    }

    public boolean autoDeleteRoom(int roomId)
            throws SQLException, ClassNotFoundException {
        return repository.autoDeleteRoom(roomId);
    }

    public String getRoomCreator(int roomId)
            throws SQLException, ClassNotFoundException {
        return repository.getRoomCreator(roomId);
    }

    public List<Room> getAllRooms()
            throws SQLException, ClassNotFoundException {
        return repository.getAllRooms();
    }

    public Room getRoomById(int roomId)
            throws SQLException, ClassNotFoundException {
        return repository.getRoomById(roomId);
    }
}
