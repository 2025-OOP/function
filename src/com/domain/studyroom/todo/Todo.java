package com.domain.studyroom.todo;

public class Todo {
    private int id;            // todo_id
    private String userId;     // username
    private String date;       // yyyy-MM-dd
    private String content;    // 할 일 내용
    private boolean completed; // 완료 여부

    public Todo() {
    }

    public Todo(int id, String userId, String date, String content, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.content = content;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
} 