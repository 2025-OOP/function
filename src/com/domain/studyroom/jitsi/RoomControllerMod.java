package com.domain.studyroom.jitsi;

import com.domain.studyroom.rooms.RoomService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URI;
import java.util.List;

public class RoomControllerMod implements HttpHandler {
    private final RoomService service = new RoomService();
    private final JitsiMeetService jitsiService = new JitsiMeetService();  // 추가

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        // CORS 헤더 추가 (React 연동용)
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");



        String method = exchange.getRequestMethod();


        // OPTIONS 요청 처리 (CORS preflight)
        if (method.equals("OPTIONS")) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }


        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        if (method.equals("POST") && path.equals("/api/rooms")) {
            handleCreate(exchange);
        } else if (method.equals("POST") && path.matches("/api/rooms/\\d+/enter")) {
            int roomId = extractRoomId(path);
            handleEnterWithJitsi(exchange, roomId);  // 변경됨
        } else if (method.equals("GET") && path.matches("/api/rooms/\\d+/participants")) {
            int roomId = extractRoomId(path);
            handleGetParticipants(exchange, roomId);  // 추가됨
        } else if (method.equals("DELETE") && path.matches("/api/rooms/\\d+/leave")) {
            int roomId = extractRoomId(path);
            handleLeaveWithCleanup(exchange, roomId);  // 변경됨
        } else if (method.equals("DELETE") && path.matches("/api/rooms/\\d+/auto-delete")) {
            int roomId = extractRoomId(path);
            handleAutoDelete(exchange, roomId);
        } else {
            send(exchange, 404, "Not Found");
        }
    }

    // 기존 메소드들은 그대로 유지
    private void handleCreate(HttpExchange exchange) throws IOException {
        JsonObject req = readBody(exchange);
        String name = req.get("name").getAsString();
        String password = req.has("password") ? req.get("password").getAsString() : null;
        try {
            int roomId = service.createRoom(name, password);
            JsonObject response = new JsonObject();
            response.addProperty("roomId", roomId);
            send(exchange, 200, new Gson().toJson(response));
        } catch (Exception e) {
            send(exchange, 500, e.getMessage());
        }
    }

    // 수정된 메소드: Jitsi 설정을 포함한 방 입장
    private void handleEnterWithJitsi(HttpExchange exchange, int roomId) throws IOException {
        try {
            JsonObject req = readBody(exchange);
            String username = req.get("username").getAsString();

            JsonObject response = jitsiService.enterRoomWithJitsi(roomId, username);

            int statusCode = response.get("success").getAsBoolean() ? 200 : 400;
            send(exchange, statusCode, new Gson().toJson(response));
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", e.getMessage());
            send(exchange, 500, new Gson().toJson(errorResponse));
        }
    }

    // 새로운 메소드: 참가자 목록 조회
    private void handleGetParticipants(HttpExchange exchange, int roomId) throws IOException {
        try {
            List<String> participants = jitsiService.getParticipantUsernames(roomId);

            JsonObject response = new JsonObject();
            response.addProperty("success", true);
            response.add("participants", new Gson().toJsonTree(participants));

            send(exchange, 200, new Gson().toJson(response));
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", e.getMessage());
            send(exchange, 500, new Gson().toJson(errorResponse));
        }
    }

    // 수정된 메소드: 참가자 정보 정리를 포함한 방 퇴장
    private void handleLeaveWithCleanup(HttpExchange exchange, int roomId) throws IOException {
        try {
            JsonObject req = readBody(exchange);
            String username = req.get("username").getAsString();

            if (jitsiService.leaveRoomWithCleanup(roomId, username)) {
                JsonObject response = new JsonObject();
                response.addProperty("success", true);
                response.addProperty("message", "퇴장 성공");
                send(exchange, 200, new Gson().toJson(response));
            } else {
                JsonObject response = new JsonObject();
                response.addProperty("success", false);
                response.addProperty("message", "퇴장 실패");
                send(exchange, 400, new Gson().toJson(response));
            }
        } catch (Exception e) {
            JsonObject errorResponse = new JsonObject();
            errorResponse.addProperty("success", false);
            errorResponse.addProperty("message", e.getMessage());
            send(exchange, 500, new Gson().toJson(errorResponse));
        }
    }

    // 수정된 메소드: 참가자 정보도 함께 삭제
    private void handleAutoDelete(HttpExchange exchange, int roomId) throws IOException {
        try {
            if (jitsiService.autoDeleteRoom(roomId)) {
                send(exchange, 200, "방 삭제 성공");
            } else {
                send(exchange, 400, "방 삭제 실패");
            }
        } catch (Exception e) {
            send(exchange, 500, e.getMessage());
        }
    }

    // 유틸리티 메소드들
    private JsonObject readBody(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        return JsonParser.parseString(sb.toString()).getAsJsonObject();
    }

    private void send(HttpExchange exchange, int code, String body) throws IOException {
        byte[] bytes = body.getBytes("UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private int extractRoomId(String path) {
        return Integer.parseInt(path.split("/")[3]);
    }
}