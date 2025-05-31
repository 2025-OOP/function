package com.domain.studyroom.todo;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class TodoController implements HttpHandler {
    private static final Gson gson = new Gson();
    private final TodoService service = new TodoService();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            String method = exchange.getRequestMethod();
            URI uri = exchange.getRequestURI();
            String path = uri.getPath();

            if (method.equals("POST") && path.equals("/api/todo")) {
                handleAdd(exchange);
            } else if (method.equals("GET") && path.startsWith("/api/todo")) {
                handleList(exchange);
            } else if (method.equals("PUT") && path.matches("/api/todo/\\d+")) {
                handleUpdate(exchange, extractId(path));
            } else if (method.equals("DELETE") && path.matches("/api/todo/\\d+")) {
                handleDelete(exchange, extractId(path));
            } else {
                send(exchange, 404, "Not Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleAdd(HttpExchange exchange) throws Exception {
        Todo todo = gson.fromJson(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8),
                Todo.class
        );
        service.addTodo(todo);
        sendJson(exchange, 200, Map.of("status", "success", "message", "할 일 추가됨"));
    }

    private void handleList(HttpExchange exchange) throws Exception {
        Map<String, String> params = TodoRepository.parseQuery(exchange.getRequestURI().getQuery());
        String userId = params.get("user");
        String date = params.get("date");

        List<Todo> list = service.getTodos(userId, date);
        sendJson(exchange, 200, list);
    }

    private void handleUpdate(HttpExchange exchange, int todoId) throws Exception {
        Todo todo = gson.fromJson(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8),
                Todo.class
        );
        service.updateTodo(todoId, todo);
        sendJson(exchange, 200, Map.of("status", "success", "message", "수정 완료"));
    }

    private void handleDelete(HttpExchange exchange, int todoId) throws Exception {
        service.deleteTodo(todoId);
        sendJson(exchange, 200, Map.of("status", "success", "message", "삭제 완료"));
    }

    private int extractId(String path) {
        return Integer.parseInt(path.substring(path.lastIndexOf("/") + 1));
    }

    private void send(HttpExchange exchange, int code, String msg) throws Exception {
        exchange.sendResponseHeaders(code, msg.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(msg.getBytes());
        }
    }

    private void sendJson(HttpExchange exchange, int code, Object data) throws Exception {
        String json = gson.toJson(data);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        send(exchange, code, json);
    }
}
