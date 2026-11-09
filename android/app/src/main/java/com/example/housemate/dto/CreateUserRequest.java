package com.example.housemate.dto;

/**
 * Data sent to the backend to create a new user (POST /users).
 * Gson converts it to JSON: {"username": "...", "email": "...", "password": "..."}
 */
public class CreateUserRequest {

    // Name chosen by the user
    public final String username;
    // Email of the user
    public final String email;
    // Password in plain text (the backend encrypts it before saving)
    public final String password;

    public CreateUserRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
