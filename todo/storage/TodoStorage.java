package studyapp.todo.storage;

import studyapp.todo.model.Todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoStorage {

    // 내부 저장소 (정적 리스트)
    private static final List<Todo> todoList = new ArrayList<>();

    // 할 일 전체 조회 (외부에서 수정 못하도록 불변 리스트 반환)
    public static List<Todo> getTodoList() {
        return Collections.unmodifiableList(todoList);
    }

    // 할 일 추가
    public static void addTodo(Todo todo) {
        todoList.add(todo);
    }

    // 할 일 삭제 (index 유효성 확인)
    public static void deleteTodo(int index) {
        if (index >= 0 && index < todoList.size()) {
            todoList.remove(index);
        }
    }

    // 완료 여부 토글 (index 유효성 확인)
    public static void toggleComplete(int index) {
        if (index >= 0 && index < todoList.size()) {
            Todo todo = todoList.get(index);
            todo.setCompleted(!todo.isCompleted());
        }
    }
}
