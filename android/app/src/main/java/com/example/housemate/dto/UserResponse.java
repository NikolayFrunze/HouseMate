package com.example.housemate.dto;

/**
 * Data received from the backend after creating or logging in a user.
 * Field names must match the JSON keys sent by Spring Boot (id, username, email).
 * Gson fills these fields automatically.
 */
public class UserResponse {

    // Unique id of the user in the database
    public Long id;
    // Name of the user
    public String username;
    // Email of the user
    public String email;
}
