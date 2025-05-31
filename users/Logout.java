package com.domain.studyroom.users;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class Logout implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            UserHttpServer.sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        UserHttpServer.sendJson(exchange, 200, Map.of(
                "status", "success",
                "message", "로그아웃 완료"
        ));
    }
}

