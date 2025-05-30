package com.domain.studyroom.rooms;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.URI;

public class RoomController implements HttpHandler {
    private final RoomService service = new RoomService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        URI uri = exchange.getRequestURI();
        String path = uri.getPath();

        if (method.equals("POST") && path.equals("/api/rooms")) {
            handleCreate(exchange);
        } else if (method.equals("POST") && path.matches("/api/rooms/\\d+/enter")) {
            int roomId = extractRoomId(path);
            handleEnter(exchange, roomId);
        } else if (method.equals("DELETE") && path.matches("/api/rooms/\\d+/leave")) {
            int roomId = extractRoomId(path);
            handleLeave(exchange, roomId);
        } else if (method.equals("DELETE") && path.matches("/api/rooms/\\d+/auto-delete")) {
            int roomId = extractRoomId(path);
            handleAutoDelete(exchange, roomId);
        } else {
            send(exchange, 404, "Not Found");
        }
    }

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

    private void handleEnter(HttpExchange exchange, int roomId) throws IOException {
        try {
            if (service.enterRoom(roomId)) send(exchange, 200, "입장 성공");
            else send(exchange, 400, "입장 실패");
        } catch (Exception e) {
            send(exchange, 500, e.getMessage());
        }
    }

    private void handleLeave(HttpExchange exchange, int roomId) throws IOException {
        try {
            if (service.leaveRoom(roomId)) send(exchange, 200, "퇴장 성공");
            else send(exchange, 400, "퇴장 실패");
        } catch (Exception e) {
            send(exchange, 500, e.getMessage());
        }
    }

    private void handleAutoDelete(HttpExchange exchange, int roomId) throws IOException {
        try {
            if (service.autoDeleteRoom(roomId)) send(exchange, 200, "방 삭제 성공");
            else send(exchange, 400, "방 삭제 실패");
        } catch (Exception e) {
            send(exchange, 500, e.getMessage());
        }
    }

    private JsonObject readBody(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) sb.append(line);
        return JsonParser.parseString(sb.toString()).getAsJsonObject();
    }

    private void send(HttpExchange exchange, int code, String body) throws IOException {
        exchange.sendResponseHeaders(code, body.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(body.getBytes());
        os.close();
    }

    private int extractRoomId(String path) {
        return Integer.parseInt(path.split("/")[3]);
    }
}
