package com.domain.studyroom.timer;

import com.google.gson.Gson;
import com.sun.net.httpserver.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TimerController implements HttpHandler {

    private final TimerService timerService = new TimerService();
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath(); // 예: /api/saveTime/yein
        String method = exchange.getRequestMethod();
        String[] parts = path.split("/");

        System.out.println("[DEBUG] 요청 경로: " + path + " / 분할 길이: " + parts.length);

        if (parts.length != 4) {
            sendResponse(exchange, 404, "경로 오류");
            return;
        }

        String userId = parts[3];

        if (method.equalsIgnoreCase("POST")) {
            InputStreamReader reader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            UserTime receivedTime = gson.fromJson(reader, UserTime.class);
            timerService.addOrUpdateUserTime(receivedTime);
            sendResponse(exchange, 200, "시간 저장 완료");
        } else if (method.equalsIgnoreCase("GET")) {
            UserTime userTime = timerService.getUserTime(userId);
            String json = gson.toJson(userTime);
            sendJson(exchange, 200, json);
        } else {
            sendResponse(exchange, 405, "지원하지 않는 메서드입니다");
        }
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String responseText) throws IOException {
        byte[] bytes = responseText.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        exchange.getResponseHeaders().add("Content-Type", "application/json; charset=UTF-8");
        sendResponse(exchange, statusCode, json);
    }
}