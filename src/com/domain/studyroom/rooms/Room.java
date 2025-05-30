package com.domain.studyroom.rooms;

public class Room {
    public int id;
    public String name;
    public String password;
    public int userCount;

    public Room(int id, String name, String password, int userCount) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.userCount = userCount;
    }
}
