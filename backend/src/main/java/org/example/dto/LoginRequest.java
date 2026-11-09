package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Data received in POST /users/login to check a user's credentials.
 * The annotations are validated automatically because the controller uses @Valid.
 */
public record LoginRequest(
        // Email: required, valid format, max 255 characters
        @NotBlank @Email @Size(max = 255) String email,
        // Password: required, max 72 characters (same limit as BCrypt in registration)
        @NotBlank @Size(max = 72) String password
) {
}
