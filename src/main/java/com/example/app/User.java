package com.example.app;

public class User {
    private String phoneNumber;
    private String email;
    private String username;
    private String password;

    public User(String phoneNumber, String email, String username, String password) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getPhoneNumber() { return phoneNumber; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
