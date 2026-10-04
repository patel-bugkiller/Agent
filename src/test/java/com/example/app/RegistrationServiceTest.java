package com.example.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {
    
    @Test
    void testValidationFailsForEmptyEmail() {
        RegistrationService service = new RegistrationService(new UserRepository());
        
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            service.registerUser("John", "", "john", "password123");
        });
        assertEquals("Email cannot be empty", e.getMessage());
    }

    @Test
    void testValidationFailsForShortPassword() {
        RegistrationService service = new RegistrationService(new UserRepository());
        
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            service.registerUser("John", "john@example.com", "john", "123");
        });
        assertEquals("Password must be at least 6 characters", e.getMessage());
    }
}
