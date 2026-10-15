package com.example.app;

import org.mindrot.jbcrypt.BCrypt;

public class LoginService {
    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User loginUser(String email, String password) {
        // 1. Validate inputs
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Email and password are required");
        }

        // 2. Look up the user by email
        User user = userRepository.findByEmail(email);
        
        // 3. If user doesn't exist, fail safely without revealing that the email isn't registered
        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // 4. Verify the password securely against the hash stored in the database
        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Login successful!
        return user;
    }
}
