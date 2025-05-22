package studyapp.todo.model;

public class Todo {
    private String content;
    private boolean completed;

    // 기본 생성자
    public Todo() {
    }

    public Todo(String content) {
        this.content = content;
        this.completed = false;
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

