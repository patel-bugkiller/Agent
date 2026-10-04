package com.example.app;

import org.mindrot.jbcrypt.BCrypt;

public class RegistrationService {
    private final UserRepository userRepository;

    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(String name, String email, String username, String password) {
        // 1. Validation
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (password == null || password.trim().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        // 2. Hash password securely using BCrypt
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        // 3. Save the user
        User user = new User(name, email, username, hashedPassword);
        userRepository.save(user);
    }
}
