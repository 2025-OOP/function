package com.domain.studyroom.todo;

import java.util.List;

public class TodoService {
    private final TodoRepository repository = new TodoRepository();

    // 할 일 추가
    public void addTodo(Todo todo) throws Exception {
        repository.insertTodo(todo);
    }

    // 특정 사용자, 특정 날짜의 할 일 목록 조회
    public List<Todo> getTodos(String userId, String date) throws Exception {
        return repository.selectTodos(userId, date);
    }

    // 특정 할 일 수정
    public void updateTodo(int todoId, Todo todo) throws Exception {
        repository.updateTodo(todoId, todo);
    }

    // 특정 할 일 삭제
    public void deleteTodo(int todoId) throws Exception {
        repository.deleteTodo(todoId);
    }
}
