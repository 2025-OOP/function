package com.domain.studyroom.todo;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class MypageController implements HttpHandler {
    private static final Gson gson = new Gson();
    private final TodoService service = new TodoService();

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                exchange.sendResponseHeaders(405, -1);
                return;
            }

            Map<String, String> params = TodoRepository.parseQuery(exchange.getRequestURI().getQuery());
            String userId = params.get("user");

            if (userId == null || userId.isBlank()) {
                sendJson(exchange, 400, Map.of("status", "fail", "message", "user 파라미터가 필요합니다."));
                return;
            }

            List<Todo> todos = service.getTodos(userId, null);
            sendJson(exchange, 200, Map.of(
                    "username", userId,
                    "todoCount", todos.size()
            ));
        } catch (Exception e) {
            e.printStackTrace();
            try {
                sendJson(exchange, 500, Map.of("status", "error", "message", e.getMessage()));
            } catch (Exception ignored) {}
        }
    }

    private void sendJson(HttpExchange exchange, int status, Map<String, Object> data) throws Exception {
        String json = gson.toJson(data);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
