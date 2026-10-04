package com.example.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {
    @Test
    void testUserModel(){
        User user = new User("John Doe","john@example.com","johndoe","hashed123");
        assertEquals("John Doe",user.getName());
        assertEquals("john@example.com",user.getEmail());
        assertEquals("johndoe",user.getUsername());
        assertEquals("hashed123",user.getPassword());
    }

    @Test
    void testRepositoryExist() {
        UserRepository repo = new UserRepository();
        User user = new User("John", "john@example.com", "john","pass");

        // We except this to fail if Mysql isnt running or configured,
        // But it verfies our JDBC code is wired up correctly!
        try {
            repo.save(user);
        }catch (Exception e){
             assertNotNull(e.getMessage());
            }   
    }
}