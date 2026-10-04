package com.example.app;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;

public class Main {
    public static void main(String[] args) throws Exception {
        // Wire up our dependencies
        UserRepository repo = new UserRepository();
        RegistrationService service = new RegistrationService(repo);
        
        // Start the server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/register", new RegistrationHandler(service));
        
        server.setExecutor(null); 
        server.start();
        System.out.println("Server started on http://localhost:8080");
    }
}
