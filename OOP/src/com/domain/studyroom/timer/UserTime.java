package com.domain.studyroom.timer;

public class UserTime {
    private String userId;
    private int hours;
    private int minutes;
    private int seconds;

    public UserTime(String userId, int hours, int minutes, int seconds) {
        this.userId = userId;
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public String getUserId() {
        return userId;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    @Override
    public String toString() {
        return userId + ": " + hours + "h " + minutes + "m " + seconds + "s";
    }
}
