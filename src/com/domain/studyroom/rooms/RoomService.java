package com.domain.studyroom.rooms;

public class RoomService {
    private final RoomRepository repo = new RoomRepository();

    public int createRoom(String name, String password) throws Exception {
        return repo.createRoom(name, password);
    }

    public boolean enterRoom(int roomId) throws Exception {
        return repo.incrementUser(roomId);
    }

    public boolean leaveRoom(int roomId) throws Exception {
        return repo.decrementUser(roomId);
    }

    public boolean autoDeleteRoom(int roomId) throws Exception {
        if (repo.getUserCount(roomId) == 0) {
            return repo.deleteRoom(roomId);
        }
        return false;
    }
}
