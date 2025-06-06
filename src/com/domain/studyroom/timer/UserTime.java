package com.domain.studyroom.timer;

public class UserTime {
    private String userId;
    private int hour;
    private int minute;
    private int second;

    public UserTime(String userId, int hour, int minute, int second) {
        this.userId = userId;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public String getUserId() {
        return userId;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public int getTotalSeconds() {
        return hour * 3600 + minute * 60 + second;
    }
}