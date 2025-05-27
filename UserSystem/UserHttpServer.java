package UserSystem;// 	localhost:8080에 HTTP 서버 열고, /signup·/login 요청 처리함

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import com.sun.net.httpserver.HttpServer;
import java.security.MessageDigest;
import java.sql.*;
import java.util.*;

import com.google.gson.*;

public class UserHttpServer {
    private static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/signup", new SignupHandler());
        server.createContext("/login", new LoginHandler());
        server.createContext("/logout", new Logout());
        server.setExecutor(null);
        System.out.println("HTTP 서버 실행 중 (포트 8080)...");
        server.start();
    }

    static class SignupHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            User newUser = gson.fromJson(body, User.class);

            try (Connection conn = getConnection()) {
                // 중복 사용자 확인
                String checkSql = "SELECT COUNT(*) FROM user WHERE username = ?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, newUser.username);
                ResultSet rs = checkStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    sendJson(exchange, 409, Map.of("status", "fail", "message", "이미 존재하는 아이디입니다."));
                    return;
                }

                // 사용자 추가
                String hashed = hashPassword(newUser.password);
                String insertSql = "INSERT INTO user (username, password) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(insertSql);
                pstmt.setString(1, newUser.username);
                pstmt.setString(2, hashed);
                pstmt.executeUpdate();

                sendJson(exchange, 200, Map.of("status", "success", "message", "회원가입 완료"));
            } catch (Exception e) {
                sendJson(exchange, 500, Map.of("status", "error", "message", e.getMessage()));
            }
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }

            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            User loginReq = gson.fromJson(body, User.class);

            try (Connection conn = getConnection()) {
                String sql = "SELECT password FROM user WHERE username = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, loginReq.username);
                ResultSet rs = pstmt.executeQuery();

                if (!rs.next()) {
                    sendJson(exchange, 401, Map.of("status", "fail", "message", "아이디 또는 비밀번호가 잘못되었습니다."));
                    return;
                }

                String storedHashed = rs.getString("password");
                if (!storedHashed.equals(hashPassword(loginReq.password))) {
                    sendJson(exchange, 401, Map.of("status", "fail", "message", "아이디 또는 비밀번호가 잘못되었습니다."));
                    return;
                }

                sendJson(exchange, 200, Map.of("status", "success", "message", "로그인 성공"));
            } catch (Exception e) {
                sendJson(exchange, 500, Map.of("status", "error", "message", e.getMessage()));
            }
        }
    }

    static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("암호화 실패");
        }
    }

    static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    static void sendJson(HttpExchange exchange, int statusCode, Map<String, Object> responseData) throws IOException {
        String json = gson.toJson(responseData);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        sendResponse(exchange, statusCode, json);
    }

    static Connection getConnection() throws SQLException, ClassNotFoundException {
        String url = "jdbc:mysql://localhost:3306/java_studyroom_project?serverTimezone=UTC&characterEncoding=UTF-8";
        String dbUser = "root";
        String dbPassword = "0107";
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, dbUser, dbPassword);
    }

    static class User {
        String username;
        String password;
    }
}