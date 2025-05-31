package com.domain.studyroom.users;

import com.domain.studyroom.db.DB;
import com.domain.studyroom.todo.TodoController;
import com.google.gson.*;
import com.sun.net.httpserver.*;
import com.domain.studyroom.todo.MypageController;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;



public class UserHttpServer {
    private static final Gson gson = new Gson();

    public static class SignupHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            User newUser = gson.fromJson(body, User.class);

            try (Connection conn = DB.getConnection()) {
                String checkSql = "SELECT COUNT(*) FROM user WHERE username = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, newUser.username);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    sendJson(exchange, 409, Map.of("status", "fail", "message", "이미 존재하는 아이디입니다."));
                    return;
                }

                String insertSql = "INSERT INTO user (username, password) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertSql);
                pstmt.setString(1, newUser.username);
                pstmt.setString(2, newUser.password);
                pstmt.executeUpdate();

                sendJson(exchange, 200, Map.of("status", "success", "message", "회원가입 완료"));
            } catch (Exception e) {
                sendJson(exchange, 500, Map.of("status", "error", "message", e.getMessage()));
            }
        }
    }

    public static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            User loginReq = gson.fromJson(body, User.class);

            try (Connection conn = DB.getConnection()) {
                String sql = "SELECT password FROM user WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, loginReq.username);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next() || !rs.getString("password").equals(loginReq.password)) {
                    sendJson(exchange, 401, Map.of("status", "fail", "message", "아이디 또는 비밀번호가 잘못되었습니다."));
                    return;
                }

                sendJson(exchange, 200, Map.of("status", "success", "message", "로그인 성공"));
            } catch (Exception e) {
                sendJson(exchange, 500, Map.of("status", "error", "message", e.getMessage()));
            }
        }
    }

    public static class Logout implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            sendJson(exchange, 200, Map.of("status", "success", "message", "로그아웃 되었습니다."));
        }
    }

    static class User {
        String username;
        String password;
    }

    static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    static void sendJson(HttpExchange exchange, int statusCode, Map<String, Object> responseData) throws IOException {
        String json = gson.toJson(responseData);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
            os.flush();
        }
    }
}

