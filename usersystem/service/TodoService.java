package usersystem.service;

import usersystem.model.Todo;
import usersystem.storage.TodoStorage;

import java.util.List;

/**
 * TodoService는 비즈니스 로직을 처리하는 계층으로,
 * 컨트롤러와 저장소(TodoStorage) 사이의 중간 역할을 수행합니다.
 */
public class TodoService {

    // 실제 데이터 저장 및 조회를 담당하는 저장소 객체
    private final TodoStorage storage = new TodoStorage();

    /**
     * 새로운 할 일을 추가합니다.
     *
     * @param todo 추가할 할 일 객체
     * @throws Exception 저장 중 오류 발생 시
     */
    public void addTodo(Todo todo) throws Exception {
        storage.insertTodo(todo);
    }

    /**
     * 특정 사용자(userId)의 특정 날짜(date)에 해당하는 할 일 목록을 조회합니다.
     *
     * @param userId 사용자 ID
     * @param date   날짜 (yyyy-MM-dd)
     * @return 해당 조건의 할 일 목록
     * @throws Exception 조회 중 오류 발생 시
     */
    public List<Todo> getTodos(String userId, String date) throws Exception {
        return storage.selectTodos(userId, date);
    }

    /**
     * 특정 ID의 할 일을 수정합니다.
     *
     * @param todoId 수정할 할 일 ID
     * @param todo   새로운 내용이 담긴 Todo 객체
     * @throws Exception 수정 중 오류 발생 시
     */
    public void updateTodo(int todoId, Todo todo) throws Exception {
        storage.updateTodo(todoId, todo);
    }

    /**
     * 특정 ID의 할 일을 삭제합니다.
     *
     * @param todoId 삭제할 할 일 ID
     * @throws Exception 삭제 중 오류 발생 시
     */
    public void deleteTodo(int todoId) throws Exception {
        storage.deleteTodo(todoId);
    }

    /**
     * 특정 사용자의 전체 할 일 목록을 조회합니다 (마이페이지 용).
     *
     * @param userId 사용자 ID
     * @return 해당 사용자의 전체 할 일 리스트
     * @throws Exception 조회 중 오류 발생 시
     */
    public List<Todo> getAllTodosByUser(String userId) throws Exception {
        return storage.selectTodosByUser(userId);
    }
}
