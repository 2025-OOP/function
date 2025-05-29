package usersystem.storage;

import usersystem.model.Todo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * TodoStorage 클래스는 MySQL 데이터베이스와 직접 통신하여
 * 할 일(Todo) 데이터를 저장, 조회, 수정, 삭제하는 역할을 합니다.
 */
public class TodoStorage {

    /**
     * MySQL 데이터베이스와의 연결을 생성합니다.
     *
     * @return DB 연결 객체
     * @throws SQLException DB 오류 발생 시
     * @throws ClassNotFoundException JDBC 드라이버를 찾을 수 없을 때
     */
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/java_studyroom_project?serverTimezone=UTC&characterEncoding=UTF-8";
        String user = "root";
        String pass = "0327";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, user, pass);
    }

    /**
     * 새 할 일을 데이터베이스에 추가합니다.
     *
     * @param todo 추가할 Todo 객체
     * @throws Exception 예외 발생 시
     */
    public void insertTodo(Todo todo) throws Exception {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO todo (user_id, date, content, completed) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, todo.getUserId());
            pstmt.setString(2, todo.getDate());
            pstmt.setString(3, todo.getContent());
            pstmt.setBoolean(4, todo.isCompleted());
            pstmt.executeUpdate();
        }
    }

    /**
     * 특정 사용자와 날짜에 해당하는 할 일 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param date   날짜 (yyyy-MM-dd)
     * @return 해당 조건의 Todo 리스트
     * @throws Exception 예외 발생 시
     */
    public List<Todo> selectTodos(String userId, String date) throws Exception {
        List<Todo> list = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM todo WHERE user_id = ? AND date = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            pstmt.setString(2, date);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Todo(
                        rs.getInt("id"),
                        rs.getString("user_id"),
                        rs.getString("date"),
                        rs.getString("content"),
                        rs.getBoolean("completed")
                ));
            }
        }
        return list;
    }

    /**
     * 특정 ID의 할 일을 수정합니다.
     *
     * @param todoId 수정할 Todo의 ID
     * @param todo   수정할 내용이 담긴 Todo 객체
     * @throws Exception 예외 발생 시
     */
    public void updateTodo(int todoId, Todo todo) throws Exception {
        try (Connection conn = getConnection()) {
            String sql = "UPDATE todo SET content = ?, completed = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, todo.getContent());
            pstmt.setBoolean(2, todo.isCompleted());
            pstmt.setInt(3, todoId);
            pstmt.executeUpdate();
        }
    }

    /**
     * 특정 ID의 할 일을 삭제합니다.
     *
     * @param todoId 삭제할 Todo의 ID
     * @throws Exception 예외 발생 시
     */
    public void deleteTodo(int todoId) throws Exception {
        try (Connection conn = getConnection()) {
            String sql = "DELETE FROM todo WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, todoId);
            pstmt.executeUpdate();
        }
    }

    /**
     * 특정 사용자의 전체 할 일 목록을 조회합니다. (마이페이지 통계용)
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 전체 Todo 리스트
     * @throws Exception 예외 발생 시
     */
    public List<Todo> selectTodosByUser(String userId) throws Exception {
        List<Todo> list = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String sql = "SELECT * FROM todo WHERE user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new Todo(
                        rs.getInt("id"),
                        rs.getString("user_id"),
                        rs.getString("date"),
                        rs.getString("content"),
                        rs.getBoolean("completed")
                ));
            }
        }
        return list;
    }
}
