package usersystem.api;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import usersystem.model.Todo;
import usersystem.service.TodoService;
import usersystem.utils.QueryUtils;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class TodoApiController {
    private static final Gson gson = new Gson();
    private static final TodoService todoService = new TodoService();

    // POST /api/todo → 할 일 추가
    public static class TodoAddHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            try {
                if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                    sendResponse(exchange, 405, "Method Not Allowed");
                    return;
                }

                Todo todo = gson.fromJson(
                        new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8),
                        Todo.class
                );

                todoService.addTodo(todo);
                sendJson(exchange, 200, Map.of("status", "success", "message", "할 일 추가됨"));
            } catch (Exception e) {
                handleError(exchange, e);
            }
        }
    }

    // GET /api/todo?user=...&date=... → 특정 날짜의 할 일 조회
    public static class TodoListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            try {
                if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    sendResponse(exchange, 405, "Method Not Allowed");
                    return;
                }

                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = QueryUtils.parseQuery(query);
                String userId = params.get("user");
                String date = params.get("date");

                List<Todo> todos = todoService.getTodos(userId, date);
                sendJson(exchange, 200, todos);
            } catch (Exception e) {
                handleError(exchange, e);
            }
        }
    }

    // PUT 또는 DELETE /api/todo/{id}
    public static class TodoUpdateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            try {
                String method = exchange.getRequestMethod();

                if (!method.equalsIgnoreCase("PUT") && !method.equalsIgnoreCase("DELETE")) {
                    sendResponse(exchange, 405, "Method Not Allowed");
                    return;
                }

                String path = exchange.getRequestURI().getPath(); // /api/todo/3
                int todoId = Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));

                if (method.equalsIgnoreCase("PUT")) {
                    Todo todo = gson.fromJson(
                            new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8),
                            Todo.class
                    );
                    todoService.updateTodo(todoId, todo);
                    sendJson(exchange, 200, Map.of("status", "success", "message", "할 일 수정 완료"));
                } else if (method.equalsIgnoreCase("DELETE")) {
                    todoService.deleteTodo(todoId);
                    sendJson(exchange, 200, Map.of("status", "success", "message", "삭제 완료"));
                }
            } catch (Exception e) {
                handleError(exchange, e);
            }
        }
    }

    // 마이페이지 핸들러
    public static class MypageHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            try {
                if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                    sendResponse(exchange, 405, "Method Not Allowed");
                    return;
                }

                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = QueryUtils.parseQuery(query);
                String userId = params.get("user");

                List<Todo> todos = todoService.getAllTodosByUser(userId);
                long completed = todos.stream().filter(Todo::isCompleted).count();

                sendJson(exchange, 200, Map.of(
                        "user", userId,
                        "total", todos.size(),
                        "completed", completed,
                        "incomplete", todos.size() - completed
                ));
            } catch (Exception e) {
                handleError(exchange, e);
            }
        }
    }

    // ---------- 유틸 ----------

    private static void sendResponse(HttpExchange exchange, int code, String msg) {
        try {
            exchange.sendResponseHeaders(code, msg.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(msg.getBytes());
            }
        } catch (Exception ignored) {}
    }

    private static void sendJson(HttpExchange exchange, int code, Object data) {
        try {
            String json = gson.toJson(data);
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            sendResponse(exchange, code, json);
        } catch (Exception ignored) {}
    }

    private static void handleError(HttpExchange exchange, Exception e) {
        e.printStackTrace();
        sendJson(exchange, 500, Map.of("status", "error", "message", e.getMessage()));
    }
}
