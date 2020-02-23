package com.example.api;

public class LoginCredentials {
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }

    public LoginCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
