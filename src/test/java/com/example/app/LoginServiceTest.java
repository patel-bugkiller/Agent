package com.example.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {
    
    @Test
    void testLoginValidatesEmptyFields() {
        LoginService service = new LoginService(new UserRepository());
        
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            service.loginUser("", "password123");
        });
        assertEquals("Email and password are required", e.getMessage());
    }

    @Test
    void testLoginFailsForNonExistentUser() {
        LoginService service = new LoginService(new UserRepository());
        
        Exception e = assertThrows(Exception.class, () -> {
            service.loginUser("ghost@example.com", "password123");
        });
        assertNotNull(e.getMessage());
    }
}
