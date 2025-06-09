package com.domain.studyroom.rooms;

public class Room {
    private int id;
    private String name;
    private String password;
    private int userCount;
    private String creator;  // 방 생성자 정보 추가
    private boolean secret;

    // 전체 필드용 생성자 (creator 포함)
    public Room(int id, String name, String password, int userCount, String creator) {
        this.id = id;
        this.name = name;
        this.password = password;  // ← null이 아닌 실제 password 값 사용
        this.userCount = userCount;
        this.creator = creator;
        this.secret = password != null && !password.isEmpty();  // password 기반으로 설정
    }

    // 기존 호환성을 위한 생성자 (creator 없는 버전)
    public Room(int id, String name, String password, int userCount) {
        this.id = id;
        this.name = name;
        this.password = password;  // ← null이 아닌 실제 password 값 사용
        this.userCount = userCount;
        this.creator = null;
        this.secret = password != null && !password.isEmpty();  // password 기반으로 설정
    }

    // creator 포함 (비밀번호 없이)
    public Room(int id, String name, String creator) {
        this.id = id;
        this.name = name;
        this.password = null;  // 비밀번호 없는 생성자이므로 null 유지
        this.userCount = 0;
        this.creator = creator;
        this.secret = false;  // 비밀번호 없으므로 false
    }

    // 기존 호환성을 위한 생성자 (비밀번호 없이, creator 없이)
    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        this.password = null;  // 비밀번호 없는 생성자이므로 null 유지
        this.userCount = 0;
        this.creator = null;
        this.secret = false;  // 비밀번호 없으므로 false
    }

    // 비밀번호와 creator를 모두 받는 생성자 추가
    public Room(int id, String name, String password, String creator) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.userCount = 0;
        this.creator = creator;
        this.secret = password != null && !password.isEmpty();
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

    public void setPassword(String password) {
        this.password = password;
        this.secret = password != null && !password.isEmpty();
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    // creator getter/setter 추가
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public boolean isSecret() {
        return secret;  // 또는 return password != null && !password.isEmpty();
    }

    public void setSecret(boolean secret) {
        this.secret = secret;
    }
}