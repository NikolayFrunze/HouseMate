package org.example.dto;

/**
 * Data sent back to the client (Android) after creating or logging in a user.
 * It never includes the password. Spring converts this record to JSON automatically.
 */
public record UserResponse(Long id, String username, String email) {
}
