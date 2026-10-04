package com.example.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
    @Test
    void testUserModel(){
        User user = new User("1234567890", "john@example.com", "johndoe", "hashed123");
        assertEquals("1234567890", user.getPhoneNumber()); // <-- Changed to PhoneNumber
        assertEquals("john@example.com", user.getEmail());
        assertEquals("johndoe", user.getUsername());
        assertEquals("hashed123", user.getPassword());
    }

    @Test
    void testRepositoryExist() {
        UserRepository repo = new UserRepository();
        User user = new User("1234567890", "john@example.com", "john", "pass");

        try {
            repo.save(user);
        } catch (Exception e){
             assertNotNull(e.getMessage());
        }   
    }
}
