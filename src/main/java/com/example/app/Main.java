package com.example.app;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        
        UserRepository repo = new UserRepository();
        RegistrationService service = new RegistrationService(repo);
        
        // Handle API requests
        server.createContext("/api/register", new RegistrationHandler(service));
        
        // Handle Webpage requests (HTML/CSS/JS)
        server.createContext("/", new StaticFileHandler("C:/Users/kailash/Downloads/Project-web/frontend"));

        
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}
