package com.example.app;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RegistrationHandler implements HttpHandler {
    private final RegistrationService registrationService;

    public RegistrationHandler(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Enable CORS so our frontend can communicate with the backend
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        if ("OPTIONS".equals(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!"POST".equals(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Method Not Allowed");
            return;
        }

        try {
            // Read and parse the incoming form data
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Map<String, String> params = parseFormData(body);
            
            // Delegate to our RegistrationService
            registrationService.registerUser(
                params.get("name"),
                params.get("email"),
                params.get("username"),
                params.get("password")
            );
            
            sendResponse(exchange, 201, "Registration successful");
            
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, e.getMessage()); // 400 Bad Request for validation errors
        } catch (Exception e) {
            // Check for MySQL duplicate entry error
            if (e.getMessage() != null && e.getMessage().contains("Duplicate entry")) {
                sendResponse(exchange, 409, "Email or Username already exists");
            } else {
                e.printStackTrace();
                sendResponse(exchange, 500, "Internal Server Error");
            }
        }
    }

    private Map<String, String> parseFormData(String body) {
        Map<String, String> map = new HashMap<>();
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                map.put(
                    URLDecoder.decode(kv[0], StandardCharsets.UTF_8),
                    URLDecoder.decode(kv[1], StandardCharsets.UTF_8)
                );
            }
        }
        return map;
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
