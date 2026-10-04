package com.example.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {
    
    @Test
    void testValidationFailsForEmptyEmail() {
        RegistrationService service = new RegistrationService(new UserRepository());
        
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            service.registerUser("1234567890", "", "john", "password123");
        });
        assertEquals("Email cannot be empty", e.getMessage());
    }

    @Test
    void testValidationFailsForShortPassword() {
        RegistrationService service = new RegistrationService(new UserRepository());
        
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            service.registerUser("1234567890", "john@example.com", "john", "123");
        });
        assertEquals("Password must be at least 6 characters", e.getMessage());
    }

    // New test to ensure Phone Number validation works
    @Test
    void testValidationFailsForEmptyPhoneNumber() {
        RegistrationService service = new RegistrationService(new UserRepository());
        
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            service.registerUser("", "john@example.com", "john", "password123");
        });
        assertEquals("Phone number cannot be empty", e.getMessage());
    }
}
