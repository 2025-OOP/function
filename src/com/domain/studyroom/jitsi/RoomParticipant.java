package com.domain.studyroom.jitsi;

public class RoomParticipant {
    private int id;
    private int roomId;
    private String username;
    private String RoomName;

    public RoomParticipant(int roomId, String username, String RoomName) {
        this.roomId = roomId;
        this.username = username;
        this.RoomName = RoomName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String RoomName) {
        this.RoomName = RoomName;
    }
}