package usersystem.model;

/**
 * Todo 클래스는 하나의 할 일(Task)을 표현하는 데이터 모델입니다.
 * 사용자 ID, 날짜, 내용, 완료 여부 등의 정보를 담습니다.
 */
public class Todo {

    // 할 일 ID (Primary Key 또는 식별자)
    private int id;

    // 사용자 ID (username 또는 userId로 사용자의 식별 목적)
    private String userId;

    // 할 일을 수행할 날짜 (형식: yyyy-MM-dd)
    private String date;

    // 할 일의 구체적인 내용
    private String content;

    // 할 일의 완료 여부 (true: 완료, false: 미완료)
    private boolean completed;

    /**
     * 기본 생성자 (JSON 직렬화/역직렬화 및 객체 초기화를 위해 필요)
     */
    public Todo() {
    }

    /**
     * 전체 필드를 초기화하는 생성자
     *
     * @param id        할 일 ID
     * @param userId    사용자 ID
     * @param date      날짜 (yyyy-MM-dd)
     * @param content   할 일 내용
     * @param completed 완료 여부
     */
    public Todo(int id, String userId, String date, String content, boolean completed) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.content = content;
        this.completed = completed;
    }


    /**
     * @return 할 일 ID
     */
    public int getId() {
        return id;
    }

    /**
     * @param id 할 일 ID 설정
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return 사용자 ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId 사용자 ID 설정
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return 할 일 날짜 (yyyy-MM-dd)
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date 날짜 설정
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return 할 일 내용
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content 할 일 내용 설정
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * @return 완료 여부 (true: 완료, false: 미완료)
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * @param completed 완료 여부 설정
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
