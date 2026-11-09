package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data received in POST /users to create a new user.
 * A record is an immutable class: its fields are set in the constructor
 * and cannot be changed later. The annotations are checked automatically
 * because the controller uses @Valid.
 */
public record CreateUserRequest(
        // Name: required, between 3 and 50 characters
        @NotBlank @Size(min = 3, max = 50) String username,
        // Email: required, must have a valid format, max 255 characters
        @NotBlank @Email @Size(max = 255) String email,
        // BCrypt only uses the first 72 bytes, that is why the maximum is 72
        @NotBlank @Size(min = 8, max = 72) String password
) {
}
