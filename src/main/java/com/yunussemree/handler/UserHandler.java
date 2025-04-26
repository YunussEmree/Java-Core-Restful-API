package com.yunussemree.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yunussemree.model.User;
import com.yunussemree.repository.UserRepository;
import com.yunussemree.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserHandler implements HttpHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserHandler.class);
    private final UserRepository userRepository;

    public UserHandler() {
        this.userRepository = new UserRepository();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        try {
            if ("GET".equals(method)) {
                handleGetRequest(exchange);
            } else if ("POST".equals(method)) {
                handlePostRequest(exchange);
            } else {
                sendResponse(exchange, 405, "Method Not Allowed");
            }
        } catch (Exception e) {
            logger.error("Error handling request", e);
            String errorMessage = "Server Error: " + e.getMessage();
            sendResponse(exchange, 500, errorMessage);
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        List<User> users = userRepository.findAll();
        sendResponse(exchange, 200, JsonUtils.toJson(users));
    }

    private void handlePostRequest(HttpExchange exchange) throws IOException {
        String requestBody = JsonUtils.inputStreamToString(exchange.getRequestBody());
        JsonNode jsonNode = JsonUtils.fromJson(requestBody);

        String name = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();
        String phone = jsonNode.get("phone").asText();

        if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Name and email are required");
            sendResponse(exchange, 400, JsonUtils.toJson(errorResponse));
            return;
        }

        User user = new User(name, email, phone);
        User savedUser = userRepository.save(user);

        sendResponse(exchange, 201, JsonUtils.toJson(savedUser));
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
