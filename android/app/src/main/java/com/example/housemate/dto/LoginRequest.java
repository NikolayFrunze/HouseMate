package com.example.housemate.dto;

/**
 * Data sent to the backend to check a login (POST /users/login).
 * Gson converts it to JSON: {"email": "...", "password": "..."}
 */
public class LoginRequest {

    // Email typed by the user
    public final String email;
    // Password typed by the user (sent in plain text over the connection)
    public final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
