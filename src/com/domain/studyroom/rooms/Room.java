package com.domain.studyroom.rooms;

public class Room {
    private int id;
    private String name;
    private String password;
    private int userCount;

    // 전체 필드용 생성자
    public Room(int id, String name, String password, int userCount) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.userCount = userCount;
    }

    // 비밀번호 없이 사용할 때
    public Room(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getter/Setter
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }
}
