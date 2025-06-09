package com.domain.studyroom.jitsi;

import com.domain.studyroom.rooms.Room;
import com.domain.studyroom.rooms.RoomService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoomControllerMod implements HttpHandler {
    private final RoomService service = new RoomService();
    private final JitsiMeetService jitsiService = new JitsiMeetService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        String method = exchange.getRequestMethod();
        // preflight
        if ("OPTIONS".equals(method)) {
            exchange.sendResponseHeaders(200, -1);
            return;
        }

        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        if ("POST".equals(method) && path.equals("/api/rooms")) {
            handleCreate(exchange);
        }
        else if ("POST".equals(method) && path.matches("/api/rooms/\\d+/enter")) {
            int roomId = extractRoomId(path);
            handleEnterWithJitsi(exchange, roomId);
        }
        else if ("GET".equals(method) && path.matches("/api/rooms/\\d+/participants")) {
            int roomId = extractRoomId(path);
            handleGetParticipants(exchange, roomId);
        }
        else if (("DELETE".equals(method) || "POST".equals(method))
                && path.matches("/api/rooms/\\d+/leave")) {
            int roomId = extractRoomId(path);
            handleLeaveWithCleanup(exchange, roomId);
        }
        else if ("DELETE".equals(method) && path.matches("/api/rooms/\\d+/auto-delete")) {
            int roomId = extractRoomId(path);
            handleAutoDelete(exchange, roomId);
        }
        else if ("GET".equals(method) && path.equals("/api/rooms/list")) {
            handleGetRoomList(exchange);
        }
        else {
            send(exchange, 404, "Not Found");
        }
    }

    // 방 생성
    private void handleCreate(HttpExchange exchange) throws IOException {
        JsonObject req = readBody(exchange);
        String name    = req.get("name").getAsString();
        String password= req.has("password") ? req.get("password").getAsString() : null;
        String creator = req.has("creator")  ? req.get("creator").getAsString()  : "익명";

        try {
            int roomId = service.createRoom(name, password, creator);
            JsonObject resp = new JsonObject();
            resp.addProperty("roomId", roomId);
            resp.addProperty("success", true);
            resp.addProperty("message", "방 생성 성공");
            resp.addProperty("roomName", name);
            resp.addProperty("creator", creator);
            send(exchange, 200, new Gson().toJson(resp));
        } catch (SQLException | ClassNotFoundException e) {
            JsonObject err = new JsonObject();
            err.addProperty("success", false);
            err.addProperty("message", "서버 오류: " + e.getMessage());
            send(exchange, 500, new Gson().toJson(err));
        }
    }

    // 비밀번호 검증 + Jitsi 입장
    private void handleEnterWithJitsi(HttpExchange exchange, int roomId) throws IOException {
        try {
            // raw body 로깅
            byte[] raw = exchange.getRequestBody().readAllBytes();
            String body = new String(raw, StandardCharsets.UTF_8);
            System.out.println("▶️ raw request body: " + body);

            JsonObject req = JsonParser.parseString(body).getAsJsonObject();
            String username = req.get("username").getAsString();
            String password = (req.has("password") && !req.get("password").isJsonNull())
                    ? req.get("password").getAsString()
                    : null;

            System.out.println("🔐 입장 시도 - Room ID: " + roomId
                    + ", Password: " + (password != null ? "***" : "null"));

            boolean canEnter = service.enterRoom(roomId, password);
            System.out.println("🔐 검증 결과: " + canEnter);

            if (!canEnter) {
                JsonObject err = new JsonObject();
                err.addProperty("success", false);
                err.addProperty("message", "비밀번호가 올바르지 않습니다.");
                send(exchange, 401, new Gson().toJson(err));
                return;
            }

            JsonObject response = jitsiService.enterRoomWithJitsi(roomId, username);
            try {
                Room room = service.getRoomById(roomId);
                if (room != null) {
                    response.addProperty("roomName", room.getName());
                }
            } catch (Exception ignored) { }

            int status = response.get("success").getAsBoolean() ? 200 : 400;
            send(exchange, status, new Gson().toJson(response));

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            JsonObject err = new JsonObject();
            err.addProperty("success", false);
            err.addProperty("message", "데이터베이스 오류가 발생했습니다.");
            send(exchange, 500, new Gson().toJson(err));
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject err = new JsonObject();
            err.addProperty("success", false);
            err.addProperty("message", e.getMessage());
            send(exchange, 500, new Gson().toJson(err));
        }
    }

    // 참가자 목록
    private void handleGetParticipants(HttpExchange exchange, int roomId) throws IOException {
        try {
            List<String> participants = jitsiService.getParticipantUsernames(roomId);
            JsonObject resp = new JsonObject();
            resp.addProperty("success", true);
            resp.add("participants", new Gson().toJsonTree(participants));
            send(exchange, 200, new Gson().toJson(resp));
        } catch (Exception e) {
            JsonObject err = new JsonObject();
            err.addProperty("success", false);
            err.addProperty("message", e.getMessage());
            send(exchange, 500, new Gson().toJson(err));
        }
    }

    // 방 퇴장
    private void handleLeaveWithCleanup(HttpExchange exchange, int roomId) throws IOException {
        try {
            JsonObject req = readBody(exchange);
            String username = req.get("username").getAsString();
            boolean ok = jitsiService.leaveRoomWithCleanup(roomId, username);

            JsonObject resp = new JsonObject();
            resp.addProperty("success", ok);
            resp.addProperty("message", ok ? "퇴장 성공" : "퇴장 실패");
            send(exchange, ok ? 200 : 400, new Gson().toJson(resp));
        } catch (Exception e) {
            JsonObject err = new JsonObject();
            err.addProperty("success", false);
            err.addProperty("message", e.getMessage());
            send(exchange, 500, new Gson().toJson(err));
        }
    }

    // 방 자동 삭제
    private void handleAutoDelete(HttpExchange exchange, int roomId) throws IOException {
        try {
            boolean ok = jitsiService.autoDeleteRoom(roomId);
            JsonObject resp = new JsonObject();
            resp.addProperty("success", ok);
            resp.addProperty("message", ok ? "방 삭제 성공" : "방 삭제 실패");
            send(exchange, ok ? 200 : 400, new Gson().toJson(resp));
        } catch (Exception e) {
            JsonObject err = new JsonObject();
            err.addProperty("success", false);
            err.addProperty("message", e.getMessage());
            send(exchange, 500, new Gson().toJson(err));
        }
    }

    // 전체 방 목록
    private void handleGetRoomList(HttpExchange exchange) throws IOException {
        try {
            List<Room> rooms = service.getAllRooms();
            List<JsonObject> list = new ArrayList<>();
            for (Room room : rooms) {
                JsonObject o = new JsonObject();
                o.addProperty("roomId",   room.getId());
                o.addProperty("roomName", room.getName());
                o.addProperty("isPrivate", room.getPassword() != null && !room.getPassword().isEmpty());
                if (room.getCreator() != null) {
                    o.addProperty("creator", room.getCreator());
                }
                list.add(o);
            }
            send(exchange, 200, new Gson().toJson(list));
        } catch (Exception e) {
            JsonObject err = new JsonObject();
            err.addProperty("success", false);
            err.addProperty("message", e.getMessage());
            send(exchange, 500, new Gson().toJson(err));
        }
    }

    // Body → JsonObject
    private JsonObject readBody(HttpExchange exchange) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
            return JsonParser.parseString(sb.toString()).getAsJsonObject();
        }
    }

    // Response 전송
    private void send(HttpExchange exchange, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Path에서 roomId 추출
    private int extractRoomId(String path) {
        return Integer.parseInt(path.split("/")[3]);
    }
}
