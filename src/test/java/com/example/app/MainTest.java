package com.example.app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
class MainTest {
    @Test 
    void testGreeting(){
        Main app = new Main();
        assertEquals("Application is running", app.getGreeting());
    }
}